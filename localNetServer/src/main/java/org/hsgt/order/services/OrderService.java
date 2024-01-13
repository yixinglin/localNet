package org.hsgt.order.services;

import org.hsgt.order.domain.Invoice;

public interface OrderService {

    String createParcelLabel(Invoice invoice);
    String createDeliveryNote(Invoice invoice, String lang);
    String createInvoice(Invoice invoice, String lang);


}
