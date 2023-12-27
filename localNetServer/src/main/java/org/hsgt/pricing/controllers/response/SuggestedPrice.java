package org.hsgt.pricing.controllers.response;

import lombok.Data;
import org.hsgt.pricing.domain.pricing.Competitor;
import org.hsgt.pricing.domain.Offer;
import org.hsgt.pricing.strategy.Strategy;

@Data
public class SuggestedPrice {
    String productId;
    float price;    // Suggested price
    float reduced; // Magnitude that price reduced
    float diff;     // Different to the first seller
    int status;    // Status after applying Price suggestion strategy
    String url;
    String strategy;

    public static SuggestedPrice build(Strategy strategy, Offer offer, Competitor self, Competitor expected) {
        SuggestedPrice suggestedPrice = new SuggestedPrice();
        suggestedPrice.setStatus(strategy.getState());
        suggestedPrice.setProductId(offer.getId());
        suggestedPrice.setStrategy(strategy.getId());
        suggestedPrice.setUrl("https://www.metro.de/marktplatz/product/" + offer.getProductKey());
        if (expected != null) {
            suggestedPrice.setPrice(expected.getPrice2());
            suggestedPrice.setReduced(expected.getPrice2()-offer.getPrice());
        } else {
            suggestedPrice.setPrice(self.getPrice2());
            suggestedPrice.setReduced(0);
        }
        return suggestedPrice;
    }

}
