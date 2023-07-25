package org.hsgt;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.ShippingGroupMapper;
import org.hsgt.services.OfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestOfferMybatis {

    @Autowired
    OfferService offerService;

    @Autowired
    OfferMapper offerMapper;
    @Autowired
    ShippingGroupMapper shippingGroupMapper;
    @Test
    public void testMyBatis() {

        List<Offer> offers = offerMapper.selectList(null);
        Offer offer = offerMapper.selectById("AAA0000863162");
        System.out.println(offers);

        List<Offer> offers2 = offerService.list();
        System.out.println(offerService.count());
        Offer offer3 = offers2.get(0);
        offer3.setId("AAAAAAAAAAAAA");
        offerService.saveOrUpdate(offer3);
        offer3.setProductKey("SDSASDASD");
        offer3.setLowestPrice(200.2f);
        offerService.saveOrUpdate(offer3);
        offerService.removeById(offer3);
         List<ShippingGroup> shippingGroups =  shippingGroupMapper.selectList(null);
         ShippingGroup shippingGroup = shippingGroupMapper.selectById("49221d33-79a1-4488-8e96-add6db70d122");

        System.out.println(shippingGroups.get(0));
    }

}
