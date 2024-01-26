package org.hsgt.pricing.servicesV2.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.domain.ConfigureDO;
import org.hsgt.pricing.enu.StrategyType;
import org.hsgt.pricing.mapper.ConfigureMapperMP;
import org.hsgt.pricing.servicesV2.IOfferService;
import org.hsgt.pricing.servicesV2.IPricingConfigureService;
import org.hsgt.pricing.strategy.Strategy;
import org.hsgt.pricing.strategy.TotalPriceStrategy;
import org.hsgt.pricing.strategy.UnitPriceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PricingConfigureServiceImpl extends ServiceImpl<ConfigureMapperMP, ConfigureDO> implements IPricingConfigureService {

    @Autowired
    IOfferService offerService;

    Logger logger = Logger.loggerBuilder(PricingConfigureServiceImpl.class);

    @Override
    public List<Configure> listDetails(Wrapper<ConfigureDO> queryWrapper) {
        List<ConfigureDO> configureDOList = super.list(queryWrapper);
        List<Configure> ans = configureDOList.stream()
                .map(cdo -> convertToConfigureBO(cdo))
                .collect(Collectors.toList());
        return ans;
    }

    @Override
    public Configure getDetailsById(Serializable id) {
        ConfigureDO configureDO = super.getById(id);
        return convertToConfigureBO(configureDO);
    }

    @Override
    public boolean updateConfigureDetailsById(Configure entity) {
        ConfigureDO cdo = convertToConfigureDO(entity);
        return super.updateById(cdo);
    }

    @Override
    public boolean updateListConfigureById(Collection<Configure> entityList) {
        List<ConfigureDO> dos = entityList.stream().map(o -> convertToConfigureDO(o)).collect(Collectors.toList());
        return super.updateBatchById(dos);
    }

    private Configure convertToConfigureBO(ConfigureDO cdo) {
        String productId = cdo.getProductId();
        String strategyStr = cdo.getStrategy();
        Offer offer = offerService.getByIdDetails(productId);

        Configure configure = new Configure();
        configure.setOffer(offer);
        configure.setEnabled(cdo.getEnabled());
        Strategy strategy = null;
        switch (StrategyType.valueOf(strategyStr)) {
            case TotalPriceStrategy:
                strategy = new TotalPriceStrategy(cdo.getReduce(), cdo.getMaxAdjust());
                break;
            case UnitPriceStrategy:
                strategy = new UnitPriceStrategy(cdo.getReduce(), cdo.getMaxAdjust());
        }
        configure.setStrategy(strategy);
        return configure;
    }

    private static ConfigureDO convertToConfigureDO(Configure item) {
        if (item == null) {
            return null;
        }
        ConfigureDO result = new ConfigureDO();
        result.setProductId(item.getOffer().getId());
        result.setEnabled(item.isEnabled());
        result.setStrategy(item.getStrategy().getId());
        result.setMaxAdjust(item.getStrategy().getMaxAdjust());
        result.setReduce(item.getStrategy().getReduce());
        return result;
    }


}
