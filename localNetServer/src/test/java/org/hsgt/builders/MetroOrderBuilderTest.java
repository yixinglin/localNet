package org.hsgt.builders;

import org.hsgt.order.rest.builders.MetroOrderBuilder;
import org.hsgt.order.rest.metro.SellerApi;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class MetroOrderBuilderTest {
    @Autowired
    SellerApi metroOrderSellerApi;
    @Test
    public void testOrderBuilder() {
        HttpResponse httpResponse = metroOrderSellerApi.selectOrderById("01c1228a-f80d-466f-8332-e38793e4e99d");
        String json = httpResponse.getContent();
        MetroOrderBuilder builder = new MetroOrderBuilder();
        Map jm = new JSONObject(json).toMap();
        builder.parseOrder(jm).parseOrderLines(jm).buildOrderLines();

        System.out.println("");
    }

}
