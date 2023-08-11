package org.hsgt.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.net.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class MetroMockSellerApiTest extends MetroMockSellerApi {


    public MetroMockSellerApiTest(ApiKey apiKey) {
        super(apiKey);
    }

    @Test
    void testSelectAllOrders() {
        System.out.println(selectAllOrders());
    }

    @Test
    void testSelectAllShippingGroups() {
        System.out.println(selectAllShippingGroups());
    }

    @Test
    void testSelectDocById() {
    }

    @Test
    void testSelectOrderById() {
        System.out.println(selectOrderById("0f352763-5ddd-42d5-918f-1097aad53451"));
    }

    @Test
    void testSelectOfferById() {
    }

    @Test
    void testSelectShippingGroupById() {
        System.out.println(selectShippingGroupById("6fae246e-8936-44da-9b0c-1cabb447ad15"));
    }

    @Test
    void testSelectProductPageById() {
        System.out.println(selectProductPageById("38f90e2d-40f1-4809-a903-a276c4edcb09"));
    }

    @Test
    void testUpdateOfferById() {
    }

    @Test
    void testAccountName() {
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSelectAllOffers() {
        System.out.println(selectAllOffers());
    }

}