package org.hsgt.order.BO;

import lombok.Data;
import org.hsgt.order.domain.OrderLineDO;

import java.io.Serializable;

@Data
public class OrderLine implements Serializable {
    String title;
    int quantity;
    float taxRate;
    float tax;
    float netPrice;
    float grossPrice;
    String currency;
    String ean;
    String sku;
    String unit;


    public static OrderLine convertToBO(OrderLineDO item) {
        if (item == null) {
            return null;
        }
        OrderLine result = new OrderLine();
        result.setTitle(item.getTitle());
        result.setQuantity(item.getQuantity());
        result.setTaxRate(item.getTaxRate());
        result.setTax(item.getTax());
        result.setNetPrice(item.getNetPrice());
        result.setGrossPrice(item.getGrossPrice());
        result.setCurrency(item.getCurrency());
        result.setEan(item.getEan());
        result.setSku(item.getSku());
        result.setUnit(item.getUnit());
        return result;
    }
}
