package org.hsgt.pricing.rest.common;

import org.net.HttpResponse;

public interface SellerApi {



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
