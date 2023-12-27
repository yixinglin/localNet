package org.hsgt.pricing.strategy;

import lombok.Data;
import org.hsgt.pricing.domain.pricing.Competitor;
import org.hsgt.pricing.domain.pricing.Configure;

import java.util.List;

@Data
public abstract class Strategy {

    public static final float POSITIVE_INFINITY = 99999;
    public static final int APPROVED = 0;       // 接受
    public static final int REJECTED_PRICE_FULFILLED = 1;  // 价格已经满足调节
    public static final int REJECTED_LOSS = 2; // 价格会造成亏损
    public static final int REJECTED_RISK_LOSS = 3; // 价格会有可能造成亏损
    public static final int REJECTED_INVALID_PRICE = 4; //  价格无效，例如价格低于0
    public static final int REJECTED_NO_COMPETITORS = 5; //  没有竞争者
    public static final int REJECTED_LARGE_ADJUST = 6;   // 调幅过大

    protected int state;
    protected float maxAdjust;
    protected float reduce;

    public Strategy() {

    }

    public Strategy(float reduce, float maxAdjust) {
        this.maxAdjust = maxAdjust;
        this.reduce = reduce;
    }

    /**
     * @param self:
     * @param competitorList:
     * @param configure:
     * @return Competitor
     * @author Lin
     * @description TODO Please fetching data from the target product page via API before performing this function!
     * @date 25.Oct.2023 025 16:59
     */
    public abstract Competitor execute(Competitor self, List<Competitor> competitorList, Configure configure);

    public abstract List<Competitor> sort(List<Competitor> sellers);
    protected boolean saftyValidation(Competitor newCompetitor, float lowestPrice) {
        boolean safe = true;

        // May lead to profit loss when customer buy a large number of products.
        if (newCompetitor.getPrice2() + newCompetitor.getShippingGroup().getExtraUnitCost() < lowestPrice) {
            this.setState(REJECTED_RISK_LOSS);
            safe = false;
        }

        // Profit loss (when ShippingUnitCost >= ShippingExtraUnitCost)
        if (newCompetitor.getPrice2() + newCompetitor.getShippingGroup().getUnitCost() < lowestPrice ) {
            this.setState(REJECTED_LOSS);
            safe = false;
        }

        // Price invalid
        if (newCompetitor.getPrice2() <= 0 || newCompetitor.getPrice1() <= 0 ) {
            this.setState(REJECTED_INVALID_PRICE);
            safe = false;
        }

        return safe;
    }

    public abstract String getId();
}
