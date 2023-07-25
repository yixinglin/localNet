package org.hsgt.controllers;

import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;
import org.hsgt.builders.metro.MetroOfferBuilder;
import org.hsgt.builders.metro.MetroShippingGroupBuilder;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.ShippingGroupMapper;
import org.hsgt.services.OfferService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestOfferController {

    @Autowired
    OfferService offerService;

    @Autowired
    OfferMapper offerMapper;
    @Autowired
    ShippingGroupMapper shippingGroupMapper;

    @Test
    void testMetroOfferController() {
        SellerApi metroApi = SellerApiFactory.createSellerApi(SellerApi.METRO_MOCKED, null);
        String offerList = metroApi.selectAllOffers().getContent();
        String shippingGroupList = metroApi.selectAllShippingGroups().getContent();
        Offer mOffer1, mOffer2;
        ShippingGroup mShippingGroup1;
        try {
            mOffer1 = new MetroOfferBuilder().offer( new JSONObject(offerList), "AAA0001010491" )
                    .moreField(offerMapper).shippingGroup(shippingGroupMapper).build();
            System.out.println(mOffer1);

            mOffer2 = new MetroOfferBuilder().offer( new JSONObject(offerList), "AAA0001094370" )
                    .moreField(offerMapper).shippingGroup(shippingGroupMapper).build();

            System.out.println(mOffer2);
            mShippingGroup1 = new MetroShippingGroupBuilder().db("c60cd2a4-aca4-4aef-8b2e-93e48d99bb15", shippingGroupMapper).build();
            System.out.println(mShippingGroup1);
            mShippingGroup1 = new MetroShippingGroupBuilder()
                    .web(new JSONObject(shippingGroupList), "8c23b432-b06b-4b22-8ae6-e199b2f79765", metroApi)
                    .build();
            System.out.println(mShippingGroup1);

        } catch (Exception e) {
            mOffer1 = mOffer2 = null;
            System.err.println(e);
        }
        System.out.println("Finish");
    }
}
