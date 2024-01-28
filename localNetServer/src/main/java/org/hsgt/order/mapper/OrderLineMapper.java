package org.hsgt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hsgt.order.domain.OrderLineDO;

@Mapper
public interface OrderLineMapper extends BaseMapper<OrderLineDO> {
}
