package org.hsgt.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.net.HttpRequest;
import org.net.HttpResponse;
import org.utils.IoUtils;
import org.utils.Logger;

import java.util.*;


public class MetroSellerApi extends HttpRequest implements SellerApi {
    private Logger logger = Logger.loggerBuilder(MetroSellerApi.class);
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
            this.logger.error(IoUtils.getStackTrace(e));
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
            this.logger.error(IoUtils.getStackTrace(e));
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
            this.logger.error(IoUtils.getStackTrace(e));
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
            this.logger.error(IoUtils.getStackTrace(e));
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
            this.logger.error(IoUtils.getStackTrace(e));
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
            this.logger.error(IoUtils.getStackTrace(e));
            throw new RuntimeException("selectProductPageById");
        }
        return resp;
    }

    /**
     * @param offer: Pre-offer obtained from API.
     * @return JSONObject
     */
    private JSONObject getPostBodyForOfferUpdate(JSONObject offer)   {
        // String path = getClass().getClassLoader().getResource("res/templatePostBodyOfferUpdate-Metro.json").getFile();
        String s = IoUtils.readFile("res/templatePostBodyOfferUpdate-Metro.json");
        JSONObject template = new JSONObject(s);

        for (String key: template.keySet()) {
            if(key.equals("shippingGroupId")) {
                if (!offer.isNull("shippingGroup"))
                    template.put(key, offer.getJSONObject("shippingGroup").getString("shippingGroupId"));
                else
                    template.put(key, JSONObject.NULL);
            } else if(key.equals("businessModel")) {
                int bm = offer.getInt(key);
                String val = (bm == 2)? "B2B": "B2B/B2C";
                template.put(key, val);
            } else {
                template.put(key, offer.get(key));
            }
        }

        return template;
    }

    @Override
    public HttpResponse updateOfferById(Object newOffer, Object offerList, boolean actualUpdate) {
        HttpResponse resp = null;
        JSONObject payload0 = new JSONObject(newOffer);
        String productId = payload0.getString("productId");
        float price = payload0.getFloat("price");
        long quantity = payload0.getLong("quantity");
        String shippingGroupId = payload0.getString("shippingGroupId");
        if (shippingGroupId == null) {
            throw new NoSuchFieldError("shippingGroupId cannot be null.");
        }

        JSONObject preOffer = null;
        for (Object o: (JSONArray) offerList) {
            if (((JSONObject)o).getString("mid").equals(productId)) {
                preOffer = (JSONObject) o;
                break;
            }
        }

        if (preOffer != null) {
            JSONObject payload = this.getPostBodyForOfferUpdate(preOffer);
            payload.getJSONObject("netPrice").put("amount", price);
            payload.put("quantity", quantity);
            if (shippingGroupId.equals("0")) {
                payload.put("shippingGroupId", JSONObject.NULL);
            } else {
                payload.put("shippingGroupId", shippingGroupId);
            }

            if (actualUpdate) {
                String url = this.baseurl_inventory + "/openapi/v2/offers";
                Map headers = this.getHttpHeaders("POST", url, payload.toString());
                headers.put("Content-Type", "application/json");
                try {
                    resp = this.methodPostRequest(url, headers, payload.toString());
                } catch (Exception e) {
                    this.logger.error(IoUtils.getStackTrace(e));
                    throw new RuntimeException("selectProductPageById");
                }
            } else {
                resp = new HttpResponse(200, newOffer.toString());
                System.out.println(payload.toString());
            }
        } else {
            throw new RuntimeException("Offer not exist! " + productId);
        }
        return resp;
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
