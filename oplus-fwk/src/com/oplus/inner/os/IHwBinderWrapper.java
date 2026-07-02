/*
 * SPDX-FileCopyrightText: 2026 AlphaDroid
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.inner.os;

public class IHwBinderWrapper {
    public static abstract class DeathRecipientWrapper {
        public DeathRecipientWrapper() {
        }

        public abstract void serviceDied(long cookie);
    }
}
