package org.hsgt.pricing.rest.common;

import org.hsgt.core.rest.ApiKey;
import org.hsgt.pricing.rest.kaufland.KauflandSellerApi;
import org.hsgt.pricing.rest.metro.MetroMockSellerApi;
import org.hsgt.pricing.rest.metro.MetroSellerApi;

public class SellerApiFactory {

    public static SellerApi createSellerApi(int channel, ApiKey apiKey, boolean isCached) {
        SellerApi api;
        switch (channel) {
            case SellerApi.METRO:
                api = new MetroSellerApi(apiKey);
                break;
            case SellerApi.METRO_MOCKED:
                api = new MetroMockSellerApi(apiKey);
                break;
            case SellerApi.KAUFLAND:
                api = new KauflandSellerApi();
                break;
            default:
                api = null;
        }
        if (isCached) {
            api = new CachedSellerApi(api);
        }
        return api;
    }

}
