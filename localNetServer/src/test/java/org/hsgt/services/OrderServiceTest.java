package org.hsgt.services;

import org.hsgt.order.rest.metro.SellerApi;
import org.hsgt.order.services.impl.MetroOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    SellerApi metroOrderSellerApi;
    @Autowired
    MetroOrderService metroOrderService;

    @Test
    public void test() {
        metroOrderService.saveOrUpdateByApi("1d7aa259-df27-45ef-a58c-9991072de595");
        System.out.println();
    }
}
