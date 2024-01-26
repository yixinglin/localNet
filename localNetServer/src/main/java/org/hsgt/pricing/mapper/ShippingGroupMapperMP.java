package org.hsgt.pricing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.hsgt.pricing.domain.ShippingGroupDO;

public interface ShippingGroupMapperMP extends BaseMapper<ShippingGroupDO> {
    @Update({"update t_shippinggroup set delFlag=0 where id=#{id}"})
    int recoverById(String id);
}
