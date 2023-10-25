package org.hsgt.services.impl;

import org.hsgt.api.SellerApi;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.NewOffer;
import org.hsgt.controllers.response.SuggestedPrice;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.entities.pricing.UpdatedOffer;
import org.hsgt.mappers.CompetitorMapper;
import org.hsgt.mappers.ConfigureMapper;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.services.PriceManagementService;
import org.hsgt.strategy.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private SellerApi api;

    public MetroPriceManagementService() {
        this.api  = Global.getMetroApiInstance();
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
                    .filter(c -> c.getShopName().equals(this.api.accountName()))
                    .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Your are not in the competitor list! " + offer.getId());
        }

        List<Competitor> others = competitors.stream()
                .filter(c -> !c.getShopName().equals(this.api.accountName()))
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
    public NewOffer pricing(NewOffer newOffer, String ip) {
        float price = newOffer.getPrice();
        int quantity = newOffer.getQuantity();
        String shippingGroupId = newOffer.getShippingGroupId();
        shippingGroupId = shippingGroupId.equals("0") ? null: shippingGroupId;
        // Update price via API
        if (Global.allowActualPricing) {
            System.out.println("Actual Pricing: " + newOffer);
        } else {
            System.out.println("Virtual Pricing: " + newOffer);
        }

        // Add pricing log to database
        UpdatedOffer updatedOffer = new UpdatedOffer();
        updatedOffer.setIp(ip);
        updatedOffer.setOffer(newOffer);
        if (Global.DEBUG)
            updatedOffer.setNote("Staging");
        else
            updatedOffer.setNote("Production");
        offerMapper.insertUpdatedPricingLog(updatedOffer);
        return newOffer;
    }

}
