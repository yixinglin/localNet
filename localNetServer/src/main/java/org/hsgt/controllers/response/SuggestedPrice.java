package org.hsgt.controllers.response;

import lombok.Data;

@Data
public class SuggestedPrice {
    String productId;
    float price;    // Suggested price
    float reduced; // Magnitude that price reduced
    int status;    // Status after applying Price suggestion strategy
    String strategy;
}
