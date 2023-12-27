package org.hsgt.order.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Invoice implements Serializable {
    String title;
    Date date;
    String invoiceNumber;
    String invoiceId;
    String logo;
    BillingAddress sellerAddress;
    BankAccount sellerBankAccount;
    BillingAddress buyerAddress;
    List<OrderLine> orderLines;
    OrderLine shippingCost;
    Finance finance;
    String comment;
}
