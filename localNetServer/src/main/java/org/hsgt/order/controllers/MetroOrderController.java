package org.hsgt.order.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.hsgt.order.controllers.response.InvoiceResponse;
import org.hsgt.order.BO.Invoice;
import org.hsgt.order.services.impl.MetroOrderServiceOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utils.IoUtils;

import java.util.Map;

@Api(tags = "Order Management")
@RestController
@RequestMapping("/orders/metro")
public class MetroOrderController {

    @Autowired
    MetroOrderServiceOld metroOrderService;

    @GetMapping("/invoice")
    public InvoiceResponse getOrderInvoice(
                @ApiParam(value = "Order ID", required = true)String orderId,
                @ApiParam(value = "Invoice language. Example: de, en, zh.") String lang,
                @ApiParam(value = "Determine to return the PDF of the invoice.", required = true) boolean pdf) {
        Map mInv = metroOrderService.makeOrderInvoice(orderId, lang);
        Invoice invoice = (Invoice) mInv.get("invoice");
        String b64 = (String) mInv.get("b64");
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setChannel("metro");
        invoiceResponse.setOrderId(orderId);
        invoiceResponse.setDate(IoUtils.currentDateTime());
        invoiceResponse.setInvoice(invoice);
        if (pdf) {
            invoiceResponse.setFile(b64);
        }
        return invoiceResponse;
    }

}
