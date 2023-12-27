package org.hsgt.order.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Finance implements Serializable {
    float totalNetPrice;
    float tax;
    float totalPrice;
}
