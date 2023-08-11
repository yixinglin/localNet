package org.hsgt.api;

import org.hsgt.config.AccountConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.net.HttpResponse;
import org.utils.IoUtils;

import static org.junit.jupiter.api.Assertions.*;

class MetroSellerApiTest  {

    SellerApi api;
    public MetroSellerApiTest() {
        String s = IoUtils.readFile("../data/config.json");
        JSONObject conf = new JSONObject(s);
        ApiKey apiKey = AccountConfig.generateApiKey(AccountConfig.METRO_KEY);
        api = SellerApiFactory.createSellerApi(SellerApi.METRO, apiKey, true);
    }
//    @Test
//    void testSelectAllOrders() {
//    }

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
        String s = this.api.selectShippingGroups(10, 0).getContent();
        System.out.println(s);
    }

    @Test
    void testSelectShippingGroups() {
    }

    @Test
    void testSelectOrderById() {
        HttpResponse resp = this.api.selectOrderById("0af2e7eb-7ed9-4b67-ad00-214a2c25b1f6");
        System.out.println(resp.getContent());
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