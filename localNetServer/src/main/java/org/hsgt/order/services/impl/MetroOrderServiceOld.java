package org.hsgt.order.services.impl;

import org.hsgt.order.BO.Invoice;
import org.hsgt.order.rest.builders.MetroInvoiceBuilder;
import org.hsgt.order.rest.metro.SellerApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.DocUtils;
import org.utils.Logger;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetroOrderServiceOld extends BaseOrderService {
    Logger logger = Logger.loggerBuilder(MetroOrderServiceOld.class);
    @Autowired
    SellerApi metroOrderSellerApi;
    public Map<String, Object> makeOrderInvoice(String orderId, String lang) {
        SellerApi api = metroOrderSellerApi;
        String s = api.selectOrderById(orderId).getContent();
        Invoice invoice =  new MetroInvoiceBuilder().parse(new JSONObject(s)).financing().build();
        String outpath = this.createInvoice(invoice, lang);
        Map mInv = new HashMap();
        mInv.put("invoice", invoice);
        mInv.put("path", outpath);
        try {
            String b64 = DocUtils.pdfToBase64(Paths.get(outpath));
            mInv.put("b64", b64);
        } catch (IOException e) {
            mInv.put("b64", null);
            logger.error(e.getStackTrace().toString());
        }
        return mInv;
    }


}
