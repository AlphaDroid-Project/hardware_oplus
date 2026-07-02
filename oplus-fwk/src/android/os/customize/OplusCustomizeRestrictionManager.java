package android.os.customize;

import android.content.Context;

public class OplusCustomizeRestrictionManager {
    public static OplusCustomizeRestrictionManager getInstance(Context context) {
        return new OplusCustomizeRestrictionManager();
    }

    public boolean getForbidRecordScreenState() {
        return false;
    }

    public boolean isPrivateSafeDisabled() {
        return true;
    }
}
