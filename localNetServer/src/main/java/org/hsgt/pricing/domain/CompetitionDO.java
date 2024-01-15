package org.hsgt.pricing.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_competition")
public class CompetitionDO {
    private String productId;
    private String shopName;
    private Integer rank_;
    private Float price;    // Price on sale
    private Integer quantity;
    private String label;
    private String shippingGroupId;
    @TableField(fill = FieldFill.INSERT_UPDATE, update = "now()")
    private LocalDateTime datetime;
}
