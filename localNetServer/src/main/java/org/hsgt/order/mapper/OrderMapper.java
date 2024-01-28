package org.hsgt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hsgt.order.domain.OrderDO;

@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {
}
