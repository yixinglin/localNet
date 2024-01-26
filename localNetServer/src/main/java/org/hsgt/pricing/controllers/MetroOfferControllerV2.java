package org.hsgt.pricing.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hsgt.core.domain.ResponseResult;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.servicesV2.ICompetitionService;
import org.hsgt.pricing.servicesV2.IOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.utils.IoUtils;

import java.util.List;

@Api(tags = "Offer Management")
@RestController
@RequestMapping("/offer/metro")
public class MetroOfferControllerV2 {

    @Autowired
    private IOfferService offerService;
    @Autowired
    private ICompetitionService competitionService;
    @Autowired
    private MetroPricingConfig metroPricingConfig;

    @ApiOperation(value = "Get offer data.", notes = "Get offer data from api. Data is up-to-date. " +
            "Note that the concrete shipping groups are just acquired from database. So they are not up-to-date.")
    @GetMapping("/selectAll")
    public ResponseResult<List<Offer>> selectAll() {
        // LambdaQueryWrapper<OfferDO> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(OfferDO::getActive, true);
        List<Offer> offers = offerService.listDetails(null, metroPricingConfig.getFilterKeywords());
        ResponseResult<List<Offer>> resp = ResponseResult.success().setData(offers).setLength(offers.size());
        return resp;
    }

    // Get productPage list form database. Data may not up-to-date
    @ApiOperation(value = "productPageListFromDatabase",
            notes = "Get product page data from Database. Data may not be up-to-date")
    @PostMapping("/productpageList")
    public ResponseResult<List<ProductPage>> productPageListFromDatabase(@RequestBody List<String> productIdList) {
        List<ProductPage> pages = competitionService.listProductPageDetailsByIds(productIdList);
        ResponseResult response = ResponseResult.success().setData(pages).setLength(pages.size());
        return response;
    }


    // Insert or update Competitor and ShippingGroup to database. Data is up-to-date.
    @ApiOperation(value = "productPage", notes = "Get product page data from API. Data is up-to-date. In this method, " +
            "the latest shipping group details are updated to the database.")
    @GetMapping("/productpage")
    public ResponseResult<ProductPage> productPage(String productId) {
        boolean isMocked = metroPricingConfig.isMocked();
        ProductPage productPage = competitionService.getProductPageDetailsById(productId);
        ResponseResult resp = ResponseResult.success().setData(productPage);
        if(!isMocked) {
            IoUtils.delay(200, 1500);
        }
        return resp;
    }

}
