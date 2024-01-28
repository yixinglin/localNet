package org.hsgt.order.services;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.order.BO.Order;
import org.hsgt.order.domain.OrderDO;

import java.io.Serializable;
import java.util.List;

public interface IOrderService extends IService<OrderDO> {

    List<Order> getByIdDetails(Wrapper<OrderDO> queryWrapper);
    Order getByIdDetails(Serializable id);
    boolean saveOrUpdateByApi(Serializable id);

    OrderDO getBySerialNumber(String serialNumber);

}
