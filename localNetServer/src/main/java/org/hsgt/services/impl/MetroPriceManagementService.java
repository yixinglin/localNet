package org.hsgt.services.impl;

import org.hsgt.api.SellerApi;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.SuggestedPrice;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
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
        return configureList;
    }

    public void updateConfiguration(Configure conf) {
        Offer offer = offerMapper.selectById(conf.getOffer());   // Offer from database
        if (offer == null) {
            Logger.loggerBuilder(getClass()).error("Offer is null! [updateConfiguratinon]");
            throw new RuntimeException("Offer is null! [updateConfiguratinon]");
        }

        // Update t_configure
        configureMapper.updateById(conf);
    }

    public SuggestedPrice suggestPriceUpdate(String productId) {
        Offer offer = new Offer();
        offer.setId(productId);
        Configure configure = new Configure();
        configure.setOffer(offer);
        configure = configureMapper.selectById(productId);
        offer = configure.getOffer();

        Strategy strategy = configure.getStrategy();
        List<Competitor> competitors = competitorMapper.findAllCompetitorByProductId(productId);
        Competitor self = competitors.stream()
                .filter(c -> c.getShopName().equals(this.api.accountName()))
                .collect(Collectors.toList()).get(0);
        List<Competitor> others = competitors.stream()
                .filter(c -> !c.getShopName().equals(this.api.accountName()))
                .collect(Collectors.toList());

        // Compute the expected price to update by comparing my price with other sellers' prices.
        Competitor expected = strategy.execute(self, others, offer);
        SuggestedPrice suggestedPrice = SuggestedPrice.build(strategy, offer, self, expected);
        return suggestedPrice;
    }

}
