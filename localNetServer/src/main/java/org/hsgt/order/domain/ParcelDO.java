package org.hsgt.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class ParcelDO {
    @TableId(type = IdType.AUTO)
    Long id;
    String trackId;
    String shipperId;
    String reference;
    String phone;
    String email;
    String note;

}
