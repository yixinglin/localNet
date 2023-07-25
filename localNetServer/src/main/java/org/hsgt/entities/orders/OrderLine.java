package org.hsgt.entities.orders;

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
}
