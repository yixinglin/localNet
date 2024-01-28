package org.hsgt.pricing.services;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.domain.OfferDO;

import java.io.Serializable;
import java.util.List;

public interface IOfferService extends IService<OfferDO> {
    public List<Offer> listDetails(Wrapper<OfferDO> queryWrapper);

    public List<Offer> listDetails(Wrapper<OfferDO> queryWrapper, List<String> filterKeywords);
    public Offer getByIdDetails(Serializable id);

    /**
     * @param :
     * @return boolean
     * @author Lin
     * @description Fetch data from API and save it to database.
     * @date 26.Jan.2024 026 21:08
     */
    boolean saveOrUpdateByApi();
}
