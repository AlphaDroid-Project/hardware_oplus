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
 * Wraps a real android.hardware.camera2.CaptureResult and exposes its underlying native
 * CameraMetadataNative, used by the OPlus camera-unit SDK (APS) to obtain the metadata pointer.
 */
public class CaptureResult {
    private static final String TAG = "CaptureResult";

    private final android.hardware.camera2.CaptureResult mCaptureResult;

    public CaptureResult(android.hardware.camera2.CaptureResult result) {
        this.mCaptureResult = result;
    }

    public CameraMetadataNative getNativeMetadata() {
        Object nativeMeta = null;
        if (this.mCaptureResult != null) {
            try {
                try {
                    Field results = android.hardware.camera2.CaptureResult.class
                            .getDeclaredField("mResults");
                    results.setAccessible(true);
                    nativeMeta = results.get(this.mCaptureResult);
                } catch (NoSuchFieldException e) {
                    Method getNativeCopy = android.hardware.camera2.CaptureResult.class
                            .getDeclaredMethod("getNativeCopy");
                    getNativeCopy.setAccessible(true);
                    nativeMeta = getNativeCopy.invoke(this.mCaptureResult);
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to obtain native metadata from CaptureResult", e);
            }
        }
        // kiro-119 (2026-06-24): nativeMeta here is the LIVE android.hardware.camera2.impl.
        // CameraMetadataNative backing this per-frame CaptureResult. The SDK (ApsUtils.
        // getMetadataPtrForJni) takes the raw mMetadataPtr from this and hands it to native EIS/
        // SuperEIS code, which processes it ASYNCHRONOUSLY on a separate thread (preview_superei).
        // AOSP recycles/frees CaptureResult's native metadata essentially as soon as the per-frame
        // callback returns, so by the time the async EIS thread dereferences this pointer, the
        // buffer may already be gone -- a use-after-free with a different fault address every time,
        // matching exactly what we see. Stock's real (OPlus-modified) framework presumably keeps
        // result buffers alive long enough for this same live-pointer pattern to be safe; pure AOSP
        // does not, and we can't change AOSP's recycling policy from here.
        //
        // Fix: deep-copy the metadata via AOSP's own nativeAllocateCopy (the same native allocator
        // CameraMetadataNative's public copy-constructor uses) and pass the COPY's raw pointer
        // through instead -- never wrapped in any Java object on our side, since this trivial
        // wrapper class has no finalizer and nothing else will free it. The native side already
        // imports free_camera_metadata (confirmed via the libAlgoProcess.so dynamic symbol table),
        // i.e. it already expects to take ownership of and free metadata pointers it's handed, so
        // handing it an independently-allocated copy is exactly the convention it's built for.
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
