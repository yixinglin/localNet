package org.hsgt.pricing.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableField(fill = FieldFill.DEFAULT, update = "0")
    private Boolean logicDel;       // Is logical deleted?

}
