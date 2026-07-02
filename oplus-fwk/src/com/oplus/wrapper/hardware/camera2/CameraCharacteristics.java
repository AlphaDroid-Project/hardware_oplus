/*
 * SPDX-FileCopyrightText: 2026 AlphaDroid
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.hardware.camera2;

import android.util.Log;

import com.oplus.wrapper.hardware.camera2.impl.CameraMetadataNative;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Wraps a real android.hardware.camera2.CameraCharacteristics and exposes its underlying native
 * CameraMetadataNative, used by the OPlus camera-unit SDK (APS) to obtain the metadata pointer.
 *
 * kiro-120 (2026-06-24): this class did not exist at all before this fix. ApsUtils.
 * getMetadataPtrForJni(Object) does `Class.forName("com.oplus.wrapper.hardware.camera2.
 * CameraCharacteristics")` and, on a build where that throws (i.e. ours, until now), silently falls
 * back to its OWN raw reflection on AOSP's real CameraCharacteristics.mProperties field -- the LIVE,
 * SHARED metadata object, with no copy. Unlike CaptureResult (recreated fresh per frame),
 * CameraCharacteristics is obtained once and reused for the entire camera session, including being
 * passed into the native SuperEIS bridge (OplusSuperEISPreview.getEisFrameHom/processImage) on EVERY
 * frame. If the native side frees the pointer it's handed (it imports free_camera_metadata, so it
 * expects ownership-transfer semantics), the SECOND frame's reflection would return the SAME pointer
 * the FIRST frame's call already had freed -- a use-after-free / double-free on a long-lived, shared
 * buffer, repeating every frame. That fits the symptom exactly: ~400 frames of escalating corruption
 * before Scudo's "corrupted chunk header" detector finally trips, rather than an instant crash.
 *
 * Fix: same approach as CaptureResult.java -- deep-copy via AOSP's own nativeAllocateCopy before
 * handing off the pointer, passed through as a raw boxed Long, never wrapped in a Java object on our
 * side (so nothing on the Java side can race a finalizer-triggered free against the native side's).
 */
public class CameraCharacteristics {
    private static final String TAG = "CameraCharacteristics";

    private final android.hardware.camera2.CameraCharacteristics mCameraCharacteristics;

    public CameraCharacteristics(android.hardware.camera2.CameraCharacteristics characteristics) {
        this.mCameraCharacteristics = characteristics;
    }

    public CameraMetadataNative getNativeMetadata() {
        Object nativeMeta = null;
        if (this.mCameraCharacteristics != null) {
            try {
                Field properties = android.hardware.camera2.CameraCharacteristics.class
                        .getDeclaredField("mProperties");
                properties.setAccessible(true);
                nativeMeta = properties.get(this.mCameraCharacteristics);
            } catch (Exception e) {
                Log.e(TAG, "Failed to obtain native metadata from CameraCharacteristics", e);
            }
        }
        // See class-level comment: this MUST be an independent copy, not the live mProperties
        // reference, since this is shared across every frame of the camera session.
        if (nativeMeta != null) {
            try {
                Field ptrField = nativeMeta.getClass().getDeclaredField("mMetadataPtr");
                ptrField.setAccessible(true);
                long livePtr = ptrField.getLong(nativeMeta);
                if (livePtr != 0) {
                    Method allocCopy = nativeMeta.getClass()
                            .getDeclaredMethod("nativeAllocateCopy", long.class);
                    allocCopy.setAccessible(true);
                    long copyPtr = (Long) allocCopy.invoke(null, livePtr);
                    if (copyPtr != 0) {
                        return new CameraMetadataNative(Long.valueOf(copyPtr));
                    }
                    Log.e(TAG, "nativeAllocateCopy returned 0, falling back to live metadata");
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to deep-copy native metadata, falling back to live reference", e);
            }
        }
        return new CameraMetadataNative(nativeMeta);
    }
}
