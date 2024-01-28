package org.hsgt.order.services;

import org.hsgt.order.BO.Invoice;

public interface OrderServiceOld {

    String createParcelLabel(Invoice invoice);
    String createDeliveryNote(Invoice invoice, String lang);
    String createInvoice(Invoice invoice, String lang);


}
