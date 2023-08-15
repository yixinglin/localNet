package org.hsgt.entities.pricing;
import lombok.Data;
import org.hsgt.entities.common.ShippingGroup;

import java.io.Serializable;

@Data
public class Competitor implements Serializable {
    private String id;        // seller id
    private String productId; //
    private float price1;    // Price on sale
    private float price2;    // Original price.
    private int quantity;
    private String label;
    private String shopName;
    private int rank;
    private String datetime;
    private ShippingGroup shippingGroup;

}
