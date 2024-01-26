package org.hsgt.pricing.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_offer")
public class OfferDO {
    @TableId
    private String productId;      // primary key
    private Float price;
    private Float lowestPrice;  // manually update
    private String productName;
    @TableField(fill = FieldFill.INSERT_UPDATE, update = "now()")
    private LocalDateTime datetime;
    private String note;    // manually update
    private Integer quantity;    // to update
    private String manufacturer;
    private String platform;
    private Integer amount;
    private String productKey;
    private String shippingGroupId;
    private String unit;
    // private Boolean active;
    @TableLogic
    private Boolean delFlag;

}
