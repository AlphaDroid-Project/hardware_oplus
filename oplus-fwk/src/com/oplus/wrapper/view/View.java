/*
 * SPDX-FileCopyrightText: 2026 AlphaDroid
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.view;

public class View {
    private final android.view.View mView;

    public View(android.view.View view) {
        this.mView = view;
    }

    public ViewRootImpl getViewRootImpl() {
        android.view.ViewRootImpl viewRootImpl = this.mView.getViewRootImpl();
        if (viewRootImpl == null) {
            return null;
        }
        return new ViewRootImpl(viewRootImpl);
    }
}
