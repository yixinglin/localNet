package org.hsgt.mapper;


import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.ConfigureMapper;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.strategy.Strategy;
import org.hsgt.strategy.TotalPriceStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConfigureMapperTest {
    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    OfferMapper offerMapper;

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

}
