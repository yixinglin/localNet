package org.hsgt.entities.common;

import lombok.Data;
import org.hsgt.entities.pricing.Competitor;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductPage implements Serializable {

    private String id;  // product ID
    private String code; // GTIN or MID
    private String manufacturer;
    private String shopName;  // My shop name
    private String productName;
    private List<Competitor> competitors;  // Product pages of competitors
}
