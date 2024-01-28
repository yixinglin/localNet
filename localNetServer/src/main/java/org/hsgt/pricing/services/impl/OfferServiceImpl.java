package org.hsgt.pricing.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.hsgt.core.mapper.SqlService;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.domain.ConfigureDO;
import org.hsgt.pricing.domain.OfferDO;
import org.hsgt.pricing.domain.ShippingGroupDO;
import org.hsgt.pricing.enu.StrategyType;
import org.hsgt.pricing.mapper.ConfigureMapperMP;
import org.hsgt.pricing.mapper.OfferMapperMP;
import org.hsgt.pricing.mapper.ShippingGroupMapperMP;
import org.hsgt.pricing.rest.builders.metro.MetroOfferBuilderV2;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.schedule.Cache;
import org.hsgt.pricing.services.IOfferService;
import org.jetbrains.annotations.NotNull;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.JSON;
import org.utils.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapperMP, OfferDO> implements IOfferService {
    Logger logger = Logger.loggerBuilder(OfferServiceImpl.class);
    @Autowired
    ShippingGroupMapperMP shippingGroupMapperMP;
    @Autowired
    ConfigureMapperMP configureMapperMP;
    @Autowired
    SellerApi metroOfferSellerApi;

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

    /**
     * @return boolean
     * @author Lin
     * @description TODO Get offer data via api and save it to database.
     * @date 14.Jan.2024 014 14:23
     */

    @Override
    public boolean saveOrUpdateByApi() {
        SellerApi api = metroOfferSellerApi;
        HttpResponse httpResponse = api.selectAllOffers();
        Cache.currentMetroOfferList = httpResponse.getContent();
        this.logger.info("@@ Saved [currentMetroOfferList] to Cache");
        MetroOfferBuilderV2 builder = new MetroOfferBuilderV2();
        JSON jp = new JSON(httpResponse.getContent());
        List<Map> jofferArray = jp.read("$.items", List.class);

        // Parse offer data from json, and save it to database (t_offer, t_configure, t_shippinggroup)
        List<OfferDO> offerDOS = jofferArray.stream().map(js -> {
            OfferDO odo = builder.fromJson(js).build();
            return odo;
        }).collect(Collectors.toList());

        // Filtering offers of quantity 0;
        offerDOS = offerDOS.stream()
                .filter(o -> o.getQuantity() > 0)
                .collect(Collectors.toList());

        // Delete all offers;
        int n = getBaseMapper().delete(null);
        // Initialize and update to database
        offerDOS.stream().forEach(odo-> initBeforeSaveToDatabase(odo));
        // Initialize lowestPrice and amount and save it to database
        List<String> ids = offerDOS.stream().map(o -> o.getProductId()).collect(Collectors.toList());
        if (ids == null || ids.size() == 0) {
            return false;
        }

        List<OfferDO> savedOffers = super.listByIds(ids);
        for (OfferDO so: savedOffers) {
            if (StringUtils.isEmpty(so.getNote())) {
                so.setNote("");
            }
            if (so.getAmount() == null) {
                so.setAmount(0);
            }
            if (so.getLowestPrice() == null) {
                so.setLowestPrice(so.getPrice() * 0.8f);
            }
        }
        return super.updateBatchById(savedOffers);
    }


    private void initBeforeSaveToDatabase(OfferDO offerDO) {
        // Initialize the shipping group and insert it to t_shippinggroup (Offer data does not contain concrete data of shipment).
        String shippingGroupId = offerDO.getShippingGroupId();
        ShippingGroupDO sg = ShippingGroup.convertToDO(ShippingGroup.free());
        sg.setId(shippingGroupId);
        sg.setName("unknown");
        shippingGroupMapperMP.recoverById(sg.getId());
        SqlService.sqlInsertOrSkip(sg, shippingGroupMapperMP);
        // Update or insert offer to database.
        OfferMapperMP offerMapperMP = getBaseMapper();
        offerMapperMP.recoverById(offerDO.getProductId());
        SqlService.sqlInsertOrUpdate(offerDO, offerMapperMP);
        // Initialize the configuration and insert it to t_configure if it does not exist.
        ConfigureDO configureDO = new ConfigureDO();
        configureDO.setProductId(offerDO.getProductId());
        configureDO.setStrategy(StrategyType.TotalPriceStrategy.getVal());
        configureDO.setEnabled(false);
        configureDO.setReduce(0.01f);
        configureDO.setMaxAdjust(0.5f);
        SqlService.sqlInsertOrSkip(configureDO, configureMapperMP);
    }

    private Offer convertToOffer(OfferDO offerDO) {
        Offer offer = Offer.convertToOffer(offerDO);
        ShippingGroupDO sgdo = shippingGroupMapperMP.selectById(offerDO.getShippingGroupId());
        offer.setShippingGroup(new ShippingGroup(sgdo));
        return offer;
    }
}
