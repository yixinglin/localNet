package org.hsgt.builders;

import org.hsgt.api.MetroMockSellerApi;
import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;
import org.hsgt.builders.metro.MetroInvoiceBuilder;
import org.hsgt.entities.orders.Invoice;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class MetroInvoiceBuilderTest {

    @Test
    public void parseInvoiceTest() {
        SellerApi api = SellerApiFactory.createSellerApi(SellerApi.METRO_MOCKED, "");
        String s = api.selectOrderById("6cf37f89-3534-44c5-b6d5-83d2a61e0719").getContent();
        // System.out.println(s);
        MetroInvoiceBuilder builder = new MetroInvoiceBuilder();
        Invoice invoice = builder.parse(new JSONObject(s)).financing().build() ;
        System.out.println(invoice);
    }


}
