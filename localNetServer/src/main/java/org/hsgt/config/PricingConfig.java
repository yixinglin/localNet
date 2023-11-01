package org.hsgt.config;

import lombok.Data;
import org.hsgt.api.SellerApi;

import java.util.List;


@Data
public abstract class PricingConfig {
    private boolean mocked;
    private boolean allowActualPricing;
    private boolean enableDynamicPricing;
    private long dynamicTaskDelayMs;
    private long dynamicHttpDelayMs;
    private long dynamicHttpDelayMax;
    private List<String> filterKeywords;
    private String note;
    private SellerApi api;

    public abstract SellerApi getApiInstance();
}
