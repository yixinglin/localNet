package org.hsgt.order.config;

import org.hsgt.core.config.AccountConfig;
import org.hsgt.core.config.e.ApiKeyType;
import org.hsgt.core.config.e.Env;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.order.rest.carriers.CarrierApi;
import org.hsgt.order.rest.carriers.GlsApi;
import org.hsgt.order.rest.carriers.GlsMockApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlsConfig extends CarrierConfig {
    @Value("${carrier.gls.env}")
    String env;

    @Override
    public CarrierApi getApiInstance() {
        CarrierApi api = null;
        ApiKey key;
        switch (env) {
            case "prod":
                key = AccountConfig.generateApiKey(ApiKeyType.GLS_KEY);
                api = new GlsApi(key, Env.prod);
                break;
            case "test":
                key = AccountConfig.generateApiKey(ApiKeyType.GLS_TEST_KEY);
                api = new GlsApi(key, Env.test);
                break;
            case "mock":
                key = AccountConfig.generateApiKey(ApiKeyType.GLS_TEST_KEY);
                api = new GlsMockApi(this, key);
                break;
        }
        return api;
    }
}

