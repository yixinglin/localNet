package org.hsgt.order.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.order.domain.AddressDO;
import org.hsgt.order.mapper.AddressMapper;
import org.hsgt.order.services.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends ServiceImpl<AddressMapper, AddressDO> implements IAddressService {

    @Override
    public boolean saveOrUpdate(AddressDO entity) {
        return super.saveOrUpdate(entity);
    }
}
