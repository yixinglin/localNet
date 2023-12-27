package org.hsgt.order.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeliveryNote {
    String title;
    Date date;
    String deliveryNumber;
    String invoiceId;
    String logo;
    BankAccount sellerBankAccount;
    ShippingAddress buyerAddress;
    List<OrderLine> orderLines;
    OrderLine shippingCost;
    String comment;
}
