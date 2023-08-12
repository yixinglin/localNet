package org.hsgt.strategy;

import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;

import java.util.Comparator;
import java.util.List;

@Data
public class UnitPriceStrategy extends Strategy {

    public static final String id = "UnitPriceStrategy";

    public UnitPriceStrategy() {

    }

    public UnitPriceStrategy(float reduce, float maxAdjust) {
        super(reduce, maxAdjust);
    }

    @Override
    public Competitor execute(Competitor self, List<Competitor> others, Offer offer) {
        Competitor newCompetitor = SerializationUtils.clone(self);
        // You have no competitors.
        if (others.isEmpty()) {
            newCompetitor = null;
            this.setState(REJECTED_NO_COMPETITORS);
            return newCompetitor;
        }

        // Sort list by unit prices.
        others.sort(Comparator.comparing(o -> o.getPrice2()));
        Competitor competitor = others.get(0);      // The seller with minimum unit price (exclude me).

        // If you are already No. 1
        if (competitor.getPrice2() > self.getPrice2()) {
            newCompetitor = null;
            this.setState(REJECTED_PRICE_FULFILLED);
            return newCompetitor;
        }

        // Reduce price values that are lower than that of the No.1 seller.
        newCompetitor.setPrice1(competitor.getPrice1() - this.reduce);
        newCompetitor.setPrice2(competitor.getPrice2() - this.reduce);

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
        return UnitPriceStrategy.id;
    }
}
