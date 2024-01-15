package org.hsgt.pricing.controllers.response;

import lombok.Data;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.strategy.Strategy;
import org.hsgt.pricing.strategy.TotalPriceStrategy;
import org.hsgt.pricing.strategy.UnitPriceStrategy;
import org.utils.Logger;

@Data
public class ConfigureResponse {
    String productId;
    String productName;
    float lowestPrice;
    String offerNote;
    int offerAmount;
    boolean enabled;
    String strategyId;
    float maxAdjust;
    float reduce;

    public static ConfigureResponse build(Configure conf) {
        ConfigureResponse ans = new ConfigureResponse();
        ans.setEnabled(conf.isEnabled());
        ans.setReduce(conf.getStrategy().getReduce());
        ans.setMaxAdjust(conf.getStrategy().getMaxAdjust());
        ans.setStrategyId(conf.getStrategy().getId());
        ans.setProductId(conf.getOffer().getId());
        ans.setLowestPrice(conf.getOffer().getLowestPrice());
        ans.setOfferNote(conf.getOffer().getNote());
        ans.setOfferAmount(conf.getOffer().getAmount());
        ans.setProductName(conf.getOffer().getProductName());
        return ans;
    }

    public static Configure build(ConfigureResponse response) {
        Configure conf = new Configure();
        Strategy strategy;
        switch (response.getStrategyId()) {
            case "TotalPriceStrategy":
                strategy = new TotalPriceStrategy(response.getReduce(), response.getMaxAdjust());
                break;
            case "UnitPriceStrategy":
                strategy = new UnitPriceStrategy(response.getReduce(), response.getMaxAdjust());
                break;
            default:
                Logger.loggerBuilder(ConfigureResponse.class).error("Strategy Name Invalid!");
                throw new RuntimeException("Strategy Name Invalid!");
        }

        conf.setStrategy(strategy);
        conf.setEnabled(response.isEnabled());

        Offer offer = new Offer();
        offer.setId(response.getProductId());
        offer.setNote(response.getOfferNote());
        offer.setLowestPrice(response.getLowestPrice());
        offer.setAmount(response.getOfferAmount());
        conf.setOffer(offer);
        return conf;
    }
}
