package org.hsgt.pricing.services;

import org.hsgt.pricing.controllers.response.SuggestedPrice;

public interface IPriceManagementService {

    SuggestedPrice suggestPriceUpdate(String productId);
    // public NewOffer pricing(NewOffer latestOffer, JSONArray offerList, String ip);
}
