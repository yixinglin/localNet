package org.hsgt.order.rest.metro;

import org.hsgt.core.rest.ApiKey;


public class SellerApiFactory {

    public static SellerApi createSellerApi(int channel, ApiKey apiKey, boolean isCached) {
        SellerApi api;
        switch (channel) {
            case org.hsgt.pricing.rest.common.SellerApi.METRO:
                api = new MetroSellerApi(apiKey);
                break;
            case org.hsgt.pricing.rest.common.SellerApi.METRO_MOCKED:
                api = new MetroMockSellerApi(apiKey);
                break;
            default:
                api = null;
        }

        return api;
    }
}
