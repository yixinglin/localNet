package org.hsgt.entities.pricing;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hsgt.entities.common.ShippingGroup;

import java.io.Serializable;


@TableName("t_offer")
@Data
public class Offer implements Serializable {
    // @TableField("productId")
    @TableId("productId")
    private String id;      // primary key
    private float price;
    private float lowestPrice;  // manually update
    private String productName;
    private String productKey;
    private int amount;
    private int quantity;    // to update
    private String note;    // manually update
    private String manufacturer;
    @TableField(exist = false)
    private ShippingGroup shippingGroup;
    private String platform;

}
