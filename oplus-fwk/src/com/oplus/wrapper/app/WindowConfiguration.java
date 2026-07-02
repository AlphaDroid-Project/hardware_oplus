/*
 * SPDX-FileCopyrightText: 2026 AlphaDroid
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.app;

import android.graphics.Rect;

public class WindowConfiguration {
    private final android.app.WindowConfiguration mWindowConfiguration;

    public WindowConfiguration(android.app.WindowConfiguration windowConfiguration) {
        this.mWindowConfiguration = windowConfiguration;
    }

    public Rect getAppBounds() {
        if (this.mWindowConfiguration == null) {
            return null;
        }
        return this.mWindowConfiguration.getAppBounds();
    }
}
