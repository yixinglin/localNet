package org.hsgt.pricing.config;

import lombok.Data;
import org.hsgt.pricing.rest.common.SellerApi;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;


@Data
public abstract class PricingConfig {
    private boolean mocked;
    private boolean allowActualPricing;
    private boolean enableDynamicPricing;
    private long dynamicTaskDelayMs;
    private long dynamicHttpDelayMs;
    private long dynamicHttpDelayMax;
    @Value("${app.enable-email-notification}")
    private boolean enableEmailNotification;
    private List<String> filterKeywords;
    private String note;
    private SellerApi api;

    public abstract SellerApi getApiInstance();
}
