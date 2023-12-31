package org.hsgt.order.config;

import org.hsgt.core.config.AccountConfig;
import org.hsgt.core.config.e.ApiKeyType;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.order.rest.metro.MetroMockSellerApi;
import org.hsgt.order.rest.metro.MetroSellerApi;
import org.hsgt.order.rest.metro.SellerApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "order.metro")
public class MetroOrderConfig extends OrderConfig {

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
