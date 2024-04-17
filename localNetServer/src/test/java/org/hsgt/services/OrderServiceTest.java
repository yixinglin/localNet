package org.hsgt.services;

import org.hsgt.order.BO.Order;
import org.hsgt.order.services.impl.OrderService;
import org.hsgt.order.services.impl.RestOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    RestOrderService restOrderService;
    @Autowired
    OrderService orderService;

    @Test
    public void test() {
        restOrderService.saveOrUpdateByMetroAPI("1d3961f5-a6a6-4877-a9cd-38b7171dcbaf");
        Order order = orderService.getDetailsBySerialNumber("O22-575464603627");
        System.out.println();
    }
}
