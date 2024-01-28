package org.hsgt.pricing.services.impl;

import org.hsgt.pricing.BO.*;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.controllers.response.SuggestedPrice;
import org.hsgt.pricing.domain.PricingHistoryDO;
import org.hsgt.pricing.enu.StrategyType;
import org.hsgt.pricing.mapper.PricingHistoryMapperMP;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.services.ICompetitionService;
import org.hsgt.pricing.services.IPriceManagementService;
import org.hsgt.pricing.services.IPricingConfigureService;
import org.hsgt.pricing.strategy.Strategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.JwtsUtils;
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
    @Autowired
    private MetroPricingConfig pricingConfig;
    @Autowired
    private SellerApi metroOfferSellerApi;
    @Autowired
    private PricingHistoryMapperMP pricingHistoryMapperMP;

    Logger logger = Logger.loggerBuilder(PriceManagementServiceImpl.class);

    @Override
    public SuggestedPrice  suggestPriceUpdate(String productId) {
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

    // Todo 测试
    @Override
    public NewOffer pricing(NewOffer latestOffer, JSONArray offerList, String ip) {
        boolean allowActualPricing = pricingConfig.isAllowActualPricing();
        boolean isAllowActualPricing = pricingConfig.isAllowActualPricing();
        SellerApi api = metroOfferSellerApi;
        String shippingGroupId = latestOffer.getShippingGroupId();

        if (offerList == null) {
            String s = api.selectAllOffers().getContent();
            offerList = (new JSONObject(s)).getJSONArray("items");
        }

        // Update price via API
        HttpResponse response;
        if (allowActualPricing) {
            this.logger.info("Actual Pricing: " + latestOffer);
            response = api.updateOfferById(latestOffer, offerList, true);
        } else {
            this.logger.info("@@ Virtual Pricing: " + latestOffer);
            response = api.updateOfferById(latestOffer, offerList, false);
        }
        if (response.getStateCode() != 200) {
            throw new RuntimeException("Fail to update offer. " + latestOffer.toString() + response.toString());
        }

        // Add pricing log to database
        PricingHistoryDO pricingHistoryDo = new PricingHistoryDO();
        pricingHistoryDo.setPrice(latestOffer.getPrice());
        pricingHistoryDo.setIp(ip);
        pricingHistoryDo.setQuantity(latestOffer.getQuantity());
        pricingHistoryDo.setShippingGroupId(latestOffer.getShippingGroupId());

        String username;
        try {
            username = JwtsUtils.verify(latestOffer.getToken()).getSubject();
            pricingHistoryDo.setUsername(username);
        } catch (Exception e) {
            pricingHistoryDo.setUsername(ip);
        }
        if (isAllowActualPricing)
            pricingHistoryDo.setNote("Actual");
        else
            pricingHistoryDo.setNote("Virtual");

        pricingHistoryMapperMP.insert(pricingHistoryDo);
        return latestOffer;
    }
}
