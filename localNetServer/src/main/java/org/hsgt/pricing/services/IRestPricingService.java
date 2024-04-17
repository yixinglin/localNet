package org.hsgt.pricing.services;

import org.hsgt.pricing.controllers.response.NewOffer;
import org.json.JSONArray;

public interface IRestPricingService {

    boolean saveOrUpdateOfferByMetroAPI();
    boolean saveOrUpdateProductPageByMetroAPI(String  id);

    boolean saveOrUpdateShippingGroupListByMetroApi();
    NewOffer pricingViaMetroAPI(NewOffer latestOffer, JSONArray offerList, String ip);
}
