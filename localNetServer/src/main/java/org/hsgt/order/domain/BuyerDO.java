package org.hsgt.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_buyer")
public class BuyerDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String mobile;
    private Integer channel;

    private Long billingAddrId;
    private Long shippingAddrId;
}
