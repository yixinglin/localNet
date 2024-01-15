package org.hsgt.pricing.controllers;

import org.hsgt.core.domain.ResponseResult;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.controllers.response.ConfigureResponse;
import org.hsgt.pricing.controllers.response.SuggestedPrice;
import org.hsgt.pricing.domain.OfferDO;
import org.hsgt.pricing.servicesV2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RestController
@RequestMapping("/pricing/metro")
public class MetroPriceManagementControllerV2 {

    @Autowired
    IOfferService offerService;
    @Autowired
    IPricingConfigureService configureService;
    @Autowired
    ICompetitionService competitionService;
    @Autowired
    IShippingGroupService shippingGroupService;
    @Autowired
    IPriceManagementService priceManagementService;

    @Autowired
    private MetroPricingConfig pricingConfig;

    @GetMapping("/suggest")
    public ResponseResult<SuggestedPrice> suggestPriceToUpdate(String productId) {
        SuggestedPrice suggestedPrice = priceManagementService.suggestPriceUpdate(productId);
        ResponseResult<SuggestedPrice> resp = ResponseResult.success().setData(suggestedPrice);
        return resp;
    }

    @GetMapping("/conf")
    public ResponseResult<List<ConfigureResponse>> getConfiguration() {
        List<Configure> configureList = configureService.listDetails(null);
        configureList = configureList.stream().filter(c -> !excluded(c)).collect(Collectors.toList());
        List<ConfigureResponse> configureResponses = configureList.stream().map(o -> ConfigureResponse.build(o)).collect(Collectors.toList());

        ResponseResult<List<ConfigureResponse>> resp =
                ResponseResult.success().setData(configureResponses)
                        .setLength(configureResponses.size());
        return resp;
    }

    @PostMapping("/conf")
    public ResponseResult updateConfiguration(@RequestBody List<ConfigureResponse> payload) {
        List<Configure> conf = payload.stream().map(o -> ConfigureResponse.build(o)).collect(Collectors.toList());
        // Update t_configure
        configureService.updateListConfigureById(conf);
        // Update t_offer
        List<OfferDO> offers = conf.stream()
                .map(c ->  convertOfferToDO_LowestPrice_Note(c.getOffer())).collect(Collectors.toList());
        offerService.updateBatchById(offers);
        ResponseResult resp = ResponseResult.success().setLength(conf.size());
        return resp;
    }

    private boolean excluded(Configure configure) {
        List<String> filterKeywords = pricingConfig.getFilterKeywords();
        return filterKeywords.stream().filter(s -> configure.getOffer().getProductName().toLowerCase().contains(s)).findFirst().isPresent();

    }

    private OfferDO convertOfferToDO_LowestPrice_Note(Offer offer) {
        OfferDO offerDO = new OfferDO();
        offerDO.setProductId(offer.getId());
        offerDO.setLowestPrice(offer.getLowestPrice());
        offerDO.setAmount(offer.getAmount());
        offerDO.setNote(offer.getNote());
        return offerDO;
    }


}
