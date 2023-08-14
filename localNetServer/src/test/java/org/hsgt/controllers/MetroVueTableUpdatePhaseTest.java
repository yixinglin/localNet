package org.hsgt.controllers;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Offer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MetroVueTableUpdatePhaseTest {

    @Autowired
    MetroOfferController metroOfferController;
    @Autowired
    MetroShippingGroupController metroShippingGroupController;

    @Test
    void testTableInitialization() {
        // Get list of offers to update
        List<Offer> offerList = (List<Offer>) metroOfferController.selectAll().getData();

        // Get offers with more details.
        for (Offer o: offerList) {
            MatcherAssert.assertThat(o, Matchers.notNullValue());
            ProductPage productPage = (ProductPage) metroOfferController.productPage(o.getProductKey()).getData();
            MatcherAssert.assertThat(productPage, Matchers.notNullValue());
            System.out.println(productPage);
        }

        List<ShippingGroup> shippingGroups = metroShippingGroupController.getShippingGroupList();
        for (ShippingGroup e: shippingGroups) {
            MatcherAssert.assertThat(e, Matchers.notNullValue());
            ShippingGroup sg = metroShippingGroupController.getShippingGroupById(e.getId());
            MatcherAssert.assertThat(sg, Matchers.notNullValue());
            System.out.println(sg);
        }
    }

    @Autowired
    MetroPriceManagementController metroPriceManagementController;
    @Test
    void testPriceSuggestion() {
        // metroPriceManagementController.suggestPriceToUpdate("AAA0000718012");
        // metroPriceManagementController.suggestPriceToUpdate("AAA0000731095");
        metroPriceManagementController.suggestPriceToUpdate("AAA0000857860");
    }

}
