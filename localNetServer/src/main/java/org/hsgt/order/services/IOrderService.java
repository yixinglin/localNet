package org.hsgt.order.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.order.BO.Order;
import org.hsgt.order.domain.OrderDO;

public interface IOrderService extends IService<OrderDO> {
    Order getDetailsBySerialNumber(String id);

    OrderDO getBySerialNumber(String serialNumber);


}
