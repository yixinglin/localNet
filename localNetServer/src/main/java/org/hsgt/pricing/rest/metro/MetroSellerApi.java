package org.hsgt.pricing.rest.metro;

import org.hsgt.core.rest.ApiKey;
import org.hsgt.core.rest.ExternalRestAPIMetro;
import org.hsgt.pricing.rest.common.SellerApi;


public class MetroSellerApi extends ExternalRestAPIMetro implements SellerApi {
    public MetroSellerApi(ApiKey apiKey) {
        super(apiKey);
    }

}
