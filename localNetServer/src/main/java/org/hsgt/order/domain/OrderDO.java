package org.hsgt.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_order")
public class OrderDO {
    @TableId(type = IdType.AUTO)
    Long id;
    String serialNumber;   // serial number of the order
    String orderKey;
    Long buyerId;
    Long shopId;
    Integer status;
    Integer paymentType;
    Float shippingCost;
    Float weight;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    Integer channel;
}
