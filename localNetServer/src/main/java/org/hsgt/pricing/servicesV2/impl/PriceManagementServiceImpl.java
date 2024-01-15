package org.hsgt.pricing.servicesV2.impl;

import org.hsgt.pricing.BO.Competitor;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.controllers.response.SuggestedPrice;
import org.hsgt.pricing.enu.StrategyType;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.servicesV2.ICompetitionService;
import org.hsgt.pricing.servicesV2.IPriceManagementService;
import org.hsgt.pricing.servicesV2.IPricingConfigureService;
import org.hsgt.pricing.strategy.Strategy;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceManagementServiceImpl implements IPriceManagementService {

    @Autowired
    SellerApi metroSellerApi;
    @Autowired
    ICompetitionService competitionService;
    @Autowired
    IPricingConfigureService configureService;

    Logger logger = Logger.loggerBuilder(PriceManagementServiceImpl.class);

    @Override
    public SuggestedPrice suggestPriceUpdate(String productId) {
        SellerApi api = metroSellerApi;
        Configure configure = configureService.getDetailsById(productId);
        Offer offer = configure.getOffer();
        if (offer == null) {
            throw new NullPointerException("No offer was found. " + productId);
        }

        Strategy strategy = configure.getStrategy();
        ProductPage page = competitionService.getProductPageDetailsById(offer.getId());
        List<Competitor> competitors = page.getCompetitors();
        Competitor self;
        try {
            self = competitors.stream()
                    .filter(c -> c.getShopName().equals(api.accountName()))
                    .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Your are not in the competitor list! " + offer.getId());
        }

        List<Competitor> others = competitors.stream()
                .filter(c -> !c.getShopName().equals(api.accountName()))
                .collect(Collectors.toList());

        // Compute the expected price to update by comparing my price with other sellers' prices.
        Competitor expected = strategy.execute(self, others, configure);
        SuggestedPrice suggestedPrice = SuggestedPrice.build(strategy, offer, self, expected);
        float diff = 0;
        if(others.size() > 0) {
            Competitor top = others.get(0);
            switch (StrategyType.valueOf(strategy.getId())) {
                case UnitPriceStrategy:
                    diff = top.getPrice2() - offer.getPrice();
                    break;
                case TotalPriceStrategy:
                    diff = (top.getPrice2() + top.getShippingGroup().getUnitCost()) - (offer.getPrice() + offer.getShippingGroup().getUnitCost());
                    break;
                default:
                    diff = 0.01f;
            }
            if (!top.getShopName().equals(self.getShopName())) {
                diff -= 0.01f;
            }
        }
        suggestedPrice.setDiff(diff);

        return suggestedPrice;
    }

    @Override
    public NewOffer pricing(NewOffer latestOffer, JSONArray offerList, String ip) {
        return null;
    }
}
