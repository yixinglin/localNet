package org.hsgt.pricing.domain;
import lombok.Data;

import java.io.Serializable;

@Data
public class Offer implements Serializable {
    private String id;      // primary key
    private float price;
    private String datetime;
    private float lowestPrice;  // manually update
    private String productName;
    private String productKey;
    private int amount;
    private int quantity;    // to update
    private String unit;
    private String note;    // manually update
    private String manufacturer;
    private ShippingGroup shippingGroup;
    private String platform;
    private Boolean active;

}
