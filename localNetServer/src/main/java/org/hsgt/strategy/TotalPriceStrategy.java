package org.hsgt.strategy;

import org.apache.commons.lang3.SerializationUtils;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Configure;

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
    public Competitor execute(Competitor self, List<Competitor> others, Configure configure) {
        float lowestPrice = configure.getOffer().getLowestPrice();
        Competitor newCompetitor = SerializationUtils.clone(self);
        // You have no competitors.
        if (others.isEmpty()) {
            newCompetitor = null;
            this.setState(REJECTED_NO_COMPETITORS);
            return newCompetitor;
        }

        // Sort list by total prices.
        others.sort(Comparator.comparing(o -> o.getPrice2() + o.getShippingGroup().getUnitCost()));
        Competitor competitor = others.get(0); // The seller with minimum total price (exclude me).

        // If you are already No. 1
        if (competitor.getPrice2() + competitor.getShippingGroup().getUnitCost() >
            self.getPrice2() + self.getShippingGroup().getUnitCost()) {
            newCompetitor = null;
            this.setState(REJECTED_PRICE_FULFILLED);
            return newCompetitor;
        }

        // Reduce price values lower than that of the No.1 seller.
        float newUnitPrice = competitor.getPrice2() + competitor.getShippingGroup().getUnitCost()
                - self.getShippingGroup().getUnitCost() - this.reduce;
        newCompetitor.setPrice1(newUnitPrice);
        newCompetitor.setPrice2(newUnitPrice);

        // Validate the new price.
        if (!this.saftyValidation(newCompetitor, lowestPrice)) {
            newCompetitor = null;
            return newCompetitor;
        }

        // Large price reduction is seemed as an unsafe operation.
        if (self.getPrice2() - newCompetitor.getPrice2() > this.maxAdjust) {
            newCompetitor = null;
            this.setState(REJECTED_LARGE_ADJUST);
            return newCompetitor;
        }
        return newCompetitor;
    }

    @Override
    public List<Competitor> sort(List<Competitor> sellers) {
        sellers.sort(Comparator.comparing(o -> o.getPrice2() + o.getShippingGroup().getUnitCost()));
        return sellers;
    }

    @Override
    public String getId() {
        return TotalPriceStrategy.id;
    }


}
