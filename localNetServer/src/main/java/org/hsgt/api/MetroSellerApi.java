package org.hsgt.api;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.NotImplementedException;
import org.net.HttpRequest;
import org.net.HttpResponse;


import java.util.*;

public class MetroSellerApi extends HttpRequest implements SellerApi {

    private String client_key;
    private String secret_key;
    private String accountName;
    public final String baseurl_pim = "https://app-seller-pim.prod.de.metro-marketplace.cloud";
    public final String baseurl_inventory = "https://app-seller-inventory.prod.de.metro-marketplace.cloud";
    public final String baseurl_orders = "https://app-order-management.prod.de.metro-marketplace.cloud";

    public MetroSellerApi(ApiKey apiKey) {
        this.client_key = apiKey.getClientKey();
        this.secret_key = apiKey.getSecretKey();
        this.accountName = apiKey.getAccountName();
    }
    @Override
    public HttpResponse selectAllOrders() {
        throw new NotImplementedException("selectOfferById");
    }

    @Override
    public HttpResponse selectOrders(int limit, int offset) {
        String status = null;
        String baseurl = this.baseurl_orders + "/openapi/v2/orders";
        String params = String.format("?limit=%d" + "&offset=%d", limit, offset);
        params += "&sort%5BcreatedAt%5D=DESC";
        if (status != null) {
            params += "&filter%5Bstatus%5D%5B%5D=" + status;
        }
        String url = baseurl + params;
        Map headers = this.getHttpHeaders("GET", url, "");

        HttpResponse resp;
        try {
            resp = this.methodGetRequest(url, headers);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("selectOrders");
        }
        return resp;
    }

    @Override
    public HttpResponse selectAllOffers() {
        return this.selectOffers(1000, 0);
    }

    @Override
    public HttpResponse selectOffers(int limit, int offset) {
        String baseurl = this.baseurl_inventory + "/openapi/v2/offers";
        String params = String.format("?limit=%d" + "&offset=%d", limit, offset);
        params += "&sort%5BcreatedAt%5D=DESC";
        String url = baseurl + params;
        Map headers = this.getHttpHeaders("GET", url, "");
        HttpResponse resp;
        try {
            resp = this.methodGetRequest(url, headers);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("selectOffers");
        }
        return resp;
    }

    @Override
    public HttpResponse selectAllShippingGroups() {
        String url = this.baseurl_inventory + "/openapi/v1/shipping-groups";
        Map headers = this.getHttpHeaders("GET",  url,"");
        HttpResponse resp;
        try {
            resp = this.methodGetRequest(url, headers);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("selectAllShippingGroups");
        }
        return resp;
    }

    @Override
    public HttpResponse selectShippingGroups(int limit, int offset) {
        return this.selectAllShippingGroups();
    }

    @Override
    public HttpResponse selectDocById(String id) {
        return null;
    }

    @Override
    public HttpResponse selectOrderById(String id) {
        String url = String.format("%s/openapi/v2/orders/%s", this.baseurl_orders, id);
        Map headers = this.getHttpHeaders("GET", url, "");
        HttpResponse resp;
        try {
            resp = this.methodGetRequest(url, headers);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("selectDocById");
        }
        return resp;
    }

    @Override
    public HttpResponse selectOfferById(String id) {
        throw new NotImplementedException("selectOfferById");
    }

    @Override
    public HttpResponse selectShippingGroupById(String id) {
        String url = this.baseurl_inventory + "/openapi/v1/shipping-groups/" + id;
        Map headers = this.getHttpHeaders("GET", url, "");
        HttpResponse resp;
        try {
            resp = this.methodGetRequest(url, headers);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("selectShippingGroupById");
        }
        return resp;
    }

    @Override
    public HttpResponse selectProductPageById(String id) {
        String baseurl = "https://service-product-index.prod.de.metro-marketplace.cloud/api/public/products/%s?useReferencePriceAsBase=1";
        String url = String.format(baseurl, id);
        Map<String, String> headers = ImmutableMap.of("Accept", "application/json, text/plain, */*",
                "Content-Type", "text",
                //"accept-encoding", "gzip, deflate, br",
                "accept-language", "de",
                "content-language", "de",
                "country-code", "de",
                "origin", "https://www.metro.de",
                "referer", "https://www.metro.de/",
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
        HttpResponse resp;
        try {
            resp = this.methodGetRequest(url, headers);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("selectProductPageById");
        }
        return resp;
    }

    @Override
    public HttpResponse updateOfferById(Object payload, String id) {
        throw new NotImplementedException("selectOfferById");
    }

    @Override
    public String accountName() {
        return this.accountName;
    }

    private String signRequest(String method, String uri, String body, long timestamp)  {
        List<String> elem = Arrays.asList(method, uri, body, Long.toString(timestamp));
        String plainText = String.join("\n", elem);
        String sign = Hashing.hmacSha256(this.secret_key.getBytes()).hashBytes(plainText.getBytes()).toString();
        return sign;
    }

    public Map getHttpHeaders(String method, String uri, String body) {
        long timestamp = new Date().getTime()/1000;
        String sign = this.signRequest(method, uri, body, timestamp);
        Map<String, String> headers = ImmutableMap.of("Accept", "application/json",
                "X-Client-Id", this.client_key, "X-Timestamp", Long.toString(timestamp),
                "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36",
                "X-Signature", sign);
        return new HashMap(headers);
    }

}
