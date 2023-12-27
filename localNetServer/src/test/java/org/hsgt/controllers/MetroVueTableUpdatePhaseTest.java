package org.hsgt.controllers;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hsgt.pricing.controllers.MetroOfferController;
import org.hsgt.pricing.controllers.MetroPriceManagementController;
import org.hsgt.pricing.controllers.MetroShippingGroupController;
import org.hsgt.pricing.domain.ProductPage;
import org.hsgt.pricing.domain.ShippingGroup;
import org.hsgt.pricing.domain.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MetroVueTableUpdatePhaseTest {

    @Autowired
    MetroOfferController metroOfferController;
    @Autowired
    MetroShippingGroupController metroShippingGroupController;


    void testTableInitialization() {
        // Get list of offers to update
        List<Offer> offerList = (List<Offer>) metroOfferController.selectAll().getData();

        // Get offers with more details.
        for (Offer o: offerList) {
            MatcherAssert.assertThat(o, Matchers.notNullValue());
            ProductPage productPage = metroOfferController.productPage(o.getProductKey()).getData();
            MatcherAssert.assertThat(productPage, Matchers.notNullValue());
            System.out.println(productPage);
        }

        List<ShippingGroup> shippingGroups = metroShippingGroupController.getShippingGroupList().getData();
        for (ShippingGroup e: shippingGroups) {
            MatcherAssert.assertThat(e, Matchers.notNullValue());
            ShippingGroup sg = metroShippingGroupController.getShippingGroupById(e.getId()).getData();
            MatcherAssert.assertThat(sg, Matchers.notNullValue());
            System.out.println(sg);
        }
    }

    @Autowired
    MetroPriceManagementController metroPriceManagementController;

    void testPriceSuggestion() {
        // metroPriceManagementController.suggestPriceToUpdate("AAA0000718012");
        // metroPriceManagementController.suggestPriceToUpdate("AAA0000731095");
        metroPriceManagementController.suggestPriceToUpdate("AAA0000857860");
    }

}
