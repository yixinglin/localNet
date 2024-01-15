package org.hsgt.pricing.servicesV2;

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

    boolean saveOrUpdateByApi();
}
