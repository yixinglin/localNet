package org.hsgt.pricing.rest.common;

import org.net.HttpResponse;

public interface SellerApi {
    public static final int METRO = 1;
    public static final int METRO_MOCKED = 3;

    public static final int KAUFLAND = 4;
    public static final int KAUFLAND_CACHED = 5;
    public static final int KAUFLAND_MOCKED = 6;


    public HttpResponse selectAllOffers();
    public HttpResponse selectOffers(int limit, int offset);
    public HttpResponse selectAllShippingGroups();
    public HttpResponse selectShippingGroups(int limit, int offset);

    public HttpResponse selectOfferById(String id);
    public HttpResponse selectShippingGroupById(String id);
    public HttpResponse selectProductPageById(String id);

    /**
     * @param payload: Payload of new offer in JSON
     * @param default_: Default offer object in JSON.
     * @param actualUpdate: Perform actual offer update.
     */
    public HttpResponse updateOfferById(Object payload, Object default_, boolean actualUpdate);



    String accountName();




}
