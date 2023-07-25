package org.hsgt.api;

import org.net.HttpResponse;

public interface SellerApi {
    public static final int METRO = 1;

    public static final int METRO_MOCKED = 2;
    public static final int KAUFLAND = 3;
    public static final int KAUFLAND_MOCKED = 4;

    public HttpResponse selectAllOrders();
    public HttpResponse selectAllOffers();
    public HttpResponse selectAllShippingGroups();

    public HttpResponse selectDocById(String id);
    public HttpResponse selectOrderById(String id);
    public HttpResponse selectOfferById(String id);
    public HttpResponse selectShippingGroupById(String id);
    public HttpResponse selectProductPageById(String id);

    public HttpResponse updateOfferById(Object offer, String id);



    String accountName();




}
