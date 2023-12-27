package org.hsgt.pricing.config;

import org.hsgt.core.config.AccountConfig;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.rest.common.SellerApiFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "pricing.metro")
@Component
public class MetroPricingConfig extends PricingConfig {

    @Override
    public SellerApi getApiInstance() {
        SellerApi api = this.getApi();
        if (api == null) {
            System.out.println("New Metro API Instance.");
            ApiKey key = AccountConfig.generateApiKey(AccountConfig.METRO_KEY);
            if (this.isMocked()) {
                api = SellerApiFactory.createSellerApi(SellerApi.METRO_MOCKED, key, false);
            } else {
                api = SellerApiFactory.createSellerApi(SellerApi.METRO, key, false);
            }
            this.setApi(api);
        }
        return api;
    }
}
