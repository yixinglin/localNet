package org.hsgt.pricing.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.pricing.BO.*;
import org.hsgt.pricing.domain.CompetitionDO;
import org.hsgt.pricing.domain.OfferDO;
import org.hsgt.pricing.domain.ShippingGroupDO;
import org.hsgt.pricing.mapper.CompetitionMapperMP;
import org.hsgt.pricing.rest.builders.metro.MetroProductPageBuilderV2;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.services.ICompetitionService;
import org.hsgt.pricing.services.IOfferService;
import org.hsgt.pricing.services.IPricingConfigureService;
import org.hsgt.pricing.services.IShippingGroupService;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionMapperMP, CompetitionDO> implements ICompetitionService {

    @Autowired
    IOfferService offerService;
    @Autowired
    IShippingGroupService shippingGroupService;
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

    @Override
    public boolean saveProductPageByApi(String id) {
        // Get product page data from API
        SellerApi api = metroSellerApi;
        HttpResponse httpResponse = api.selectProductPageById(id);
        String content = httpResponse.getContent();
        MetroProductPageBuilderV2 builderV2 = new MetroProductPageBuilderV2(api.accountName());
        ProductPage productPage = builderV2.pageInfo(content).sellers(content).build();
        AtomicInteger index = new AtomicInteger();
        List<Competitor> competitionList = productPage.getCompetitors();
        competitionList.stream().forEach(c -> c.setRank(index.getAndIncrement()));
        List<CompetitionDO> competitionDOS = competitionList.stream().map(competitor -> {
            CompetitionDO cdo = Competitor.convertToCompetitionDO(competitor);
            return cdo;
            }
        ).collect(Collectors.toList());
        // Check if you are in the seller list.
        long count = competitionDOS.stream()
                .filter(c -> c.getShopName().equals(metroSellerApi.accountName()))
                .count();
        if (count == 0)
            throw new RuntimeException("Your product is NOT in a competition! " + productPage.getId());

        // Number of sellers of this product
        int numSeller0 = productPage.getCompetitors().size(); // Number of sellers acquired from API
        // Clean the data first
        LambdaUpdateWrapper<CompetitionDO> luWrapper = new LambdaUpdateWrapper<>();
        luWrapper.eq(CompetitionDO::getProductId, productPage.getCode());
        remove(luWrapper);
        // Save sellers to database
        competitionDOS.stream().forEach(c -> this.saveOrUpdateByMultiId(c));
        // Save or update shipping groups to database
        List<ShippingGroupDO> shippingGroupList = competitionList.stream()
                .map(c -> ShippingGroup.convertToDO(c.getShippingGroup()))
                .collect(Collectors.toList());
        shippingGroupService.saveOrUpdateBatch(shippingGroupList);
        return true;
    }

    private Wrapper<CompetitionDO> getMultiIdUpdateWrapper(CompetitionDO entity) {
        LambdaUpdateWrapper<CompetitionDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CompetitionDO::getProductId, entity.getProductId())
                .eq(CompetitionDO::getShopName, entity.getShopName());
        return wrapper;
    }

    public boolean saveOrUpdateByMultiId(CompetitionDO entity) {
        CompetitionMapperMP competitionMapperMP = getBaseMapper();
        competitionMapperMP.recoverById(entity.getProductId());
        Wrapper<CompetitionDO> wrapper = getMultiIdUpdateWrapper(entity);
        entity.setDatetime(LocalDateTime.now());
        return super.update(entity, wrapper) || super.save(entity);
    }


}
