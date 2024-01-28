package org.hsgt.order.enu;

public enum OrderStatus {
    PLACED(0),
    CONFIRMED(1),
    SHIPPED(2),
    RETURN_ACCEPTED(3),
    RETURN_DECLINED(4),
    RETURN_REQUESTED(5),
    CANCELED(6);

    private int val;
    OrderStatus(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
