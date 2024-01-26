package org.hsgt.pricing.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName("t_shippinggroup")
@Data
public class ShippingGroupDO {
    @TableId
    private String id;  // Shipping Group ID
    private String name;        // Shipping group name.
    private Float unitCost;         //  Shipping cost from
    private Float extraUnitCost;    //  Extra shipping cost when buying more items

    private Float maxShippingCost;
    private Float freeFrom;         // Free shipping from

    private Integer minTransportDays; // Minimum transport time in working days
    private Integer maxTransportDays; // Maximum transport time in working days
    private String destCountry;   // Destination country.
    private String platform;
    private String owner;
    @TableLogic
    private Boolean delFlag;

}
