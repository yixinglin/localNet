package org.hsgt.mapper;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.core.domain.SystemConfigure;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.BO.UpdatedOffer;
import org.hsgt.pricing.mapper.ConfigureMapper;
import org.hsgt.pricing.mapper.OfferMapper;
import org.hsgt.core.mapper.SystemConfigureMapper;
import org.hsgt.pricing.strategy.Strategy;
import org.hsgt.pricing.strategy.TotalPriceStrategy;
import org.junit.jupiter.api.Assertions;
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


    public void testSystemConfigureMapper() {
        SystemConfigure pricingInterval = systemConfigureMapper.selectById("pricingInterval");
        SystemConfigure requestInterval = systemConfigureMapper.selectById("requestInterval");
        System.out.println(pricingInterval.getValue());
        System.out.println(requestInterval.getValue());

        Assertions.assertEquals(pricingInterval.getValue(), "1200", "pricingInterval");
        Assertions.assertEquals(requestInterval.getValue(), "3", "requestInterval");
    }


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


    public void testUpdatedOffer() {
        UpdatedOffer updatedOffer = new UpdatedOffer();
        Offer offer = offerMapper.selectById("AAA0000718012");
        updatedOffer.setOffer(NewOffer.from(offer));
        updatedOffer.setIp("1.2.3.4");
        updatedOffer.setNote("Testing");
        offerMapper.insertUpdatedPricingLog(updatedOffer);
    }
}
