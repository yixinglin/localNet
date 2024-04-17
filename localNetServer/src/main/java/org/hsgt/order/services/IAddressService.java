package org.hsgt.order.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.order.BO.BillingAddress;
import org.hsgt.order.BO.ShippingAddress;
import org.hsgt.order.domain.AddressDO;

public interface IAddressService extends IService<AddressDO> {
    BillingAddress getBillingAddressDetailsById(Long billingAddrId);

    ShippingAddress getShippingAddressDetailsById(Long shippingAddrId);
}
