package org.hsgt.order.domain;
import lombok.Data;
import org.hsgt.order.domain.e.OrderStatus;
import java.util.Date;

@Data
public class Order {
    String id;
    String orderNumber;
    OrderStatus orderStatus;
    Date createAt;
    Date updateAt;
    String phone;
    String email;
    DeliveryNote deliveryNote;
    Invoice invoice;
}
