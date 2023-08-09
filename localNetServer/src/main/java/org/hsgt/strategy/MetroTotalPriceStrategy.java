package org.hsgt.strategy;
import org.apache.commons.lang3.SerializationUtils;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;

import java.util.Comparator;
import java.util.List;

// 商品最低总价策略
public class MetroTotalPriceStrategy extends BaseStrategy implements Strategy {


    private float maxAdjust;
    private float reduce;

    @Override
    public Competitor execute(Competitor me, List<Competitor> competitorList, Offer offer) {
        Competitor newCompetitor = SerializationUtils.clone(me);
        float lowestPrice = offer.getLowestPrice();

        if (competitorList.isEmpty()) {
            // 验证：没有竞争者
            newCompetitor = null;
            this.setState(REJECTED_NO_COMPETITORS);
            return newCompetitor;
        }


        if (me.getRank() == 0) {
            // 验证：已经是第一名了，不用跟单
            newCompetitor = null;
            this.setState(REJECTED_PRICE_FULFILLED);
            return newCompetitor;
        }

        // 比第一名单价低0.01元
        competitorList.sort(Comparator.comparing(o -> o.getPrice2() + o.getShippingGroup().getUnitCost()));
        Competitor competitor = competitorList.get(0); // The seller with minimum total price.

        newCompetitor.setPrice1(competitor.getPrice1() - this.reduce);
        newCompetitor.setPrice2(competitor.getPrice2() - this.reduce);

        if (!this.saftyValidation(newCompetitor, offer.getLowestPrice())) {
            newCompetitor = null;
            return newCompetitor;
        }

        // 验证：如果调幅过大，不跟单
        if (me.getPrice2() - newCompetitor.getPrice2() > this.maxAdjust) {
            newCompetitor = null;
            this.setState(REJECTED_LARGE_ADJUST);
            return newCompetitor;
        }

        return newCompetitor;
    }


}
