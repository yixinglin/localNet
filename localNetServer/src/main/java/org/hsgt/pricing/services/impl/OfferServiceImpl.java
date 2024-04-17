package org.hsgt.pricing.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.domain.OfferDO;
import org.hsgt.pricing.domain.ShippingGroupDO;
import org.hsgt.pricing.mapper.OfferMapperMP;
import org.hsgt.pricing.mapper.ShippingGroupMapperMP;
import org.hsgt.pricing.services.IOfferService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapperMP, OfferDO> implements IOfferService {
    Logger logger = Logger.loggerBuilder(OfferServiceImpl.class);
    @Autowired
    ShippingGroupMapperMP shippingGroupMapperMP;

    public List<Offer> listDetails(Wrapper<OfferDO> queryWrapper) {
        List<OfferDO> offerDOList = super.list(queryWrapper);
        List<Offer> ans = offerDOList.stream()
                .map(fdo -> convertToOffer(fdo))
                .collect(Collectors.toList());
        return ans;
    }

    @Override
    public List<Offer> listDetails(Wrapper<OfferDO> queryWrapper, List<String> filterKeywords) {
        List<Offer> offers = this.listDetails(queryWrapper);
        offers = offers.stream().filter(o -> !excluded(o, filterKeywords))
                .collect(Collectors.toList()); // Exclude offers with the given keywords
        return offers;
    }

    private boolean excluded(Offer offer, @NotNull List<String> filterKeywords) {
        return filterKeywords.stream()
                .filter(s -> offer.getProductName().toLowerCase().contains(s))
                .findFirst().isPresent();
    }

    @Override
    public Offer getByIdDetails(Serializable id) {
        OfferDO offerDO = super.getById(id);
        if (offerDO != null) {
            return convertToOffer(offerDO);
        } else {
            return null;
        }
    }


    private Offer convertToOffer(OfferDO offerDO) {
        Offer offer = Offer.convertToOffer(offerDO);
        ShippingGroupDO sgdo = shippingGroupMapperMP.selectById(offerDO.getShippingGroupId());
        offer.setShippingGroup(new ShippingGroup(sgdo));
        return offer;
    }
}
