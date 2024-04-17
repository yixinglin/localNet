package org.hsgt.order.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.order.BO.BillingAddress;
import org.hsgt.order.BO.ShippingAddress;
import org.hsgt.order.domain.AddressDO;
import org.hsgt.order.enu.AddressType;
import org.hsgt.order.mapper.AddressMapper;
import org.hsgt.order.services.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends ServiceImpl<AddressMapper, AddressDO> implements IAddressService {

    @Override
    public boolean saveOrUpdate(AddressDO entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    public BillingAddress getBillingAddressDetailsById(Long billingAddrId) {
        AddressDO addr = this.getById(billingAddrId);
        assert addr.getType() == AddressType.Billing.getVal(): "This is not a billing address";
        BillingAddress address = BillingAddress.convertToBO(addr);
        return address;
    }

    @Override
    public ShippingAddress getShippingAddressDetailsById(Long shippingAddrId) {
        AddressDO addr = this.getById(shippingAddrId);
        assert addr.getType() == AddressType.Shipping.getVal(): "This is not a shipping address";
        ShippingAddress address = ShippingAddress.convertToBO(addr);
        return address;
    }
}
