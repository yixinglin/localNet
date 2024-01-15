package org.hsgt.pricing.servicesV2;

import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.controllers.response.SuggestedPrice;
import org.json.JSONArray;

public interface IPriceManagementService {

    SuggestedPrice suggestPriceUpdate(String productId);
    public NewOffer pricing(NewOffer latestOffer, JSONArray offerList, String ip);
}
