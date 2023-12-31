package org.hsgt.api;

import org.hsgt.core.config.AccountConfig;
import org.hsgt.core.config.e.ApiKeyType;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.rest.metro.MetroMockSellerApi;
import org.hsgt.pricing.rest.metro.MetroSellerApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MetroSellerApiTest {

    SellerApi api;
    SellerApi mockedApi;
    public MetroSellerApiTest() {
        JSONObject conf = AccountConfig.getConfigInstance();
        ApiKey apiKey = AccountConfig.generateApiKey(ApiKeyType.METRO_KEY);
        // api = SellerApiFactory.createSellerApi(ChannelType.METRO_MOCKED, apiKey, false);
        api = new MetroSellerApi(apiKey);
        // mockedApi = SellerApiFactory.createSellerApi(ChannelType.METRO_MOCKED, apiKey, false);
        mockedApi = new MetroMockSellerApi(apiKey);
    }

    @Test
    void testSelectOrders() {
//        String s = this.api.selectOrders(19, 0).getContent();
//        System.out.printf(s);
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
//        HttpResponse resp = this.api.selectOrderById("0af2e7eb-7ed9-4b67-ad00-214a2c25b1f6");
//        System.out.println(resp.getContent());
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
        String s = this.mockedApi.selectAllOffers().getContent();
        JSONArray offerList = new JSONObject(s).getJSONArray("items");
        NewOffer newOffer = new NewOffer();
        newOffer.setProductId("AAA0001037072");
        newOffer.setPrice(4.97f);
        newOffer.setQuantity(44);
        newOffer.setShippingGroupId("0");
        // String a = getClass().getClassLoader().getResource("hsgt").getPath();

        this.api.updateOfferById(newOffer, offerList, false);
    }

}