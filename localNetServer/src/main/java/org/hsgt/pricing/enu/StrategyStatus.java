package org.hsgt.pricing.enu;

public enum StrategyStatus {

    APPROVED(0),       // 接受
    REJECTED_PRICE_FULFILLED(1),  // 价格已经满足调节
    REJECTED_LOSS(2), // 价格会造成亏损
    REJECTED_RISK_LOSS(3), // 价格会有可能造成亏损
    REJECTED_INVALID_PRICE(4), //  价格无效，例如价格低于0
    REJECTED_NO_COMPETITORS(5), //  没有竞争者
    REJECTED_LARGE_ADJUST(6);   // 调幅过大

    private int val;
    StrategyStatus(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
