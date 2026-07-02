package com.oplus.wrapper.view;

public class Surface {
    private final android.view.Surface mSurface;

    public static void destroy(android.view.Surface target) {
        target.destroy();
    }

    public Surface() {
        mSurface = new android.view.Surface();
    }

    public Surface(android.view.Surface surface) {
        mSurface = surface;
    }

    public void destroy() {
        mSurface.destroy();
    }

    public android.view.Surface getSurface() {
        return mSurface;
    }
}
