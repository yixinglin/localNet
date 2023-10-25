package org.hsgt.config;

import org.hsgt.api.ApiKey;
import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;

import java.util.Arrays;
import java.util.List;

public class Global {
    public static final boolean DEBUG = true;
    public static final boolean allowActualPricing = false;
    public static final boolean ALLOW_API_UPDATE = true;

    public static List<String> pricing_filterKeywords = Arrays.asList("hansagt");
    public static final long dynamic_time_delay_sec = 1200;  // in seconds
    public static boolean enable_metro_dynamic_pricing = true;
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
