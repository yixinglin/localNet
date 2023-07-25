package org.hsgt.controllers;

import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;
import org.hsgt.channel.metro.MetroOffer;
import org.hsgt.channel.metro.MetroOfferBuilder;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.ShippingGroupMapper;
import org.hsgt.services.OfferService;
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
        // metroApi.selectAllOffers();
        // new MetroOfferBuilder().offer();

    }
}
