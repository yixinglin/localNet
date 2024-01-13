package org.hsgt.pricing.services.impl;

import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.controllers.response.SuggestedPrice;
import org.hsgt.pricing.domain.pricing.Competitor;
import org.hsgt.pricing.domain.pricing.Configure;
import org.hsgt.pricing.domain.Offer;
import org.hsgt.pricing.domain.pricing.UpdatedOffer;
import org.hsgt.pricing.mapper.CompetitorMapper;
import org.hsgt.pricing.mapper.ConfigureMapper;
import org.hsgt.pricing.mapper.OfferMapper;
import org.hsgt.pricing.services.PriceManagementService;
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
public class MetroPriceManagementService implements PriceManagementService {

    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    OfferMapper offerMapper;
    @Autowired
    CompetitorMapper competitorMapper;

    @Autowired
    private MetroPricingConfig pricingConfig;
    @Autowired
    private SellerApi metroOfferSellerApi;

    Logger logger = Logger.loggerBuilder(MetroPriceManagementService.class);

    public MetroPriceManagementService() {
    }

    public List<Configure> queryAllConfigurations() {
        List<Configure> configureList = configureMapper.selectList(null);
        configureList = configureList.stream().filter(c -> c.getOffer().getActive()).collect(Collectors.toList());
        return configureList;
    }

    public void updateConfiguration(List<Configure> conf) {
        for (Configure c: conf) {
            Offer offer = offerMapper.selectById(c.getOffer());   // Offer from database
            if (offer == null) {
                Logger.loggerBuilder(getClass()).error("Offer is null! [updateConfiguratinon]");
                // throw new RuntimeException("Offer is null! [updateConfiguratinon]");
                continue;
            }
            // Update t_configure
            configureMapper.updateById(c);
        }

    }

    /*
     * @param productId:
      * @return SuggestedPrice
     * @author Lin
     * @description TODO Suggest price based on the data from database.
     * @date 25.Oct.2023 025 19:08
     */
    public SuggestedPrice suggestPriceUpdate(String productId) {
        SellerApi api = metroOfferSellerApi;
        Offer offer = new Offer();
        offer.setId(productId);
        Configure configure = new Configure();
        configure.setOffer(offer);
        configure = configureMapper.selectById(productId);
        try {
            offer = configure.getOffer();
        } catch (NullPointerException e) {
            throw new NullPointerException("No offer was found. " + productId);
        }

        Strategy strategy = configure.getStrategy();
        List<Competitor> competitors = competitorMapper.findAllCompetitorByProductId(productId);
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
            switch (strategy.getId()) {
                case "UnitPriceStrategy":
                    diff = top.getPrice2() - offer.getPrice();
                    break;
                case "TotalPriceStrategy":
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
    public NewOffer pricing(NewOffer newOffer, JSONArray offerList, String ip) {
        boolean allowActualPricing = pricingConfig.isAllowActualPricing();
        boolean isAllowActualPricing = pricingConfig.isAllowActualPricing();
        SellerApi api = metroOfferSellerApi;
        String shippingGroupId = newOffer.getShippingGroupId();

        if (offerList == null) {
            String s = api.selectAllOffers().getContent();
            offerList = (new JSONObject(s)).getJSONArray("items");
        }

        // Update price via API
        HttpResponse response;
        if (allowActualPricing) {
            this.logger.info("Actual Pricing: " + newOffer);
            response = api.updateOfferById(newOffer, offerList, true);
        } else {
            this.logger.info("@@ Virtual Pricing: " + newOffer);
            response = api.updateOfferById(newOffer, offerList, false);
        }
        if (response.getStateCode() != 200) {
            throw new RuntimeException("Fail to update offer. " + newOffer.toString() + response.toString());
        }
        // Add pricing log to database
        UpdatedOffer updatedOffer = new UpdatedOffer();
        updatedOffer.setIp(ip);
        updatedOffer.setOffer(newOffer);
        String username;
        try {
            username = JwtsUtils.verify(newOffer.getToken()).getSubject();
            updatedOffer.setUsername(username);
        } catch (Exception e) {
            updatedOffer.setUsername(ip);
        }
        if (isAllowActualPricing)
            updatedOffer.setNote("Actual");
        else
            updatedOffer.setNote("Virtual");
        offerMapper.insertUpdatedPricingLog(updatedOffer);
        return newOffer;
    }

}
