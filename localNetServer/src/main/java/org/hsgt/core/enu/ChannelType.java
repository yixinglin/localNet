package org.hsgt.core.enu;

public enum ChannelType {
    METRO(0), METRO_MOCKED(1), KAUFLAND(2),
    GLS(3), GLS_TEST(4);

    private int val;

    ChannelType(int i) {
        this.val = i;
    }

    public int getVal() {
        return val;
    }
}
