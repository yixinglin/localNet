package org.hsgt.mapper;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hsgt.entities.common.SystemConfigure;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.entities.pricing.UpdatedOffer;
import org.hsgt.mappers.ConfigureMapper;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.SystemConfigureMapper;
import org.hsgt.strategy.Strategy;
import org.hsgt.strategy.TotalPriceStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    @Autowired
    SystemConfigureMapper systemConfigureMapper;
    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    OfferMapper offerMapper;

    @Test
    public void testSystemConfigureMapper() {
        SystemConfigure pricingInterval = systemConfigureMapper.selectById("pricingInterval");
        SystemConfigure requestInterval = systemConfigureMapper.selectById("requestInterval");
        System.out.println(pricingInterval.getValue());
        System.out.println(requestInterval.getValue());

        Assertions.assertEquals(pricingInterval.getValue(), "1200", "pricingInterval");
        Assertions.assertEquals(requestInterval.getValue(), "3", "requestInterval");
    }

    @Test
    public void testConfigure() {
        Strategy strategy = new TotalPriceStrategy(0.2f, 0.5f);
        Offer offer = offerMapper.selectById("AAA0001029984");
        Configure configure = new Configure();
        configure.setOffer(offer);
        configure.setEnabled(true);
        configure.setStrategy(strategy);
        // SqlService.sqlInsertOrUpdate(configure, configureMapper);
        // configureMapper.insert(configure);
        configureMapper.updateById(configure);
        Configure c = configureMapper.selectById(offer.getId());
        MatcherAssert.assertThat(c, Matchers.notNullValue());
        MatcherAssert.assertThat(c.getStrategy(), Matchers.notNullValue());
        System.out.println(c);
    }

    @Test
    public void testUpdatedOffer() {
        UpdatedOffer updatedOffer = new UpdatedOffer();
        Offer offer = offerMapper.selectById("AAA0000718012");
        updatedOffer.setOffer(offer);
        updatedOffer.setIp("1.2.3.4");
        updatedOffer.setNote("Testing");
        offerMapper.insertUpdatedPricingLog(updatedOffer);
    }
}
