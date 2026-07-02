package android.app;

import android.content.Context;

import java.util.ArrayList;

public class OplusWhiteListManager {

    public OplusWhiteListManager(Context context) {}

    public ArrayList<String> getStageProtectListFromPkg(String pkg, int type) {
        return new ArrayList<String>();
    }

    public void addStageProtectInfo(String pkg, long timeout) {}

    // OplusCamera ApsService.onStop calls this 4-arg overload; missing → NoSuchMethodError crash.
    public void addStageProtectInfo(String pkg, String reason, long timeout, com.oplus.app.IOplusProtectConnection connection) {}

    public void removeStageProtectInfo(String pkg) {}
}
