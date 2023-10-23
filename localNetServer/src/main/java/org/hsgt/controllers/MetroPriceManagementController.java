package org.hsgt.controllers;

import io.swagger.annotations.Api;
import org.hsgt.api.SellerApi;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.ConfigureResponse;
import org.hsgt.controllers.response.ControllerResponse;
import org.hsgt.controllers.response.SuggestedPrice;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.CompetitorMapper;
import org.hsgt.mappers.ConfigureMapper;
import org.hsgt.services.OfferService;
import org.hsgt.services.PriceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Price Management" )
@RestController
@RequestMapping("/pricing/metro")
public class MetroPriceManagementController {

    @Autowired
    PriceManagementService priceManagementService;

    @Qualifier("metroOfferService")
    @Autowired
    OfferService offerService;

    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    CompetitorMapper competitorMapper;

    private final SellerApi api;
    public MetroPriceManagementController() {
        api = Global.getMetroApiInstance();
    }

    // Suggest price based on the data from database.
    @GetMapping("/suggest")
    public ControllerResponse<SuggestedPrice> suggestPriceToUpdate(String productId) {
        SuggestedPrice suggestedPrice = priceManagementService.suggestPriceUpdate(productId);
        ControllerResponse<SuggestedPrice> resp = ControllerResponse.ok().setData(suggestedPrice);
        return resp;
    }

    @GetMapping("/conf")
    public ControllerResponse<List<ConfigureResponse>> getConfiguration() {
        List<Configure> configureList = priceManagementService.queryAllConfigurations();
        List<ConfigureResponse> configureResponses = configureList.stream().map(o -> ConfigureResponse.build(o)).collect(Collectors.toList());
        ControllerResponse<List<ConfigureResponse>> resp = ControllerResponse.ok().setData(configureResponses);
        return resp;
    }

    @PostMapping("/conf")
    public ControllerResponse updateConfiguration(@RequestBody List<ConfigureResponse> payload) {
        List<Configure> conf = payload.stream().map(o -> ConfigureResponse.build(o)).collect(Collectors.toList());
        // Update t_configure
        priceManagementService.updateConfiguration(conf);
        // Update t_offer
        for (Configure c: conf) {
            offerService.updateLowestPriceAndNote(c.getOffer());
        }
        ControllerResponse resp = ControllerResponse.ok();
        return resp;
    }

    @PostMapping("/edit")
    public ControllerResponse pricing(@RequestBody Offer offer) {
        Offer resp = priceManagementService.pricing(offer);
        ControllerResponse<Offer> cr = ControllerResponse.ok().setData(resp);
        return cr;
    }
}
