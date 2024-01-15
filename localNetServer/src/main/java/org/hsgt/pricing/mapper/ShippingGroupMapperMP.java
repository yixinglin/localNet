package org.hsgt.pricing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.hsgt.pricing.domain.ShippingGroupDO;

public interface ShippingGroupMapperMP extends BaseMapper<ShippingGroupDO> {

    @Update({"update t_shippinggroup set logicDel = 1 where id=#{id}"})
    int logicDelete(String id);
    @Update({"update t_shippinggroup set logicDel=0 where id=#{id}"})
    int setLogicalExist(String id);

}
