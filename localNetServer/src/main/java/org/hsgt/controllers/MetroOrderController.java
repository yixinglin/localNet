package org.hsgt.controllers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.hsgt.api.SellerApi;
import org.hsgt.builders.metro.MetroInvoiceBuilder;
import org.hsgt.config.Global;
import org.hsgt.controllers.response.InvoiceResponse;
import org.hsgt.entities.orders.Invoice;
import org.hsgt.utils.InvoiceMaker;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utils.DocUtils;
import org.utils.IoUtils;
import java.io.IOException;
import java.nio.file.Paths;

@Api(tags = "Order Management")
@RestController
@RequestMapping("/orders/metro")
public class MetroOrderController {

    private SellerApi api;

    public MetroOrderController() {
        this.api = Global.getMetroApiInstance();
    }

    @GetMapping("/invoice")
    public InvoiceResponse orderInvoice(
                @ApiParam(value = "Order ID", required = true)String orderId,
                @ApiParam(value = "Invoice language. Example: de, en, zh.") String lang,
                @ApiParam(value = "Determine to return the PDF of the invoice.", required = true) boolean pdf) {
        String s = this.api.selectOrderById(orderId).getContent();
        Invoice invoice =  new MetroInvoiceBuilder().parse(new JSONObject(s)).financing().build();
        InvoiceMaker invoiceMaker = new InvoiceMaker();
        String outpath;
        switch (lang.toLowerCase()) {
            case "de":
                outpath = invoiceMaker.makeInvoiceReplaceMap(invoice).toGermanStyle().toPdf();
                break;
            default:
                outpath = invoiceMaker.makeInvoiceReplaceMap(invoice).toPdf();
        }

        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setChannel("metro");
        invoiceResponse.setOrderId(orderId);
        invoiceResponse.setDate(IoUtils.currentDateTime());
        invoiceResponse.setInvoice(invoice);
        if (pdf) {
            try {
                String b64 = DocUtils.pdfToBase64(Paths.get(outpath));
                invoiceResponse.setFile(b64);
            } catch (IOException e) {
                invoiceResponse.setFile(null);
                System.err.println(e);
            }
        }

        return invoiceResponse;
    }

}
