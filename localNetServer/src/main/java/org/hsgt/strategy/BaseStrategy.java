package org.hsgt.strategy;
import lombok.Data;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;

import java.util.List;

@Data
public abstract class BaseStrategy implements Strategy {

    private int state;
    protected boolean saftyValidation(Competitor newCompetitor, float lowestPrice) {
        boolean safe = true;

        // 当用户购买大量的产品的时候，我们可能会亏钱
        if (newCompetitor.getPrice2() + newCompetitor.getShippingGroup().getExtraUnitCost() < lowestPrice) {
            this.setState(REJECTED_RISK_LOSS);
            safe = false;
        }

        // 肯定会亏钱 (假设 ShippingUnitCost >= ShippingExtraUnitCost)
        if (newCompetitor.getPrice2() + newCompetitor.getShippingGroup().getUnitCost() < lowestPrice ) {
            this.setState(REJECTED_LOSS);
            safe = false;
        }

        // 无效的价格
        if (newCompetitor.getPrice2() <= 0 || newCompetitor.getPrice1() <= 0 ) {
            this.setState(REJECTED_INVALID_PRICE);
            safe = false;
        }

        return safe;
    }

    public abstract Competitor execute(Competitor me, List<Competitor> competitorList, Offer offer);
}
