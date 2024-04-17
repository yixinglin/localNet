package org.hsgt.pricing.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.domain.ShippingGroupDO;
import org.hsgt.pricing.mapper.ShippingGroupMapperMP;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.services.IShippingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShippingGroupServiceImpl  extends ServiceImpl<ShippingGroupMapperMP, ShippingGroupDO>  implements IShippingGroupService {

    @Autowired
    private SellerApi metroOfferSellerApi;

    Logger logger = Logger.loggerBuilder(ShippingGroupServiceImpl.class);

    @Override
    public List<ShippingGroup> listAllLogicExistDetails(Wrapper<ShippingGroupDO> queryWrapper) {
        List<ShippingGroupDO> shippingGroupDOList = super.list(queryWrapper);
        List<ShippingGroup> ans;
        ans = shippingGroupDOList.stream()
                .map(sgdo -> convertToShippingGroup(sgdo))
                .collect(Collectors.toList());
        return ans;
    }

    @Override
    public List<ShippingGroup> listMyCurrentActivatedShippingGroups() {
        LambdaQueryWrapper<ShippingGroupDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingGroupDO::getOwner, metroOfferSellerApi.accountName());
        return listAllLogicExistDetails(wrapper);
    }

    @Override
    public ShippingGroup getByIdDetails(Serializable id) {
        ShippingGroupDO shippingGroupDO = super.getById(id);
        return convertToShippingGroup(shippingGroupDO);
    }

    @Override
    public boolean saveOrIgnore(ShippingGroupDO entity) {
        if (null != entity) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
            Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
            return StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal)) ? save(entity) : true;
        }
        return false;
    }


    private ShippingGroupDO convertToShippingGroupDO(ShippingGroup shippingGroup) {
        return ShippingGroup.convertToDO(shippingGroup, false);
    }

    private ShippingGroup convertToShippingGroup(ShippingGroupDO sgdo) {
        return ShippingGroup.convertToBO(sgdo);
    }
}
