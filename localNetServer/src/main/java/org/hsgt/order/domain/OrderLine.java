package org.hsgt.order.domain;

import lombok.Data;

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
    String unit;
}
