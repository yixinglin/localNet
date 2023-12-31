package org.hsgt.order.config;

import lombok.Data;
import org.hsgt.order.rest.carriers.CarrierApi;
import org.springframework.beans.factory.annotation.Value;

@Data
public abstract class CarrierConfig {
    @Value("${carrier.cache}")
    String cachePath;

    public abstract CarrierApi getApiInstance();
}
