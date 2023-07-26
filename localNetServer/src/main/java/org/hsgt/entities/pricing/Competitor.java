package org.hsgt.entities.pricing;
import lombok.Data;
import org.hsgt.entities.common.ShippingGroup;

import java.io.Serializable;

@Data
public class Competitor implements Serializable {
    private String id;        // seller id
    private String productId; //
    // @TableField("price")
    private float price1;    // Price on sale
    // @TableField("price")
    private float price2;    // Original price.
    private int quantity;
    private String label;
    private String shopName;
    // @TableField("rank_")
    private int rank;
    // @TableField()
    private ShippingGroup shippingGroup;

}
