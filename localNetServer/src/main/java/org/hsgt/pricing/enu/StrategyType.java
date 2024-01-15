package org.hsgt.pricing.enu;

public enum StrategyType {
    TotalPriceStrategy("TotalPriceStrategy"),
    UnitPriceStrategy("UnitPriceStrategy");
    private String val;
    StrategyType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}