package org.hsgt.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hsgt.api.SellerApi;
import org.hsgt.builders.metro.MetroOfferBuilder;
import org.hsgt.builders.metro.MetroProductPageBuilder;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.ControllerResponse;
import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.*;
import org.hsgt.strategy.TotalPriceStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.utils.IoUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Offer Management")
@RestController
@RequestMapping("/offer/metro")
public class MetroOfferController {

    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private ShippingGroupMapper shippingGroupMapper;
    @Autowired
    private CompetitorMapper competitorMapper;
    @Autowired
    private ConfigureMapper configureMapper;
    private SellerApi api;

    public MetroOfferController() {
        this.api = Global.getMetroApiInstance();
    }

    @ApiOperation(value = "Get offer data.", notes = "Get offer data from api. Data is up-to-date")
    @GetMapping("/selectAll")
    public ControllerResponse<List<Offer>> selectAll() {
        // Offer data from API to database
        String s = this.api.selectAllOffers().getContent();
        MetroOfferBuilder builder = new MetroOfferBuilder();
        JSONArray jOfferList = new JSONObject(s).getJSONArray("items");
        List<Offer> offers =  new ArrayList<>();
        for (int i = 0; i < jOfferList.length(); i++) {
            // Convert JSON to Offer object
            Offer o = builder.offer(jOfferList.getJSONObject(i)).build();
            // Update offer to database
            this.offerToDataBase(o);
            o.setShippingGroup(null);
            offers.add(o);
        }

        // Offer data from database
        offers = this.offerMapper.selectList(null);
        // Sort
        offers = offers.stream()
                .sorted(Comparator.comparing(Offer::getNote).thenComparing(Offer::getPrice))
                .collect(Collectors.toList());

        ControllerResponse<List<Offer>> resp = ControllerResponse.ok().setData(offers);
        return resp;
    }

    // Insert offer to database, including Offer, Configuration, ShippingGroup ID
    private void offerToDataBase(Offer offer) {
        // Insert to t_shippinggroup
        ShippingGroup sg = offer.getShippingGroup();
        sg.setOwner(this.api.accountName());
        SqlService.sqlInsertOrSkip(sg, shippingGroupMapper);
        // Update or insert to t_offer
        offer.setLowestPrice(offer.getPrice() * 0.8f);
        SqlService.sqlInsertOrUpdate(offer, offerMapper);
        // Insert to t_configure
        Configure configure = new Configure();
        configure.setOffer(offer);
        configure.setStrategy(new TotalPriceStrategy(0.01f, 0.5f));
        configure.setEnabled(false);
        SqlService.sqlInsertOrSkip(configure, configureMapper);
    }

    // Insert or update Competitor and ShippingGroup to database. Data is up-to-date.
    @ApiOperation(value = "productPage", notes = "Get product page data from API. Data is up-to-date")
    @GetMapping("/productpage")
    public ControllerResponse<ProductPage> productPage(String productKey) {
        // Product page data from API to database
        String s = this.api.selectProductPageById(productKey).getContent();
        JSONObject jPage = new JSONObject(s);
        MetroProductPageBuilder builder = new MetroProductPageBuilder(this.api.accountName());
        ProductPage productPage = builder.pageInfo(jPage).sellers(jPage).build();
        for (Competitor c: productPage.getCompetitors()) {
            SqlService.sqlInsertOrUpdate(c, competitorMapper);
            SqlService.sqlInsertOrUpdate(c.getShippingGroup(), shippingGroupMapper);
        }

        // Product data from database.
        List<Competitor> competitors = competitorMapper.findAllCompetitorByProductId(productPage.getCode());
        List<Competitor> self = competitors.stream().filter(c -> c.getShopName().equals(this.api.accountName())).collect(Collectors.toList());
        productPage.setCompetitors(competitors);
        productPage.setSelf(self.get(0));
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
        List<ProductPage> pages = new ArrayList<>();
        for (String productId: productIdList) {
            ProductPage page = new ProductPage();
            Offer offer  = this.offerMapper.selectById(productId);
            List<Competitor> sellers = this.competitorMapper.findAllCompetitorByProductId(productId);
            page.setCompetitors(sellers);
            page.setId(offer.getProductKey());
            page.setCode(offer.getId());
            page.setProductName(offer.getProductName());
            pages.add(page);
        }
        ControllerResponse response = ControllerResponse.ok().setData(pages);
        return response;
    }
}
