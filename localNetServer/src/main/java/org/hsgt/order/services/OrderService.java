package org.hsgt.order.services;

import org.hsgt.order.domain.Invoice;

public interface OrderService {

    public String createParcelLabel(Invoice invoice);
    public String createDeliveryNote(Invoice invoice, String lang);
    public String createInvoice(Invoice invoice, String lang);


}
