package org.hsgt.pricing.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.pricing.BO.Competitor;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.domain.CompetitionDO;
import org.hsgt.pricing.domain.OfferDO;
import org.hsgt.pricing.mapper.CompetitionMapperMP;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.services.ICompetitionService;
import org.hsgt.pricing.services.IOfferService;
import org.hsgt.pricing.services.IPricingConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionMapperMP, CompetitionDO> implements ICompetitionService {

    @Autowired
    IOfferService offerService;
    @Autowired
    IPricingConfigureService pricingConfigureService;
    @Autowired
    SellerApi metroSellerApi;

    Logger logger = Logger.loggerBuilder(CompetitionServiceImpl.class);

    public List<Competitor> listDetails(Wrapper<CompetitionDO> queryWrapper) {
        List<CompetitionDO> competitionDOList = super.list(queryWrapper);
        List<Competitor> ans = null;
        ans = competitionDOList.stream()
                .map(competitionDO -> {
                    Competitor competitor = Competitor.convertToCompetitor(competitionDO);
                    Offer offer = offerService.getByIdDetails(competitionDO.getProductId());
                    competitor.setShippingGroup(offer.getShippingGroup());
                    return competitor;
                })
                .collect(Collectors.toList());
        return ans;
    }

    public List<ProductPage> listProductPageDetailsByIds(Collection<? extends Serializable> idList) {
        List<OfferDO> offerDOS = this.offerService.getBaseMapper().selectBatchIds(idList);
        List<ProductPage> ans = offerDOS.stream().map(odo -> {
            ProductPage page = new ProductPage();
            Configure configure = this.pricingConfigureService.getDetailsById(odo.getProductId());
            LambdaQueryWrapper<CompetitionDO> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(CompetitionDO::getProductId, odo.getProductId());
            List<Competitor> sellers = listDetails(lambdaQueryWrapper);
            configure.getStrategy().sort(sellers);
            page.setCompetitors(sellers);
            page.setId(odo.getProductKey());
            page.setCode(odo.getProductId());
            page.setProductName(odo.getProductName());
            page.setManufacturer(odo.getManufacturer());
            try {
                Competitor self = sellers.stream()
                        .filter(o -> o.getShopName().equals(metroSellerApi.accountName()))
                        .findFirst().get();
                page.setSelf(self);
            } catch (NoSuchElementException e) {
                throw new RuntimeException("Your are not in the competition list " + page.getId());
            }
            return page;
        }).collect(Collectors.toList());
        return ans;
    }


    @Override
    public ProductPage getProductPageDetailsById(Serializable id) {
        List<ProductPage> productPages = this.listProductPageDetailsByIds(Arrays.asList(id));
        if (productPages != null && productPages.size() > 0) {
            return productPages.get(0);
        } else {
            return null;
        }
    }

}
