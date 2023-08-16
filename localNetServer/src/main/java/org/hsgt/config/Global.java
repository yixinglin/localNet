package org.hsgt.config;

import org.hsgt.api.ApiKey;
import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;

public class Global {
    public static final boolean DEBUG = true;
    public static final boolean ALLOW_API_UPDATE = true;
    private Global() {}

    public static SellerApi getMetroApiInstance() {
        ApiKey key = AccountConfig.generateApiKey(AccountConfig.METRO_KEY);
        SellerApi api;
        if (DEBUG) {
            api = SellerApiFactory.createSellerApi(SellerApi.METRO_MOCKED, key, false);
        } else {
            api = SellerApiFactory.createSellerApi(SellerApi.METRO, key, false);
        }
        return api;
    }

}
