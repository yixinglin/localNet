package org.hsgt.controllers;
import static org.junit.Assert.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;
import org.hsgt.builders.metro.MetroOfferBuilder;
import org.hsgt.builders.metro.MetroProductPageBuilder;
import org.hsgt.builders.metro.MetroShippingGroupBuilder;
import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.CompetitorMapper;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.ShippingGroupMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestOfferController {

//    @Autowired
//    OfferService offerService;

    @Autowired
    OfferMapper offerMapper;
    @Autowired
    ShippingGroupMapper shippingGroupMapper;
    @Autowired
    CompetitorMapper competitorMapper;


    @Test
    void testMetroOfferController() {
        SellerApi metroApi = SellerApiFactory.createSellerApi(SellerApi.METRO_MOCKED, null, false);
        String offerList = metroApi.selectAllOffers().getContent();
        String shippingGroupList = metroApi.selectAllShippingGroups().getContent();
        String productPage =  metroApi.selectProductPageById("5e284310-3551-40ba-981b-bb65161981e5").getContent();
        Offer mOffer1, mOffer2;
        ShippingGroup mShippingGroup1;
        try {
            QueryWrapper<Offer> oWrapper = new QueryWrapper<>();
            oWrapper.orderByAsc("lowestPrice");
            List<Offer> oList = offerMapper.selectList(null);

            mOffer1 = new MetroOfferBuilder().offer( new JSONObject(offerList), "AAA0001010491" )
                    .moreField(offerMapper).shippingGroup(shippingGroupMapper).build();
            System.out.println(mOffer1);
            assertNotNull(mOffer1.getShippingGroup());

            mOffer2 = new MetroOfferBuilder().offer( new JSONObject(offerList), "AAA0001094370" )
                    .moreField(offerMapper).shippingGroup(shippingGroupMapper).build();
             // mOffer2.getShippingGroup()
            assertNull(mOffer2.getShippingGroup().getId());

            System.out.println(mOffer2);
            mShippingGroup1 = new MetroShippingGroupBuilder().db("c60cd2a4-aca4-4aef-8b2e-93e48d99bb15", shippingGroupMapper).build();
            System.out.println(mShippingGroup1);
            mShippingGroup1 = new MetroShippingGroupBuilder()
                    .web(new JSONObject(shippingGroupList), "8c23b432-b06b-4b22-8ae6-e199b2f79765", metroApi)
                    .build();
            System.out.println(mShippingGroup1);

            ProductPage page = new MetroProductPageBuilder("HansaGT Medical")
                    .pageInfo(new JSONObject(productPage))
                    .sellers(new JSONObject(productPage)).build();
            System.out.println(page);

        } catch (Exception e) {
            mOffer1 = mOffer2 = null;
            System.err.println(e);
        }
        System.out.println("Finish");
    }

    @Test
    void testMetroOfferController2() {
        SellerApi metroApi = SellerApiFactory.createSellerApi(SellerApi.METRO_MOCKED, null, false);
        String offerList = metroApi.selectAllOffers().getContent();
        String shippingGroupList = metroApi.selectAllShippingGroups().getContent();
        Offer mOffer1, mOffer2;
        ShippingGroup mShippingGroup1;

        // List<Competitor> competitorList = competitorMapper.selectList(null);
        Competitor c = new Competitor();
        c.setShopName("GoDingens");
        c.setProductId("AAA0000718012");
        c = competitorMapper.selectById(c);
        c.setLabel("aa");
        competitorMapper.updateById(c);

        c.setShopName("AAAAAAAAAAAAAAA");
        competitorMapper.insert(c);
        System.out.println("Finished");

    }

    @Autowired
    MetroOfferController controller;
    @Test
    void testMetroOfferController3() {
        List<Offer> s = controller.selectAll();
        System.out.println(s);

    }
}
