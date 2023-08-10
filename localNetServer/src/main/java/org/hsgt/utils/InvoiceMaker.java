package org.hsgt.utils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.Data;
import org.hsgt.entities.orders.*;
import org.utils.DocUtils;
import org.utils.IoUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * @param null:
  * @return null
 * @author Lin
 * @description TODO
 * @date 10.Aug.2023 010 03:03
 * Example:
        InvoiceMaker invoiceMaker = new InvoiceMaker();
        invoiceMaker.makeInvoiceReplaceMap(invoice).toGermanStyle().toPdf();
 */
public class InvoiceMaker {

    // private final Invoice invoice;
    private Map invoiceReplaceMap;
    private String tempDirectory = "src/main/resources/hsgt/.cache";  // Directory for temporal files.
    private String templateDocFile = "src/main/resources/hsgt/template_b2c_de.docx";  // 模板文件
    private String targetPdfDirectory = "src/main/resources/hsgt/.cache";  // Directory for PDF output;
    private Path outputPdfPath;
    private Path outputDocxPath;

    public InvoiceMaker() {

    }

    public InvoiceMaker makeInvoiceReplaceMap(Invoice invoice) {
        IoUtils.makedir(Paths.get(this.tempDirectory));
        IoUtils.makedir(Paths.get(this.targetPdfDirectory));
        this.outputDocxPath = Paths.get(this.targetPdfDirectory, invoice.getInvoiceNumber()+".docx");
        this.outputPdfPath = Paths.get(this.targetPdfDirectory, invoice.getInvoiceNumber()+".pdf");
        this.invoiceReplaceMap = makeReplaceMap(invoice);
        return this;
    }

    public InvoiceMaker toGermanStyle() {
        if (this.invoiceReplaceMap == null) {
            throw new RuntimeException("InvoiceReplaceMap cannot be null!");
        }

        String[] fieldNames = new String[] {"price_no_tax", "shipment", "total_price", "tax"};
        for (String fn: fieldNames) {
            this.invoiceReplaceMap.put(fn, this.floatOfGermanyStyle((String) this.invoiceReplaceMap.get(fn)));
        }

        String key = "order_lines";
        ArrayList<TemplateOrderLine> orderLines = (ArrayList<TemplateOrderLine>) this.invoiceReplaceMap.get(key);
        for (TemplateOrderLine e: orderLines) {
            e.setGross_price(floatOfGermanyStyle(e.getGross_price()));
            e.setUnit_price(floatOfGermanyStyle(e.getUnit_price()));
        }

        key = "date_of_issue";
        String curDate = (String) this.invoiceReplaceMap.get(key);
        Map<String, String> translateMap = new HashMap() {{
            put("January", "Januar");
            put("February", "Februar");
            put("March", "März");
            put("April", "April");
            put("May", "Mai");
            put("June", "Juni");
            put("July", "Juli");
            put("August", "August");
            put("September", "September");
            put("October", "Oktober");
            put("November", "November");
            put("December", "Dezember");
        }};
        for (String e: translateMap.keySet()) {
            curDate = curDate.replace(e, translateMap.get(e));
        }
        this.invoiceReplaceMap.put(key, curDate);
        return this;
    }


    public String toDocx() {
        if(this.invoiceReplaceMap == null) {
            throw new RuntimeException("InvoiceReplaceMap can't be null!");
        }

        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure configure = Configure.builder().bind("order_lines", policy).build();

        try(XWPFTemplate template = XWPFTemplate.compile(this.templateDocFile, configure).render(this.invoiceReplaceMap);
            FileOutputStream out = new FileOutputStream(this.outputDocxPath.toString())) {
            template.write(out);
            out.flush();
        } catch (FileNotFoundException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }

        return this.outputDocxPath.toString();
    }

    public String toPdf() {
        this.toDocx();
        DocUtils.wordToPdf(this.outputDocxPath, this.outputPdfPath);
        return this.outputPdfPath.toString();
    }

