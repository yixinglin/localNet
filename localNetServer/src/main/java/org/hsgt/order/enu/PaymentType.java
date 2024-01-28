package org.hsgt.order.enu;

public enum PaymentType {
    CreditCard(0), Cash(1), Other(2),
    Paypal(3), ApplePay(4), GooglePay(5),
    AliPay(6), WeChatPay(7);

    private int val;
    PaymentType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

}
