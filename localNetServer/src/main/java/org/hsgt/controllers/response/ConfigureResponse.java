package org.hsgt.controllers.response;

import lombok.Data;

@Data
public class ConfigureResponse {
    String productId;
    String productName;
    float lowestPrice;
    String offerNote;
    int offerAmount;
    boolean enabled;
    String strategyId;
    float maxAdjust;
    float reduce;
}
