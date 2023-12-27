package org.hsgt.order.rest.metro;

import org.apache.commons.lang3.NotImplementedException;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.core.rest.ExternalRestAPIMetro;
import org.net.HttpResponse;
import org.utils.IoUtils;

public class MetroMockSellerApi extends ExternalRestAPIMetro implements SellerApi {

    public static final String dataDir = "../data";
    private final ApiKey apiKey;

    public MetroMockSellerApi(ApiKey apiKey) {
        super(apiKey);
        this.apiKey = apiKey;
    }

    @Override
    public HttpResponse selectAllOrders() {
        logger.info("@@ MOCK: selectAllOrders");
        String content = IoUtils.readFile(this.dataDir + "/metro/orders.json");
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(200);
        return httpResponse;
    }

    @Override
    public HttpResponse selectOrders(int limit, int offset) {
        logger.info("@@ MOCK: selectOrders");
        return this.selectAllOrders();
    }

    @Override
    public HttpResponse selectDocById(String id) {
        throw new NotImplementedException("selectDocById");
    }

    @Override
    public HttpResponse selectOrderById(String id) {
        logger.info("@@ MOCK: selectOrderById");
        String fname = String.format(this.dataDir + "/metro/orders/%s.json", id);
        String content = IoUtils.readFile(fname);
        HttpResponse httpResponse = new HttpResponse(200, content);
        return httpResponse;
    }


}
