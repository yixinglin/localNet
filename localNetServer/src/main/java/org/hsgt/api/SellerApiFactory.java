package org.hsgt.api;

import com.google.protobuf.Api;

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
