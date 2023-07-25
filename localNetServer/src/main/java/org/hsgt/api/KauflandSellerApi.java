package org.hsgt.api;

import org.net.HttpRequest;
import org.net.HttpResponse;

public class KauflandSellerApi extends HttpRequest implements SellerApi {
    @Override
    public HttpResponse selectAllOrders() {
        return null;
    }

    @Override
    public HttpResponse selectAllOffers() {
        return null;
    }

    @Override
    public HttpResponse selectAllShippingGroups() {
        return null;
    }

    @Override
    public HttpResponse selectDocById(String id) {
        return null;
    }

    @Override
    public HttpResponse selectOrderById(String id) {
        return null;
    }

    @Override
    public HttpResponse selectOfferById(String id) {
        return null;
    }

    @Override
    public HttpResponse selectShippingGroupById(String id) {
        return null;
    }

    @Override
    public HttpResponse selectProductPageById(String id) {
        return null;
    }

    @Override
    public HttpResponse updateOfferById(Object offer, String id) {
        return null;
    }

    @Override
    public String accountName() {
        return null;
    }
}
