package android.view;

public class OplusWindowManager {

    public OplusWindowManager() {}

    public void requestKeyguard(String command) {}

    // OplusCamera CameraPreviewAnimator -> DisplayUtil calls this; missing -> NoSuchMethodError
    // crash. Stub per dodge oplus-fwk parity.
    public boolean setPreferredDisplayMode(int mode) { return false; }
}
