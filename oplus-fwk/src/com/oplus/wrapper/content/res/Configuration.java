/*
 * SPDX-FileCopyrightText: 2026 AlphaDroid
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.content.res;

import com.oplus.wrapper.app.WindowConfiguration;

public class Configuration {
    private final android.content.res.Configuration mConfiguration;

    public Configuration(android.content.res.Configuration config) {
        this.mConfiguration = config;
    }

    public WindowConfiguration getWindowConfiguration() {
        if (this.mConfiguration == null) {
            return null;
        }
        return new WindowConfiguration(this.mConfiguration.windowConfiguration);
    }
}
