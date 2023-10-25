package org.hsgt.controllers;

import io.swagger.annotations.Api;
import org.hsgt.api.SellerApi;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.ConfigureResponse;
import org.hsgt.controllers.response.ControllerResponse;
import org.hsgt.controllers.response.NewOffer;
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

import javax.servlet.http.HttpServletRequest;
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

    private List<String> filterKeywords = Global.pricing_filterKeywords;
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
        configureList = configureList.stream().filter(c -> !excluded(c)).collect(Collectors.toList());
        List<ConfigureResponse> configureResponses = configureList.stream().map(o -> ConfigureResponse.build(o)).collect(Collectors.toList());
        ControllerResponse<List<ConfigureResponse>> resp = ControllerResponse.ok().setData(configureResponses).setLength(configureResponses.size());
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
        ControllerResponse resp = ControllerResponse.ok().setLength(conf.size());
        return resp;
    }

    @PostMapping("/edit")
    public ControllerResponse pricing(@RequestBody NewOffer newOffer, HttpServletRequest httpRequest) {
        String ip = httpRequest != null? httpRequest.getRemoteAddr(): "127.0.0.1";
        NewOffer resp = priceManagementService.pricing(newOffer, ip);
        ControllerResponse<Offer> cr = ControllerResponse.ok().setData(resp);
        return cr;
    }

    public boolean excluded(Configure configure) {
        return filterKeywords.stream().filter(s -> configure.getOffer().getProductName().toLowerCase().contains(s)).findFirst().isPresent();
    }
}
