package org.hsgt.order.enu;

public enum AddressType {
    Billing(0), Shipping(1);
    private int val;
    AddressType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
