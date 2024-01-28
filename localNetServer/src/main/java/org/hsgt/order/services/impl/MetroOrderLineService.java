package org.hsgt.order.services.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.order.domain.OrderLineDO;
import org.hsgt.order.mapper.OrderLineMapper;
import org.hsgt.order.services.IOrderLineService;
import org.springframework.stereotype.Service;

@Service
public class MetroOrderLineService extends ServiceImpl<OrderLineMapper, OrderLineDO> implements IOrderLineService {

    /**
     * @param entity:
     * @return boolean
     * @author Lin
     * @description Save or update based on orderId
     * @date 28.Jan.2024 028 19:50
     */
    @Override
    public boolean saveOrUpdate(OrderLineDO entity) {
        Long orderId = entity.getOrderId();
        assert orderId != null: "orderId cannot be null";
        LambdaUpdateWrapper<OrderLineDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrderLineDO::getOrderId, orderId);
        return this.update(entity, wrapper) || this.save(entity);
    }
}
