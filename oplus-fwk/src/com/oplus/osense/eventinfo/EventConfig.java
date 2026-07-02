package com.oplus.osense.eventinfo;

import java.util.HashSet;

public class EventConfig {

    public EventConfig(HashSet<Integer> events) {}

    // OplusCamera OCAM_Performance references this; missing -> NoSuchMethodError. Stub per dodge.
    public void setOsenseConfigSet(HashSet<String> hashSet) {}
}
