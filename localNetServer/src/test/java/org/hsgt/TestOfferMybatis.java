package org.hsgt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hsgt.pricing.domain.ShippingGroup;
import org.hsgt.pricing.domain.Offer;
import org.hsgt.pricing.mapper.OfferMapper;
import org.hsgt.pricing.mapper.ShippingGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestOfferMybatis {

//    @Autowired
//    OfferService offerService;

    @Autowired
    OfferMapper offerMapper;
    @Autowired
    ShippingGroupMapper shippingGroupMapper;

    public void testMyBatis() {

        QueryWrapper<Offer> oWrapper = new QueryWrapper<>();
        oWrapper.eq("note", "CLUNGENE (selbst) 5er");
        List<Offer> offers = offerMapper.selectList(null);
        Offer offer = offerMapper.selectById("AAA0000863162");
        System.out.println(offers);

        Offer offer3 = offers.get(0);
        offer3.setId("AAAAAAAAAAAAA");
        offer3.setNote("SSSSSSSSSS");
        offer3.setProductName("B");
        // offerMapper.insert(offer3);
        offerMapper.updateById(offer3);

//        List<Offer> offers2 = offerService.list();
//        System.out.println(offerService.count());
//        offer3 = offers2.get(0);
//        offer3.setId("AAAAAAAAAAAAA");
//        offerMapper.updateById(offer3);
//        offerService.saveOrUpdate(offer3);
//        offer3.setProductKey("SDSASDASD");
//        offer3.setLowestPrice(200.2f);
//        offerService.saveOrUpdate(offer3);
//        offerService.removeById(offer3);

        List<ShippingGroup> shippingGroups =  shippingGroupMapper.selectList(null);
        ShippingGroup shippingGroup = shippingGroupMapper.selectById("49221d33-79a1-4488-8e96-add6db70d122");

 //       System.out.println(shippingGroups.get(0));
    }

}
