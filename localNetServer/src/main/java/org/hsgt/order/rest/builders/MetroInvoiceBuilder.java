package org.hsgt.order.rest.builders;

import org.hsgt.order.domain.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.utils.IoUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetroInvoiceBuilder {

    private JSONObject accountSettings;
    private Invoice invoice;

    private final float generalTaxRate = 0.19f;

    public MetroInvoiceBuilder() {
        String s = IoUtils.readFile("../data/accountSettings.json");
        this.accountSettings = new JSONObject(s);
        this.invoice = new Invoice();
        this.loadSellerInfo();
    }

    private MetroInvoiceBuilder loadSellerInfo() {
        // Set billing information
        invoice.setDate(new Date());
        invoice.setComment("Der Rechnungsbetrag wurde bezahlt.");

        //  Parse seller information
        BillingAddress sellerAddress = new BillingAddress();
        JSONObject baddr = this.accountSettings.getJSONObject("address");
        sellerAddress.setCity(baddr.getString("city"));
        sellerAddress.setCountry(baddr.getString("country"));
        sellerAddress.setEmail(baddr.getString("email"));
        sellerAddress.setPhone(baddr.getString("phone"));
        sellerAddress.setFirstName(baddr.getString("firstName"));
        sellerAddress.setLastName(baddr.getString("lastName"));
        sellerAddress.setCompanyName(baddr.getString("companyName"));
        sellerAddress.setHouseNumber(baddr.getString("houseNumber"));
        sellerAddress.setStreetName(baddr.getString("streetName"));
        sellerAddress.setZipCode(baddr.getString("zipCode"));
        sellerAddress.setSupplementField(baddr.getString("supplementField"));
        sellerAddress.setTaxNumber(baddr.getString("taxNumber"));
        invoice.setSellerAddress(sellerAddress);

        //  Parse bank account information
        BankAccount bankAccount = new BankAccount();
        JSONObject bacc = this.accountSettings.getJSONObject("bankAccount");
        bankAccount.setAccountHolder(bacc.getString("accountHolder"));
        bankAccount.setIBAN(bacc.getString("IBAN"));
        bankAccount.setBIC(bacc.getString("BIC"));
        bankAccount.setFinancialInstitution(bacc.getString("financialInstitution"));
        invoice.setSellerBankAccount(bankAccount);
        return this;
    }

    // Parse JSON requested from Metro API.
    public MetroInvoiceBuilder parse(JSONObject json) {
        JSONObject buyerDetails = json.getJSONObject("buyerDetails");

        invoice.setTitle("Rechnung");
        invoice.setInvoiceNumber(json.getString("orderNumber"));
        invoice.setInvoiceId(json.getString("orderId"));

        //  Parse customer address
        JSONObject baddr;
        baddr = buyerDetails.getJSONObject("address").getJSONObject("billing");
        BillingAddress buyerAddress = new BillingAddress();
        invoice.setBuyerAddress(buyerAddress);
        buyerAddress.setCity(baddr.getString("city"));
        buyerAddress.setCountry(baddr.getString("country"));
        buyerAddress.setEmail("");
        buyerAddress.setPhone("");
        buyerAddress.setFirstName(baddr.getString("firstName"));
        buyerAddress.setLastName(baddr.getString("lastName"));
        if (!baddr.isNull("companyName"))
            buyerAddress.setCompanyName(baddr.getString("companyName"));
        buyerAddress.setHouseNumber(baddr.getString("houseNumber"));
        buyerAddress.setStreetName(baddr.getString("street"));
        buyerAddress.setZipCode(baddr.getString("zipCode"));
        if (!baddr.isNull("addressLine2")) {
            buyerAddress.setSupplementField(baddr.getString("addressLine2"));
        }
        if(!buyerDetails.isNull("taxNumber")) {
            buyerAddress.setTaxNumber(buyerDetails.getJSONObject("taxNumber").getString("value"));
        }
        if(!buyerDetails.isNull("companyVat")) {
            buyerAddress.setCompanyVat(buyerDetails.getString("companyVat"));
        }

        //  Financial calculation
        List<OrderLine> orderLineList = new ArrayList<>();
        this.invoice.setOrderLines(orderLineList);
        JSONArray jsonOrderLines = json.getJSONArray("orderLines");
        for (int i = 0; i < jsonOrderLines.length(); i++) {
            OrderLine orderLine = new OrderLine();
            orderLineList.add(orderLine);
            JSONObject item = jsonOrderLines.getJSONObject(i);
            orderLine.setTitle(item.getString("productName"));
            JSONObject financialDetails = item.getJSONObject("financialDetails");
            orderLine.setGrossPrice(financialDetails.getJSONObject("unitPrice").getJSONObject("gross").getFloat("amount"));
            orderLine.setCurrency(financialDetails.getJSONObject("unitPrice").getJSONObject("gross").getString("currency"));
            orderLine.setQuantity(item.getInt("quantity"));
            orderLine.setTaxRate(financialDetails.getInt("vatRate")/100.0f);

            float taxRate = orderLine.getTaxRate();
            float tax = orderLine.getGrossPrice() * (taxRate/(1+taxRate));
            float grossPrice = orderLine.getGrossPrice();
            float netPrice = grossPrice / (1+taxRate);

            orderLine.setTax(tax);
            orderLine.setNetPrice(netPrice);
        }

        OrderLine shippingCost = new OrderLine();
        this.invoice.setShippingCost(shippingCost);
        JSONObject financialDetails = json.getJSONObject("financialDetails");
        shippingCost.setNetPrice(financialDetails.getJSONObject("shipping").getJSONObject("net").getFloat("amount"));
        shippingCost.setCurrency(financialDetails.getJSONObject("shipping").getJSONObject("net").getString("currency"));
        shippingCost.setGrossPrice(financialDetails.getJSONObject("shipping").getJSONObject("gross").getFloat("amount"));
        shippingCost.setTax(financialDetails.getJSONObject("shipping").getJSONObject("vat").getFloat("amount"));
        shippingCost.setTaxRate(this.generalTaxRate);
        shippingCost.setQuantity(1);
        shippingCost.setTitle("Shipping");
        return this;
    }

    public MetroInvoiceBuilder financing() {
        Finance finance = new Finance();
        this.invoice.setFinance(finance);
        List<OrderLine> orderLineList = this.invoice.getOrderLines();
        float totalNetPrice = (float) orderLineList.stream().mapToDouble(o -> o.getNetPrice() * o.getQuantity()).sum();
        float totalPrice = (float) orderLineList.stream().mapToDouble(o -> o.getGrossPrice() * o.getQuantity()).sum();
        float totalTax = (float) orderLineList.stream().mapToDouble(o -> o.getTax() * o.getQuantity()).sum();
        totalPrice += this.invoice.getShippingCost().getGrossPrice();
        totalTax += this.invoice.getShippingCost().getTax();
        finance.setTax(totalTax);
        finance.setTotalPrice(totalPrice);
        finance.setTotalNetPrice(totalNetPrice);
        return this;
    }

    public Invoice build() {
        Invoice ans = this.invoice;
        this.invoice = new Invoice();
        return ans;
    }

}
