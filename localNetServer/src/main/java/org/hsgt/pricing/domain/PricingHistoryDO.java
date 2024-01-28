package org.hsgt.pricing.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_updated_offer")
public class PricingHistoryDO {
    private int id;
    @TableField(fill = FieldFill.INSERT, update = "now()")
    private LocalDateTime datetime;
    private String ip;
    private String productId;
    private Float price;
    private Integer quantity;
    private String shippingGroupId;
    private String note;
    private String username;

}
