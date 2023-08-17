package org.hsgt.controllers.response;

import lombok.Data;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.strategy.Strategy;

@Data
public class SuggestedPrice {
    String productId;
    float price;    // Suggested price
    float reduced; // Magnitude that price reduced
    int status;    // Status after applying Price suggestion strategy
    String strategy;

    public static SuggestedPrice build(Strategy strategy, Offer offer, Competitor self, Competitor expected) {
        SuggestedPrice suggestedPrice = new SuggestedPrice();
        suggestedPrice.setStatus(strategy.getState());
        suggestedPrice.setProductId(offer.getId());
        suggestedPrice.setStrategy(strategy.getId());
        if (expected != null) {
            suggestedPrice.setPrice(expected.getPrice2());
            suggestedPrice.setReduced(expected.getPrice2()-offer.getPrice());
        } else {
            suggestedPrice.setPrice(self.getPrice2());
            suggestedPrice.setReduced(0);
        }
        return suggestedPrice;
    }

//    public static SuggestedPrice fromStrategy(Strategy strategy) {
//    }
}
