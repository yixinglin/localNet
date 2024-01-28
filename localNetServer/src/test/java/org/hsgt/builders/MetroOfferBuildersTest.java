package org.hsgt.builders;

import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.domain.ShippingGroupDO;
import org.hsgt.pricing.rest.builders.metro.MetroProductPageBuilderV2;
import org.hsgt.pricing.rest.builders.metro.MetroShippingGroupBuilderV2;
import org.hsgt.pricing.rest.common.SellerApi;
import org.junit.jupiter.api.Test;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MetroOfferBuildersTest {
    @Autowired
    SellerApi metroSellerApi;

    @Test
    public void testShippingGroupBuilder() {
        HttpResponse httpResponse = metroSellerApi.selectShippingGroupById("2150d27b-7c08-4699-a04c-ace84fb38750");
        MetroShippingGroupBuilderV2 shippingGroupBuilder = new MetroShippingGroupBuilderV2();
        ShippingGroupDO shippingGroupDO = shippingGroupBuilder.fromJson(httpResponse.getContent()).build();
        System.out.println(shippingGroupDO);

    }

    @Test
    public void testProductPage() {
        HttpResponse httpResponse = metroSellerApi.selectProductPageById("bf7d06f7-8b28-41ae-8870-33a66f735c64");
        MetroProductPageBuilderV2 shippingGroupBuilderV2 =
                new MetroProductPageBuilderV2(metroSellerApi.accountName());
        ProductPage productPage = shippingGroupBuilderV2
                .pageInfo(httpResponse.getContent())
                .sellers(httpResponse.getContent()).build();
        System.out.println(productPage);
    }

}
