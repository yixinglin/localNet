package org.hsgt.order.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.order.domain.BuyerDO;
import org.hsgt.order.mapper.BuyerMapper;
import org.hsgt.order.services.IBuyer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class BuyerService extends ServiceImpl<BuyerMapper, BuyerDO> implements IBuyer {

    @Override
    public boolean saveOrUpdate(BuyerDO entity) {
        String code = entity.getCode();
        assert code != null: "code cannot be empty!";
        LambdaUpdateWrapper<BuyerDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BuyerDO::getCode, code);
        return update(entity, wrapper) || save(entity);
    }

    @Override
    public BuyerDO getByCode(Serializable code) {
        LambdaQueryWrapper<BuyerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuyerDO::getCode, code);
        List<BuyerDO> list = super.list(wrapper);
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
