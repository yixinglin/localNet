package org.hsgt.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.hsgt.pricing.BO.Competitor;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.controllers.response.SuggestedPrice;
import org.hsgt.pricing.domain.OfferDO;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.servicesV2.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private IOfferService offerService;
    @Autowired
    private IPricingConfigureService pricingConfigureService;
    @Autowired
    private ICompetitionService competitionService;

    @Autowired
    private IShippingGroupService shippingGroupService;
    @Autowired
    private IPriceManagementService priceManagementService;

    @Autowired
    private SellerApi metroSellerApi;

    @Test
    public void testOfferService() {
        List<OfferDO> list = offerService.list();
        System.out.println(list);
        List<Offer> offerList = offerService.listDetails(null);
        Offer offer = offerService.getByIdDetails("AAA0000982685");

        boolean b1 = offerService.saveOrUpdateByApi();

        OfferDO offerDO = new OfferDO();
        offerDO.setProductId("AAA0000982685");
        offerDO.setNote("Getein 1er");
        // iOfferService.saveOrUpdate(offerDO);

        List<Configure> configures = pricingConfigureService.listDetails(null);
        System.out.println(configures);

        List<Competitor> competitors = competitionService.listDetails(null);
        System.out.println(competitors);

//        boolean orUpdateByApi = shippingGroupService.saveOrUpdateByApi("2150d27b-7c08-4699-a04c-ace84fb38750");
//        System.out.println(orUpdateByApi);

        boolean b2 = shippingGroupService.saveOrUpdateBatchByApi();
        System.out.println(b2);

        List<ProductPage> productPages = competitionService.listProductPageDetailsByIds(Arrays.asList("AAA0000900214"));

        boolean b = competitionService.saveProductPageByApi("b9da5e88-8e8c-49bd-a3a6-8802954cc260");

        System.out.println();
    }

    @Test
    public void testOfferApi() {
        // iOfferService.saveOrUpdateByApi();
        boolean b = offerService.saveOrUpdateByApi();
        LambdaQueryWrapper<OfferDO> wrapper = new LambdaQueryWrapper();
        wrapper.eq(OfferDO::getActive, true);
        List<Offer> offers = offerService.listDetails(wrapper);

        offers.stream().forEach(o -> {
            competitionService.saveProductPageByApi(o.getProductKey());
        });
        System.out.println();
    }

    @Test
    public void testPricingSuggest() {
        // Update offer via api
        boolean b = offerService.saveOrUpdateByApi();
        // Update product page via api
        String id = "AAA0001094368";
        Offer offer = offerService.getByIdDetails(id);
        
        boolean b1 = competitionService.saveProductPageByApi(offer.getProductKey());// AAA0001094370    AAA0001029983 AAA0001094368
        // Get suggestions
        SuggestedPrice newPrice = priceManagementService.suggestPriceUpdate(id);
        System.out.println(newPrice);
    }
}
