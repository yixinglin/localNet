package org.hsgt.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hsgt.api.SellerApi;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.ControllerResponse;
import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.services.OfferService;
import org.hsgt.services.ProductPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.utils.IoUtils;

import java.util.List;

@Api(tags = "Offer Management")
@RestController
@RequestMapping("/offer/metro")
public class MetroOfferController {

//    @Qualifier("MetroOfferService")
    @Autowired
    private OfferService offerService;
//    @Qualifier("MetroProductPageService")
    @Autowired()
    private ProductPageService productPageService;

    private SellerApi api;

    public MetroOfferController() {
        this.api = Global.getMetroApiInstance();
    }

    @ApiOperation(value = "Get offer data.", notes = "Get offer data from api. Data is up-to-date. " +
            "Note that the concrete shipping groups are just acquired from database. So they are not up-to-date.")
    @GetMapping("/selectAll")
    public ControllerResponse<List<Offer>> selectAll() {
        List<Offer> offers = offerService.queryById((List<String>) null);
        ControllerResponse<List<Offer>> resp = ControllerResponse.ok().setData(offers);
        return resp;
    }

    // Insert or update Competitor and ShippingGroup to database. Data is up-to-date.
    @ApiOperation(value = "productPage", notes = "Get product page data from API. Data is up-to-date. In this method, " +
            "the latest shipping group details are updated to the database.")
    @GetMapping("/productpage")
    public ControllerResponse<ProductPage> productPage(String productKey) {
        ProductPage productPage = productPageService.queryById(productKey);
        ControllerResponse resp = ControllerResponse.ok().setData(productPage);
        if(!Global.DEBUG) {
            IoUtils.delay(1000, 6000);
        }
        return resp;
    }

    // Get productPage list form database. Data may not up-to-date
    @ApiOperation(value = "productPageListFromDatabase",
            notes = "Get product page data from Database. Data may not be up-to-date")
    @PostMapping("/productpageList")
    public ControllerResponse<List<ProductPage>> productPageListFromDatabase(@RequestBody List<String> productIdList) {
        List<ProductPage> pages = productPageService.queryAll();
        ControllerResponse response = ControllerResponse.ok().setData(pages);
        return response;
    }
}
