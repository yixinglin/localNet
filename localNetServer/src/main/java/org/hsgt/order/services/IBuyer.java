package org.hsgt.order.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.order.BO.Buyer;
import org.hsgt.order.domain.BuyerDO;

import java.io.Serializable;

public interface IBuyer extends IService<BuyerDO> {
    public BuyerDO getByCode(Serializable code);

    Buyer getDetailsById(Long buyerId);
}
