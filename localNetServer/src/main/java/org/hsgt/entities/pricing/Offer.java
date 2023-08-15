package org.hsgt.entities.pricing;
import lombok.Data;
import org.hsgt.entities.common.ShippingGroup;

import java.io.Serializable;


// @TableName(value = "t_offer", autoResultMap = true)
@Data
public class Offer implements Serializable {
    // @TableId("productId")
    private String id;      // primary key
    private float price;
    private String datetime;
    private float lowestPrice;  // manually update
    private String productName;
    private String productKey;
    private int amount;
    private int quantity;    // to update

    private String note;    // manually update
    private String manufacturer;
    // @TableField(exist = false)
    private ShippingGroup shippingGroup;
    private String platform;

}
