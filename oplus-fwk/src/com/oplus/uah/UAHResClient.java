package com.oplus.uah;

import com.oplus.uah.info.UAHEventRequest;
import com.oplus.uah.info.UAHResRequest;
import com.oplus.uah.info.UAHRuleCtrlRequest;

public class UAHResClient {

    private UAHResClient(Class cls) {}

    public static UAHResClient get(Class cls) {
        return new UAHResClient(cls);
    }

    public int acquireEvent(UAHEventRequest request) {
        return 0;
    }

    public int acquireRes(UAHResRequest request) {
        return 0;
    }

    public int acquireRuleCtrl(UAHRuleCtrlRequest request) {
        return 0;
    }

    public void release(int handle) {}
}
