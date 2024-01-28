package org.hsgt.order.BO;

import lombok.Data;
import org.hsgt.order.enu.OrderStatus;

import java.util.Date;
import java.util.List;

@Data
public class Order {
    String orderNumber;
    OrderStatus status;
    Date createAt;
    Date updateAt;
    Buyer buyer;

    List<OrderLine> orderLines;
    // DeliveryNote deliveryNote;
    // Invoice invoice;
    ShippingAddress shippingAddress;
    BillingAddress billingAddress;


}
