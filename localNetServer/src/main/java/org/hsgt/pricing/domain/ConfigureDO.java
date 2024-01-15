package org.hsgt.pricing.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_configure")
public class ConfigureDO {

    @TableId
    String productId;
    Boolean enabled;
    String strategy;
    Float maxAdjust;
    Float reduce;
}
