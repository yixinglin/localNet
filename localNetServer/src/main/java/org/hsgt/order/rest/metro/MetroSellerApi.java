package org.hsgt.order.rest.metro;

import org.hsgt.core.rest.ApiKey;
import org.hsgt.core.rest.ExternalRestAPIMetro;

public class MetroSellerApi extends ExternalRestAPIMetro implements SellerApi {

    public MetroSellerApi(ApiKey apiKey) {
        super(apiKey);
    }

//    public final String baseurl_pim = "https://app-seller-pim.prod.de.metro-marketplace.cloud";
//    public final String baseurl_orders = "https://app-order-management.prod.de.metro-marketplace.cloud";
//
//    public MetroSellerApi(ApiKey apiKey) {
//        super(apiKey);
//    }

//    @Override
//    public HttpResponse selectAllOrders() {
//        throw new NotImplementedException("selectOfferById");
//    }
//
//    @Override
//    public HttpResponse selectOrders(int limit, int offset) {
//        String status = null;
//        String baseurl = this.baseurl_orders + "/openapi/v2/orders";
//        String params = String.format("?limit=%d" + "&offset=%d", limit, offset);
//        params += "&sort%5BcreatedAt%5D=DESC";
//        if (status != null) {
//            params += "&filter%5Bstatus%5D%5B%5D=" + status;
//        }
//        String url = baseurl + params;
//        Map headers = this.getHttpHeaders("GET", url, "");
//
//        HttpResponse resp;
//        try {
//            resp = this.methodGetRequest(url, headers);
//        } catch (Exception e) {
//            this.logger.error(IoUtils.getStackTrace(e));
//            throw new RuntimeException("selectOrders");
//        }
//        return resp;
//    }
//
//    @Override
//    public HttpResponse selectOrderById(String id) {
//        String url = String.format("%s/openapi/v2/orders/%s", this.baseurl_orders, id);
//        Map headers = this.getHttpHeaders("GET", url, "");
//        HttpResponse resp;
//        try {
//            resp = this.methodGetRequest(url, headers);
//        } catch (Exception e) {
//            this.logger.error(IoUtils.getStackTrace(e));
//            throw new RuntimeException("selectDocById");
//        }
//        return resp;
//    }
//
//    @Override
//    public HttpResponse selectDocById(String id) {
//        return null;
//    }
}
