package org.hsgt.entities.orders;

import lombok.Data;

import java.io.Serializable;

@Data
public class Finance implements Serializable {
    float totalNetPrice;
    float tax;
    float totalPrice;
}
