/*
 * SPDX-FileCopyrightText: 2026 AlphaDroid
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.hardware.camera2.impl;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * Wraps a real android.hardware.camera2.impl.CameraMetadataNative instance and exposes its
 * native metadata pointer, which the OPlus camera-unit SDK (APS) consumes via
 * {@code CameraMetadataNative.getMetadataPtr()}.
 */
public class CameraMetadataNative {
    private static final String TAG = "CameraMetadataNative";

    private final Object mNativeMetadata;

    public CameraMetadataNative(Object nativeMetadata) {
        this.mNativeMetadata = nativeMetadata;
    }

    public long getMetadataPtr() {
        if (this.mNativeMetadata == null) {
            return 0L;
        }
        // kiro-119: CaptureResult.getNativeMetadata() now passes an already-independent, deep-
        // copied pointer through directly as a boxed Long (see its comment for why) instead of a
        // live android.hardware.camera2.impl.CameraMetadataNative reference. Handle both.
        if (this.mNativeMetadata instanceof Long) {
            return (Long) this.mNativeMetadata;
        }
        try {
            Field ptrField = this.mNativeMetadata.getClass().getDeclaredField("mMetadataPtr");
            ptrField.setAccessible(true);
            return ptrField.getLong(this.mNativeMetadata);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read mMetadataPtr from "
                    + this.mNativeMetadata.getClass().getName(), e);
            return 0L;
        }
    }
}