    // Convert Invoice object to replace map for the InvoiceMaker.
    private Map makeReplaceMap(Invoice invoice) {
        Map<String, Object> replMap = new HashMap();
        replMap.put("invoice_no", invoice.getInvoiceNumber());
        replMap.put("date_of_issue", this.date2str(invoice.getDate()));
        BillingAddress billingAddress = invoice.getBuyerAddress();
        String companyName = billingAddress.getCompanyName();
        String buyerName = String.format("%s %s", billingAddress.getFirstName(), billingAddress.getLastName());
        String co = billingAddress.getSupplementField();
        String name = "";
        if(companyName != null && companyName.length()>0)
            name += companyName + '\n';
        name += buyerName + '\n';
        if(co != null && co.length() > 0)
            name += co;
        replMap.put("name", name.trim());
        replMap.put("address", String.format("%s %s", billingAddress.getStreetName(), billingAddress.getHouseNumber()));
        replMap.put("city", String.format("%s %s", billingAddress.getZipCode(), billingAddress.getCity()));
        String countryName = billingAddress.getCountry();
        if(countryName.equals("DE"))
            countryName = "Deutschland";
        replMap.put("country", countryName);

        String taxNoAndVat = "";
        if(billingAddress.getTaxNumber()!=null) {
            String taxNumber = "St-Nr, IdNr. " + billingAddress.getTaxNumber();
            taxNoAndVat = taxNumber + '\n';
        }
        if(billingAddress.getCompanyVat()!=null) {
            String companyVat = "USt ID: " + billingAddress.getCompanyVat();
            taxNoAndVat += companyVat;
        }
        replMap.put("phone", taxNoAndVat.trim());

        Finance finance = invoice.getFinance();
        replMap.put("tax_rate", String.format("%d", (int)(invoice.getOrderLines().get(0).getTaxRate()*100)));
        replMap.put("price_no_tax", this.float2str(finance.getTotalNetPrice()));
        replMap.put("tax", this.float2str(finance.getTax()));
        replMap.put("total_price", this.float2str(finance.getTotalPrice()));
        replMap.put("shipment", this.float2str(invoice.getShippingCost().getNetPrice()));

        //   Orderline mapping
        List<TemplateOrderLine> templateOrderLines = new ArrayList<>();
        replMap.put("order_lines", templateOrderLines);
        for (OrderLine oline: invoice.getOrderLines()) {
            TemplateOrderLine tol = new TemplateOrderLine();
            templateOrderLines.add(tol);
            tol.setTitle(oline.getTitle());
            tol.setQuantity(String.format("%d", oline.getQuantity()));
            tol.setGross_price(this.float2str(oline.getGrossPrice()*oline.getQuantity()));
            tol.setUnit_price(this.float2str(oline.getGrossPrice()));
            tol.setTax_rate(String.format("%d", (int)(oline.getTaxRate()*100)));
        }

        //   Seller info
        BillingAddress sellerAddress = invoice.getSellerAddress();
        replMap.put("seller_tax_number", sellerAddress.getTaxNumber());
        replMap.put("seller_address", String.format("%s %s", sellerAddress.getStreetName(), sellerAddress.getHouseNumber()));
        replMap.put("seller_company", sellerAddress.getCompanyName());
        replMap.put("seller_city", String.format("%s %s", sellerAddress.getZipCode(), sellerAddress.getCity()));
        replMap.put("seller_country", sellerAddress.getCountry());
        replMap.put("seller_phone", sellerAddress.getPhone());
        replMap.put("seller_email", sellerAddress.getEmail());
        BankAccount bankAccount = invoice.getSellerBankAccount();
        replMap.put("account_holder", bankAccount.getAccountHolder());
        replMap.put("iban", bankAccount.getIBAN());
        replMap.put("bic", bankAccount.getBIC());
        replMap.put("financial_institution", bankAccount.getFinancialInstitution());
        return replMap;
    }

    private String float2str(float fl) {
        return String.format("%.2f", fl);
    }

    private String date2str(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String ret = new SimpleDateFormat("dd MMMM yyyy").format(date);
        return ret;
    }

    private String floatOfGermanyStyle(String num) {
        return num.replace('.', ',');
    }

    @Data
    public class TemplateOrderLine {
        private String title;
        private String quantity;
        private String unit_price;
        private String tax_rate;
        private String gross_price;

    }
}
