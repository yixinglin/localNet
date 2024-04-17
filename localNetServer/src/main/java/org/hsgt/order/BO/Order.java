package org.hsgt.order.BO;

import lombok.Data;
import org.hsgt.order.domain.OrderDO;
import org.hsgt.order.enu.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    String orderNumber;
    OrderStatus status;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    Buyer buyer;
    List<OrderLine> orderLines;
    ShippingAddress shippingAddress;
    BillingAddress billingAddress;


    public static Order convertToBO(OrderDO item) {
        if (item == null) {
            return null;
        }
        Order result = new Order();
        result.setOrderNumber(item.getSerialNumber());
        result.setStatus(OrderStatus.fromVal(item.getStatus()));
        result.setCreateAt(item.getCreateAt());
        result.setUpdateAt(item.getUpdateAt());
        return result;
    }

}
