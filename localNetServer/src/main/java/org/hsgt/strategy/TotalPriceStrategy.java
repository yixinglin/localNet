package org.hsgt.strategy;
import org.apache.commons.lang3.SerializationUtils;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;

import java.util.Comparator;
import java.util.List;

public class TotalPriceStrategy extends Strategy {
    public static final String id = "TotalPriceStrategy";

    public TotalPriceStrategy() {

    }

    public TotalPriceStrategy(float reduce, float maxAdjust) {
        super(reduce, maxAdjust);
    }

    @Override
    public Competitor execute(Competitor self, List<Competitor> competitorList, Offer offer) {
        Competitor newCompetitor = SerializationUtils.clone(self);
        // You have no competitors.
        if (competitorList.isEmpty()) {
            newCompetitor = null;
            this.setState(REJECTED_NO_COMPETITORS);
            return newCompetitor;
        }

        // Sort list by total prices.
        competitorList.sort(Comparator.comparing(o -> o.getPrice2() + o.getShippingGroup().getUnitCost()));
        Competitor competitor = competitorList.get(0); // The seller with minimum total price.

        // If you are already No. 1
        if (competitor.getShopName().equals(self.getShopName())) {
            newCompetitor = null;
            this.setState(REJECTED_PRICE_FULFILLED);
            return newCompetitor;
        }

        // Reduce price values that are lower than that of the No.1 seller.
        newCompetitor.setPrice1(competitor.getPrice1() - this.reduce);
        newCompetitor.setPrice2(competitor.getPrice2() - this.reduce);

        // Validate the new price.
        if (!this.saftyValidation(newCompetitor, offer.getLowestPrice())) {
            newCompetitor = null;
            return newCompetitor;
        }

        // Price reduction is too large, which are not safe.
        if (self.getPrice2() - newCompetitor.getPrice2() > this.maxAdjust) {
            newCompetitor = null;
            this.setState(REJECTED_LARGE_ADJUST);
            return newCompetitor;
        }
        return newCompetitor;
    }

    @Override
    public String getId() {
        return TotalPriceStrategy.id;
    }


}
