package org.hsgt.order.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.order.BO.OrderLine;
import org.hsgt.order.domain.OrderLineDO;

import java.util.List;

public interface IOrderLineService extends IService<OrderLineDO> {

    List<OrderLineDO> getByOrderId(Long id);

    List<OrderLine> getDetailsByOrderId(Long id);
}
