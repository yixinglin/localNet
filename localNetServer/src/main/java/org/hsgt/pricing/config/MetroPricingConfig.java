package org.hsgt.pricing.config;

import org.hsgt.core.config.AccountConfig;
import org.hsgt.core.enu.ApiKeyType;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.rest.metro.MetroMockSellerApi;
import org.hsgt.pricing.rest.metro.MetroSellerApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "pricing.metro")
@Component
public class MetroPricingConfig extends PricingConfig {

    @Bean("metroOfferSellerApi")
    @Override
    public SellerApi getApiInstance() {
        SellerApi api = this.getApi();
        if (api == null) {
            System.out.println("New Metro API Instance.");
            ApiKey key = AccountConfig.generateApiKey(ApiKeyType.METRO_KEY);
            if (this.isMocked()) {
                // api = SellerApiFactory.createSellerApi(ChannelType.METRO_MOCKED, key, false);
                api = new MetroMockSellerApi(key);
            } else {
                // api = SellerApiFactory.createSellerApi(ChannelType.METRO, key, false);
                api = new MetroSellerApi(key);
            }
            this.setApi(api);
        }
        return api;
    }
}
