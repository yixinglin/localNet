package org.hsgt.config;

import org.hsgt.api.ApiKey;
import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "pricing.metro")
@Component
public class MetroPricingConfig extends PricingConfig{

    public MetroPricingConfig() {
        System.out.println("MetroPricingConfig");
    }

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
