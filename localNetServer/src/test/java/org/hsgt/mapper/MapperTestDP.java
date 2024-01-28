package org.hsgt.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hsgt.pricing.domain.*;
import org.hsgt.pricing.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MapperTestDP {

    @Autowired
    OfferMapperMP offerMapperMP;
    @Autowired
    ConfigureMapperMP configureMapperMP;
    @Autowired
    CompetitionMapperMP competitionMapperMP;
    @Autowired
    PricingHistoryMapperMP pricingHistoryMapperMP;
    @Autowired
    ShippingGroupMapperMP shippingGroupMapperMP;


    @Test
    public void testOfferMapper() {
        OfferDO offerDO = offerMapperMP.selectById("AAA0000857861");
        System.out.println(offerDO);
    }

    @Test
    public void testSelection() {
        OfferDO offerDO = offerMapperMP.selectById("AAA0000857861");
        System.out.println(offerDO);
        ConfigureDO configureDO = configureMapperMP.selectById("AAA0000863160");
        System.out.println(configureDO);
        QueryWrapper<PricingHistoryDO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("productId", "AAA0000982683");
        List<PricingHistoryDO> pricingHistoryDos = pricingHistoryMapperMP.selectList(queryWrapper);
        System.out.println(pricingHistoryDos);
        ShippingGroupDO shippingGroupDO = shippingGroupMapperMP.selectById("395553e6-a712-455d-a6fc-c7f88c3fd908");
        System.out.println(shippingGroupDO);

        QueryWrapper<CompetitionDO> competitionDOQueryWrapper = new QueryWrapper<>();
        competitionDOQueryWrapper.eq("productId", "AAA0000161574")
                                .or()
                                .eq("shopName", "Novaprax GmbH");
        List<CompetitionDO> competitionDOS = competitionMapperMP.selectList(competitionDOQueryWrapper);
        System.out.println(competitionDOS);
    }


}
