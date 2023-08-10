package org.hsgt.api;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.utils.IoUtils;

import static org.junit.jupiter.api.Assertions.*;

class MetroSellerApiTest  {

    SellerApi api;
    public MetroSellerApiTest() {
        String s = IoUtils.readFile("../data/config.json");
        JSONObject conf = new JSONObject(s);
        ApiKey apiKey = new ApiKey();
        apiKey.setClientKey(conf.getJSONObject("metro").getString("client"));
        apiKey.setSecretKey(conf.getJSONObject("metro").getString("secret"));
        apiKey.setAccountName(conf.getJSONObject("metro").getString("account"));
        api = SellerApiFactory.createSellerApi(SellerApi.METRO, apiKey);
    }
    @Test
    void testSelectAllOrders() {
    }

    @Test
    void testSelectOrders() {
        String s = this.api.selectOrders(19, 0).getContent();
        System.out.printf(s);
    }

    @Test
    void testSelectAllOffers() {
        String s = this.api.selectOffers(100, 0).getContent();
        System.out.printf(s);
    }

    @Test
    void testSelectOffers() {
    }

    @Test
    void testSelectAllShippingGroups() {
    }

    @Test
    void testSelectShippingGroups() {
    }

    @Test
    void testSelectOrderById() {
    }

    @Test
    void testSelectOfferById() {
    }

    @Test
    void testSelectShippingGroupById() {
    }

    @Test
    void testSelectProductPageById() {
    }

    @Test
    void testUpdateOfferById() {
    }
}