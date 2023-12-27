package org.hsgt.pricing.domain;
import lombok.Data;
import java.io.Serializable;

// @TableName("t_shippinggroup")
@Data
public class ShippingGroup implements Serializable {

    // @TableId
    private String id="0";  // Shipping Group ID
    // @TableField("name")
    private String groupName="free";        // Shipping group name.
    private float unitCost= 0;         //  Shipping cost from
    private float extraUnitCost =0;    //  Extra shipping cost when buying more items

    private float maxShippingCost= 99999;
    private float freeFrom=99999;         // Free shipping from

    private int minTransportDays=0; // Minimum transport time in working days
    private int maxTransportDays=0; // Maximum transport time in working days
    private String destCountry;   // Destination country.
    private String platform;
    private String owner;

}
