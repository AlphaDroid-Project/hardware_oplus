package com.oplus.media;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.FileDescriptor;

public class OplusHeifWriter {
    public static final int COLOR_FMT_MAX = 5;
    public static final int COLOR_FMT_NV12 = 3;
    public static final int COLOR_FMT_NV21 = 4;
    public static final int COLOR_FMT_P010 = 1;
    public static final int COLOR_FMT_RGBA8888 = 2;
    public static final int COLOR_FMT_YUV420Planar = 0;
    private static final String TAG = "OplusHeifWriter_Java";
    static final int maxValue = 100;
    static final int minValue = 0;
    private long mNativeObject = nativeSetup();

    public static class Options {
        public byte[] fileExtender;
        public FileDescriptor gainmapFd;
        public long mcroVideoPresentationTimestampUs;
        public FileDescriptor videoFd;
        public String xmpData;
    }

    private static native long nativeCreate(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native long nativeCreateLivePhoto(long j, byte[] bArr, byte[] bArr2, FileDescriptor fileDescriptor, Options options);

    private static native long nativeCreateLivePhotoByBmp(long j, Bitmap bitmap, byte[] bArr, FileDescriptor fileDescriptor, Options options);

    private static native void nativeDestory(long j);

    private static native long nativeProcessHeicPhotoFrame(long j, byte[] bArr, byte[] bArr2, FileDescriptor fileDescriptor);

    private static native long nativeSetup();

    static {
        Log.v(TAG, "loadLibrary");
        System.loadLibrary("oplusheifwriter");
    }

    public boolean createPrimaryImage(int width, int height, int strideWidth, int strideHeight, int fmt, int quality, int rotation) {
        if (quality > 0 && quality <= 100) {
            if (width > 0 && height > 0 && strideWidth > 0 && strideHeight > 0 && fmt >= 0 && fmt < 5) {
                long ret = nativeCreate(this.mNativeObject, width, height, strideWidth, strideHeight, fmt, quality, rotation);
                Log.i(TAG, " OplusHeifWriter start! quality: " + quality);
                if (ret < 0) {
                    return false;
                }
                return true;
            }
            Log.i(TAG, "Input param error.");
            return false;
        }
        IllegalArgumentException e = new IllegalArgumentException("quality range error");
        throw e;
    }

    private void createTrack() {
    }

    private void addTrackSample() {
    }

    public boolean processPrimaryImage(byte[] yuvBuffer, byte[] exifData, FileDescriptor fd) {
        long ret = nativeProcessHeicPhotoFrame(this.mNativeObject, yuvBuffer, exifData, fd);
        if (ret < 0) {
            return false;
        }
        return true;
    }

    public boolean processPrimaryLivePhoto(byte[] yuvBuffer, byte[] exifData, FileDescriptor outFd, Options opts) {
        long ret = nativeCreateLivePhoto(this.mNativeObject, yuvBuffer, exifData, outFd, opts);
        if (ret < 0) {
            Log.i(TAG, "processPrimaryLivePhoto failed!");
            return false;
        }
        return true;
    }

    public boolean processPrimaryLivePhoto(Bitmap bmp, byte[] exifData, FileDescriptor outFd, Options opts) {
        long ret = nativeCreateLivePhotoByBmp(this.mNativeObject, bmp, exifData, outFd, opts);
        if (ret < 0) {
            Log.i(TAG, "processPrimaryLivePhoto failed!");
            return false;
        }
        return true;
    }

    public void destory() {
        Log.i(TAG, " OplusHeifWriter destory!");
        nativeDestory(this.mNativeObject);
        this.mNativeObject = 0L;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mNativeObject != 0) {
                destory();
                this.mNativeObject = 0L;
            }
        } finally {
            super.finalize();
        }
    }
}
