package com.oplus.hardware.camera2;

import android.hardware.camera2.CaptureResult;

public interface OplusCaptureResult {
    CaptureResult getCaptureResult();
    long getFrameNumber();
    long getTimestamp();
    <T> T get(CaptureResult.Key<T> key);
    boolean isCloseable();
    CloseHandle addResultRefCount(String tag);

    public static class CloseHandle {
        public static final CloseHandle INVALID = new CloseHandle();

        public CloseHandle() {
        }

        public void init(OplusCaptureResultManager manager, OplusCaptureResult result, String tag) {
        }

        public static boolean isValid(CloseHandle handle) {
            return handle != null && handle != INVALID;
        }

        public static void a(CloseHandle handle, OplusCaptureResultManager manager) {
        }

        public static CloseHandle close(CloseHandle handle) {
            return null;
        }

        public void onClose() {
        }

        public static void lambda$onClose$0(OplusCaptureResultManager manager) {
        }
    }
}
