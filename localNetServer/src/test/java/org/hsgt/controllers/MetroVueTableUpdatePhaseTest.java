package org.hsgt.controllers;

import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.pricing.Offer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MetroVueTableUpdatePhaseTest {

    @Autowired
    MetroOfferController metroOfferController;

    @Test
    void testTableInitialization() {
        // Get list of offers to update
        List<Offer> offerList = metroOfferController.selectAll();

        // Get offers with more details.
        for (Offer o: offerList) {
            ProductPage productPage = metroOfferController.productPage(o.getProductKey());
            System.out.println(productPage);
        }



    }

}
