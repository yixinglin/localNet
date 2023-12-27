package org.hsgt.order.controllers.response;

import lombok.Data;
import org.hsgt.order.domain.Invoice;

import java.io.Serializable;

@Data
public class InvoiceResponse implements Serializable {
    String channel;
    String orderId;
    String date;
    Invoice invoice;
    String file;
}
