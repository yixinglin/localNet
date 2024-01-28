package org.hsgt.order.services.impl;

import org.hsgt.order.config.MetroOrderConfig;
import org.hsgt.order.BO.Invoice;
import org.hsgt.order.services.OrderServiceOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.InvoiceMaker;

@Service
public abstract class BaseOrderService implements OrderServiceOld {

    @Autowired
    protected MetroOrderConfig metroOrderConfig;

    @Override
    public String createParcelLabel(Invoice invoice) {
        return null;
    }

    @Override
    public String createInvoice(Invoice invoice, String lang) {
        InvoiceMaker invoiceMaker = new InvoiceMaker(metroOrderConfig.getTemplateDocFile(),
                metroOrderConfig.getTemplateDocFile(), metroOrderConfig.getPdfOutPath());
        String outpath;
        switch (lang.toLowerCase()) {
            case "de":
                outpath = invoiceMaker.makeInvoiceReplaceMap(invoice).toGermanStyle().toPdf();
                break;
            default:
                outpath = invoiceMaker.makeInvoiceReplaceMap(invoice).toPdf();
        }
        return outpath;
    }

    public String createDeliveryNote(Invoice invoice, String lang) {
        return null;
    }

}
