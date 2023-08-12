package org.hsgt.controllers.response;

import lombok.Data;
import org.hsgt.entities.orders.Invoice;

import java.io.Serializable;

@Data
public class InvoiceResponse implements Serializable {
    String channel;
    String orderId;
    String date;
    Invoice invoice;
    String file;
}
