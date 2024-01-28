package org.hsgt.pricing.services;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.domain.ShippingGroupDO;

import java.io.Serializable;
import java.util.List;

public interface IShippingGroupService extends IService<ShippingGroupDO> {
    List<ShippingGroup> listAllLogicExistDetails(Wrapper<ShippingGroupDO> queryWrapper);
    List<ShippingGroup> listMyCurrentActivatedShippingGroups();

    /**
     * @param id:
     * @return ShippingGroup
     * @author Lin
     * @description Get details of shipping group by id.
     * @date 15.Jan.2024 015 00:05
     */
    ShippingGroup getByIdDetails(Serializable id);

    /**
     * @param entity:
     * @return boolean
     * @author Lin
     * @description Save details of a shipping group to the database.
     * @date 15.Jan.2024 015 00:04
     * @deprecated Not supported anymore due to the Metro RestAPI
     */
    boolean saveOrIgnore(ShippingGroupDO entity);

    /**
     * @return boolean
     * @author Lin
     * @description Get data from Api and save it to database.
     *  Note that the shipping groups not included in the response
     *  will be logically deleted.
     * @date 15.Jan.2024 015 01:39
     */
    boolean saveOrUpdateBatchByApi();

    /*
     * @param id: Shipping group id
      * @return boolean
     * @description Get shipping group by id via API.
     */
    boolean saveOrUpdateByApi(String id);

}
