package org.hsgt.pricing.rest.metro;

import org.apache.commons.lang3.NotImplementedException;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.pricing.rest.common.SellerApi;
import org.net.HttpRequest;
import org.net.HttpResponse;
import org.utils.IoUtils;
import org.utils.Logger;

public class MetroMockSellerApi extends HttpRequest implements SellerApi {
    Logger logger = Logger.loggerBuilder(MetroMockSellerApi.class);
    public static final String dataDir = "../data";
    private final ApiKey apiKey;

    public MetroMockSellerApi(ApiKey apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public HttpResponse selectAllOffers() {
        logger.info("@@ MOCK: selectOffers");
        String content = IoUtils.readFile(this.dataDir + "/metro/offers.json");
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(200);
        return httpResponse;
    }

    @Override
    public HttpResponse selectOffers(int limit, int offset) {
        return this.selectAllOffers();
    }

    @Override
    public HttpResponse selectAllShippingGroups() {
        logger.info("@@ MOCK: selectShippingGroups");
        String content = IoUtils.readFile(this.dataDir + "/metro/shippingGroups.json");
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(300);
        return httpResponse;
    }

    @Override
    public HttpResponse selectShippingGroups(int limit, int offset) {
        return this.selectAllShippingGroups();
    }


    @Override
    public HttpResponse selectOfferById(String id) {
        throw new NotImplementedException("selectOfferById");
    }

    @Override
    public HttpResponse selectShippingGroupById(String id) {
        logger.info("@@ MOCK: selectShippingGroupById");
        String fname = String.format(this.dataDir + "/metro/shippingGroups/%s.json", id);
        String content = IoUtils.readFile(fname);
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(100);
        return httpResponse;
    }

    @Override
    public HttpResponse selectProductPageById(String id) {
        logger.info( String.format("@@ MOCK: selectProductPageById [%s]\n", id));
        String content = IoUtils.readFile(String.format(this.dataDir + "/metro/pages/%s.json", id));
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(300, 800);
        return httpResponse;
    }

    @Override
    public HttpResponse updateOfferById(Object payload, Object default_, boolean actualUpdate) {
        logger.info(String.format("@@ MOCK: updateOfferById [%s]\n", payload.toString()));
        HttpResponse httpResponse = new HttpResponse(200,  "Mock");
        return httpResponse;
    }

    @Override
    public String accountName() {
        return this.apiKey.getAccountName();
    }
}
