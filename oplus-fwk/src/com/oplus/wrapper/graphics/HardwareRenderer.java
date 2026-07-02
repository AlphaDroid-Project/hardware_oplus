/*
 * SPDX-FileCopyrightText: 2026 AlphaDroid
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.graphics;

public class HardwareRenderer {

    public interface FrameDrawingCallback {
        void onFrameDraw(long frame);
    }
}
