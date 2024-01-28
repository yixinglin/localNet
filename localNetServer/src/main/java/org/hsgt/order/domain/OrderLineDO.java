package org.hsgt.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_orderline")
public class OrderLineDO {
    // Composite Primary Key
    @TableId(type = IdType.AUTO)
    Long id;
    Long orderId;

    Long productId;
    String ean;
    String sku;
    String mid;
    Integer quantity;

    String title;
    String description;
    Integer taxRate;
    Float tax;
    Float netPrice;
    Float grossPrice;
    String currency;
    String unit;

}
