package org.hsgt.pricing.rest.common;

import org.net.HttpResponse;

public interface SellerApi {



     HttpResponse selectAllOffers();
     HttpResponse selectOffers(int limit, int offset);
     HttpResponse selectAllShippingGroups();
     HttpResponse selectShippingGroups(int limit, int offset);

     HttpResponse selectOfferById(String id);
     HttpResponse selectShippingGroupById(String id);
     HttpResponse selectProductPageById(String id);

    /**
     * @param payload: Payload of new offer in JSON
     * @param default_: Default offer object in JSON.
     * @param actualUpdate: Perform actual offer update.
     */
    public HttpResponse updateOfferById(Object payload, Object default_, boolean actualUpdate);

    String accountName();

}
