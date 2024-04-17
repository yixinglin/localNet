package org.hsgt.order.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.order.BO.OrderLine;
import org.hsgt.order.domain.OrderLineDO;
import org.hsgt.order.mapper.OrderLineMapper;
import org.hsgt.order.services.IOrderLineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderLineService extends ServiceImpl<OrderLineMapper, OrderLineDO> implements IOrderLineService {

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

    @Override
    public List<OrderLineDO> getByOrderId(Long id) {
        LambdaQueryWrapper<OrderLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderLineDO::getOrderId, id);
        List<OrderLineDO> list = this.list(wrapper);
        return list;
    }

    @Override
    public List<OrderLine> getDetailsByOrderId(Long id) {
        List<OrderLineDO> orderLineDOList = this.getByOrderId(id);
        List<OrderLine> lines = orderLineDOList.stream().map(o -> {
            return OrderLine.convertToBO(o);
        }).collect(Collectors.toList());

        return lines;
    }
}
