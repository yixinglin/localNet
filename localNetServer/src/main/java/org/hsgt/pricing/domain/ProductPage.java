package org.hsgt.pricing.domain;

import lombok.Data;
import org.hsgt.pricing.domain.pricing.Competitor;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductPage implements Serializable {

    private String id;  // product ID
    private String code; // GTIN or MID
    private String manufacturer;
    private String productName;
    private Competitor self;  // My Shop
    private List<Competitor> competitors;  // Product pages of competitors
}
