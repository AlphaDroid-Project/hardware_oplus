package com.oplus.wrapper.util;

public class StatsEvent {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        public Builder setAtomId(int atomId) {
            return this;
        }

        public Builder usePooledBuffer() {
            return this;
        }

        public Builder writeInt(int value) {
            return this;
        }

        public Builder writeLong(long value) {
            return this;
        }

        public Builder writeBoolean(boolean value) {
            return this;
        }

        public StatsEvent build() {
            return new StatsEvent();
        }
    }
}
