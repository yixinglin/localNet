package org.hsgt.api;
import org.net.HttpRequest;
import org.net.HttpResponse;
import org.utils.IoUtils;

public class MetroMockSellerApi extends HttpRequest implements SellerApi {

    public static final String dataDir = "../data";

    public MetroMockSellerApi() {
    }
    
    @Override
    public HttpResponse selectAllOrders() {
        System.out.println("@@ MOCK: selectAllOrders");
        String content = IoUtils.readFile(this.dataDir + "/metro/orders.json");
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(200);
        return httpResponse;
    }

    @Override
    public HttpResponse selectOrders(int limit, int offset) {
        return null;
    }

    @Override
    public HttpResponse selectAllOffers() {
        System.out.println("@@ MOCK: selectOffers");
        String content = IoUtils.readFile(this.dataDir + "/metro/offers.json");
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(200);
        return httpResponse;
    }

    @Override
    public HttpResponse selectOffers(int limit, int offset) {
        return null;
    }

    @Override
    public HttpResponse selectAllShippingGroups() {
        System.out.println("@@ MOCK: selectShippingGroups");
        String content = IoUtils.readFile(this.dataDir + "/metro/shippingGroups.json");
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(300);
        return httpResponse;
    }

    @Override
    public HttpResponse selectShippingGroups(int limit, int offset) {
        return null;
    }

    @Override
    public HttpResponse selectDocById(String id) {
        return null;
    }

    @Override
    public HttpResponse selectOrderById(String id) {
        System.out.println("@@ MOCK: selectOrderById");
        String fname = String.format(this.dataDir + "/metro/orders/%s.json", id);
        String content = IoUtils.readFile(fname);
        HttpResponse httpResponse = new HttpResponse(200, content);
        return httpResponse;
    }

    @Override
    public HttpResponse selectOfferById(String id) {
        return null;
    }

    @Override
    public HttpResponse selectShippingGroupById(String id) {
        System.out.println("@@ MOCK: selectShippingGroupById");
        String fname = String.format(this.dataDir + "/metro/shippingGroups/%s.json", id);
        String content = IoUtils.readFile(fname);
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(100);
        return httpResponse;
    }

    @Override
    public HttpResponse selectProductPageById(String id) {
        System.out.printf("@@ MOCK: selectProductPageById %s\n", id);
        String content = IoUtils.readFile(String.format(this.dataDir + "/metro/pages/%s.json", id));
        HttpResponse httpResponse = new HttpResponse(200, content);
        delay(300);
        return httpResponse;
    }

    @Override
    public HttpResponse updateOfferById(Object offer, String id) {
        return null;
    }

    @Override
    public String accountName() {
        return "metromockseller";
    }
}
