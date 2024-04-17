package org.hsgt.pricing.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.hsgt.core.mapper.SqlService;
import org.hsgt.pricing.BO.Competitor;
import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.domain.*;
import org.hsgt.pricing.enu.StrategyType;
import org.hsgt.pricing.mapper.*;
import org.hsgt.pricing.rest.builders.metro.MetroOfferBuilderV2;
import org.hsgt.pricing.rest.builders.metro.MetroProductPageBuilderV2;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.schedule.Cache;
import org.hsgt.pricing.services.ICompetitionService;
import org.hsgt.pricing.services.IRestPricingService;
import org.hsgt.pricing.services.IShippingGroupService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.JSON;
import org.utils.JwtsUtils;
import org.utils.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RestPricingService implements IRestPricingService {
    Logger logger = Logger.loggerBuilder(RestPricingService.class);
    @Autowired
    SellerApi metroOfferSellerApi;
    @Autowired
    OfferServiceImpl offerService;
    @Autowired
    ShippingGroupMapperMP shippingGroupMapperMP;
    @Autowired
    ConfigureMapperMP configureMapperMP;

    @Autowired
    ICompetitionService competitionService;
    @Autowired
    IShippingGroupService shippingGroupService;
    @Autowired
    private MetroPricingConfig pricingConfig;
    @Autowired
    private PricingHistoryMapperMP pricingHistoryMapperMP;

    /**
     * @return boolean
     * @author Lin
     * @description TODO Get offer data via api and save it to database.
     * @date 14.Jan.2024 014 14:23
     */
    @Override
    public boolean saveOrUpdateOfferByMetroAPI() {
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

        List<OfferDO> savedOffers = this.saveListOfferToDatabase(offerDOS);
        if (savedOffers == null) {
            return false;
        } else {
            return offerService.updateBatchById(savedOffers);
        }
    }

    private List<OfferDO> saveListOfferToDatabase(List<OfferDO> offerDOS) {
        // Delete all offers;
        int n = offerService.getBaseMapper().delete(null);
        // Initialize and update to database
        offerDOS.stream().forEach(odo-> initBeforeSaveToDatabase(odo));
        // Initialize lowestPrice and amount and save it to database
        List<String> ids = offerDOS.stream().map(o -> o.getProductId()).collect(Collectors.toList());
        if (ids == null || ids.size() == 0) {
            return null;
        }

        List<OfferDO> savedOffers = offerService.listByIds(ids);
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
        return savedOffers;
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
        OfferMapperMP offerMapperMP = offerService.getBaseMapper();
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



    @Override
    public boolean saveOrUpdateProductPageByMetroAPI(String id) {

        // Get product page data from API
        SellerApi api = metroOfferSellerApi;
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
                .filter(c -> c.getShopName().equals(api.accountName()))
                .count();
        if (count == 0)
            throw new RuntimeException("Your product is NOT in a competition! " + productPage.getId());



        // Number of sellers of this product
        int numSeller0 = productPage.getCompetitors().size(); // Number of sellers acquired from API
        // Clean the data first
        LambdaUpdateWrapper<CompetitionDO> luWrapper = new LambdaUpdateWrapper<>();
        luWrapper.eq(CompetitionDO::getProductId, productPage.getCode());
        competitionService.remove(luWrapper);
        // Save sellers to database
        competitionDOS.stream().forEach(c -> this.saveOrUpdateByMultiId(c));
        // Save or update shipping groups to database
        List<ShippingGroupDO> shippingGroupList = competitionList.stream()
                .map(c -> ShippingGroup.convertToDO(c.getShippingGroup()))
                .collect(Collectors.toList());
        shippingGroupService.saveOrUpdateBatch(shippingGroupList);
        return true;
    }

    @Override
    public boolean saveOrUpdateShippingGroupListByMetroApi() {
        ShippingGroupMapperMP shippingGroupMapperMP = (ShippingGroupMapperMP) shippingGroupService.getBaseMapper();
        SellerApi api = metroOfferSellerApi;
        HttpResponse httpResponse = api.selectAllShippingGroups();
        String content = httpResponse.getContent();
        JSON jp = new JSON(content);
        List<Map> items = jp.read("$.shippingGroups", List.class);
        List<ShippingGroupDO> sgdo = items.stream().map(m -> {
            JSON jp0 = new JSON(m);
            ShippingGroupDO sg = new ShippingGroupDO();
            sg.setPlatform("metro");
            sg.setOwner(api.accountName());
            sg.setName(jp0.read("$.shippingGroupName"));
            sg.setId(jp0.read("$.shippingGroupId"));
            shippingGroupMapperMP.recoverById(sg.getId());
            return sg;
        }).collect(Collectors.toList());

        // Todo Save to database
        return shippingGroupService.saveOrUpdateBatch(sgdo);
    }

    // Todo 测试
    @Override
    public NewOffer pricingViaMetroAPI(NewOffer latestOffer, JSONArray offerList, String ip) {
        boolean allowActualPricing = pricingConfig.isAllowActualPricing();
        boolean isAllowActualPricing = pricingConfig.isAllowActualPricing();
        SellerApi api = metroOfferSellerApi;
        String shippingGroupId = latestOffer.getShippingGroupId();

        if (offerList == null) {
            String s = api.selectAllOffers().getContent();
            offerList = (new JSONObject(s)).getJSONArray("items");
        }

        // Update price via API
        HttpResponse response;
        if (allowActualPricing) {
            this.logger.info("Actual Pricing: " + latestOffer);
            response = api.updateOfferById(latestOffer, offerList, true);
        } else {
            this.logger.info("@@ Virtual Pricing: " + latestOffer);
            response = api.updateOfferById(latestOffer, offerList, false);
        }
        if (response.getStateCode() != 200) {
            throw new RuntimeException("Fail to update offer. " + latestOffer.toString() + response.toString());
        }

        // Add pricing log to database
        PricingHistoryDO pricingHistoryDo = new PricingHistoryDO();
        pricingHistoryDo.setPrice(latestOffer.getPrice());
        pricingHistoryDo.setIp(ip);
        pricingHistoryDo.setQuantity(latestOffer.getQuantity());
        pricingHistoryDo.setShippingGroupId(latestOffer.getShippingGroupId());

        String username;
        try {
            username = JwtsUtils.verify(latestOffer.getToken()).getSubject();
            pricingHistoryDo.setUsername(username);
        } catch (Exception e) {
            pricingHistoryDo.setUsername(ip);
        }
        if (isAllowActualPricing)
            pricingHistoryDo.setNote("Actual");
        else
            pricingHistoryDo.setNote("Virtual");

        pricingHistoryMapperMP.insert(pricingHistoryDo);
        return latestOffer;
    }

    private Wrapper<CompetitionDO> getMultiIdUpdateWrapper(CompetitionDO entity) {
        LambdaUpdateWrapper<CompetitionDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CompetitionDO::getProductId, entity.getProductId())
                .eq(CompetitionDO::getShopName, entity.getShopName());
        return wrapper;
    }

    public boolean saveOrUpdateByMultiId(CompetitionDO entity) {
        CompetitionMapperMP competitionMapperMP = (CompetitionMapperMP) competitionService.getBaseMapper();
        competitionMapperMP.recoverById(entity.getProductId());
        Wrapper<CompetitionDO> wrapper = getMultiIdUpdateWrapper(entity);
        entity.setDatetime(LocalDateTime.now());
        return competitionService.update(entity, wrapper) || competitionService.save(entity);
    }

}
