package org.hsgt.entities.common;

import lombok.Data;
import org.hsgt.entities.pricing.Competitor;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductPage implements Serializable {

    String id;  // product ID
    String code; // GTIN or MID
    String manufacturer;
    String shopName;  // My shop name
    String productName;
    List<Competitor> competitors;  // Product pages of competitors
}
