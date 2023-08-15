package org.hsgt.controllers;

import io.swagger.annotations.Api;
import org.hsgt.api.SellerApi;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.ConfigureResponse;
import org.hsgt.controllers.response.ControllerResponse;
import org.hsgt.controllers.response.SuggestedPrice;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.CompetitorMapper;
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
import java.util.stream.Collectors;

@Api(tags = "Price Management" )
@RestController
@RequestMapping("/pricing/metro")
public class MetroPriceManagementController {
    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    OfferMapper offerMapper;
    @Autowired
    CompetitorMapper competitorMapper;

    private final SellerApi api;
    public MetroPriceManagementController() {
        api = Global.getMetroApiInstance();
    }

    // Suggest price based on the data from database.
    @GetMapping("/suggest")
    public ControllerResponse<SuggestedPrice> suggestPriceToUpdate(String productId) {

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

        // Compute the expected price to update
        Competitor expected = strategy.execute(self, others, offer);
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
        ControllerResponse<SuggestedPrice> resp = ControllerResponse.ok().setData(suggestedPrice);
        return resp;
    }

    @GetMapping("/conf")
    public ControllerResponse<List<ConfigureResponse>> getConfiguration() {
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
        ControllerResponse<List<ConfigureResponse>> resp = ControllerResponse.ok().setData(configureResponses);
        return resp;
    }

    @PostMapping("/conf")
    public ControllerResponse<ConfigureResponse> updateConfiguration(@RequestBody ConfigureResponse payload) {
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
        ControllerResponse<ConfigureResponse> resp = ControllerResponse.ok().setData(payload);
        return resp;
    }
}
