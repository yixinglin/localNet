package org.hsgt.controllers;

import io.swagger.annotations.Api;
import org.hsgt.controllers.response.ConfigureResponse;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.ConfigureMapper;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.SqlService;
import org.hsgt.strategy.Strategy;
import org.hsgt.strategy.TotalPriceStrategy;
import org.hsgt.strategy.UnitPriceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "Price Management" )
@RestController
@RequestMapping("/pricing/metro")
public class MetroPriceManagementController {
    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    OfferMapper offerMapper;

    @GetMapping("/conf")
    public List<ConfigureResponse> getConfiguration() {
        List<Configure> configureList = configureMapper.selectList(null);
        List<ConfigureResponse> configureResponses = new ArrayList<>();
        for (Configure c: configureList) {
            ConfigureResponse cr = new ConfigureResponse();
            cr.setEnabled(c.isEnabled());
            cr.setReduce(c.getStrategy().getReduce());
            cr.setMaxAdjust(c.getStrategy().getMaxAdjust());
            cr.setStrategyId(c.getStrategy().getId());
            cr.setProductId(c.getOffer().getId());
            cr.setLowestPrice(c.getOffer().getLowestPrice());
            cr.setOfferNote(c.getOffer().getNote());
            cr.setOfferAmount(c.getOffer().getAmount());
            cr.setProductName(c.getOffer().getProductName());
            configureResponses.add(cr);
        }
        return configureResponses;
    }

    @PostMapping("/conf")
    public ConfigureResponse updateConfiguration(@RequestBody ConfigureResponse payload) {
        Configure conf = new Configure();
        Strategy strategy;
        switch (payload.getStrategyId()) {
            case "TotalPriceStrategy":
                strategy = new TotalPriceStrategy(payload.getReduce(), payload.getMaxAdjust());
                break;
            case "UnitPriceStrategy":
                strategy = new UnitPriceStrategy(payload.getReduce(), payload.getMaxAdjust());
                break;
            default:
                throw new RuntimeException("Strategy Name Invalid!");
        }

        conf.setStrategy(strategy);
        conf.setEnabled(payload.isEnabled());

        Offer offer = offerMapper.selectById(payload.getProductId());
        if (offer == null) {
            throw new RuntimeException("Offer is null! [updateConfiguratinon]");
        }

        offer.setNote(payload.getOfferNote());
        offer.setLowestPrice(payload.getLowestPrice());
        offer.setAmount(payload.getOfferAmount());
        conf.setOffer(offer);
        SqlService.sqlInsertOrUpdate(conf, configureMapper);
        offerMapper.updateLowestPriceAndNote(offer);
        return payload;
    }
}
