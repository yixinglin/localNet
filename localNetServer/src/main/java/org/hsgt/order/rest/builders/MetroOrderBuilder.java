package org.hsgt.order.rest.builders;

import org.hsgt.core.enu.ChannelType;
import org.hsgt.order.domain.AddressDO;
import org.hsgt.order.domain.BuyerDO;
import org.hsgt.order.domain.OrderDO;
import org.hsgt.order.domain.OrderLineDO;
import org.hsgt.order.enu.AddressType;
import org.hsgt.order.enu.OrderStatus;
import org.hsgt.order.enu.PaymentType;
import org.utils.JSON;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetroOrderBuilder {

    private List<OrderLineDO> orderlines;
    private OrderDO order;
    private AddressDO billingAddress;
    private AddressDO shippingAddress;
    private BuyerDO buyer;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00");
    public MetroOrderBuilder() {
        orderlines = new ArrayList<>();
        order = new OrderDO();
        billingAddress = new AddressDO();
        shippingAddress = new AddressDO();
        buyer = new BuyerDO();
    }

    /**
     * @param jsonOrder: An object of map
     * @return MetroOrderBuilder
     * @author Lin
     * @description
     * @date 27.Jan.2024 027 23:24
     */
    public MetroOrderBuilder parseOrder(Object jsonOrder) {
        JSON jp = new JSON(jsonOrder);
        String s = jp.read("$.orderId");
        order.setOrderKey(jp.read("$.orderId"));
        order.setSerialNumber(jp.read("$.orderNumber"));
        order.setCreateAt(LocalDateTime.parse(jp.read("$.created"), formatter));
        order.setShippingCost(jp.read("$.shippingCost.amount", Float.class));
        order.setUpdateAt(LocalDateTime.now());

        String payment = jp.read("$.payment.method");
        switch (payment) {
            case "credit_card":
                order.setPaymentType(PaymentType.CreditCard.getVal()); break;
            default:
                order.setPaymentType(PaymentType.Other.getVal());
        }


        String status = jp.read("$.status");
        switch (status) {
            case "shipped":
                order.setStatus(OrderStatus.SHIPPED.getVal()); break;
            case "placed":
                order.setStatus(OrderStatus.PLACED.getVal()); break;
            case "canceled ":
                order.setStatus(OrderStatus.CANCELED.getVal()); break;
            case "confirmed":
                order.setStatus(OrderStatus.CONFIRMED.getVal());break;
            case "return_requested":
                order.setStatus(OrderStatus.RETURN_REQUESTED.getVal()); break;
            case "return_accepted":
                order.setStatus(OrderStatus.RETURN_ACCEPTED.getVal());break;
            case "return_declined":
                order.setStatus(OrderStatus.RETURN_DECLINED.getVal()); break;
        }
        order.setChannel(ChannelType.METRO.getVal());
        return this;
    }

    public MetroOrderBuilder parseOrderLines(Object jsonOrder) {
        JSON jp = new JSON(jsonOrder);
        List<Map> mapList = jp.read("$.orderLines", List.class);
        orderlines = mapList.stream().map(o -> this.parseOrderLine(o)).collect(Collectors.toList());
        return this;
    }

    public MetroOrderBuilder parseShippingAddress(Object jsonOrder) {
        JSON jp = new JSON(jsonOrder);
        shippingAddress.setCity(jp.read("$.buyerDetails.address.shipping.city"));
        shippingAddress.setZipCode(jp.read("$.buyerDetails.address.shipping.zipCode"));
        shippingAddress.setCountryCode(jp.read("$.buyerDetails.address.shipping.country"));
        shippingAddress.setCountry(jp.read("$.buyerDetails.address.shipping.country"));
        shippingAddress.setStateOrProvince(jp.read("$.buyerDetails.address.shipping.stateOrProvince"));
        shippingAddress.setStreetName(jp.read("$.buyerDetails.address.shipping.street"));
        shippingAddress.setHouseNumber(jp.read("$.buyerDetails.address.shipping.houseNumber"));
        shippingAddress.setCompanyName(jp.read("$.buyerDetails.address.shipping.companyName"));
        shippingAddress.setFirstName(jp.read("$.buyerDetails.address.shipping.firstName"));
        shippingAddress.setLastName(jp.read("$.buyerDetails.address.shipping.lastName"));
        shippingAddress.setSupplementField(jp.read("$.buyerDetails.address.shipping.addressLine2"));
        shippingAddress.setEmail(jp.read("$.buyerDetails.email"));
        shippingAddress.setPhone(jp.read("$.buyerDetails.address.shipping.phone"));
        shippingAddress.setType(AddressType.Shipping.getVal());
        return this;
    }

    public MetroOrderBuilder parseBillingAddress(Object jsonOrder) {
        JSON jp = new JSON(jsonOrder);
        billingAddress.setCity(jp.read("$.buyerDetails.address.billing.city"));
        billingAddress.setZipCode(jp.read("$.buyerDetails.address.billing.zipCode"));
        billingAddress.setCountryCode(jp.read("$.buyerDetails.address.billing.country"));
        billingAddress.setCountry(jp.read("$.buyerDetails.address.billing.country"));
        billingAddress.setStateOrProvince(jp.read("$.buyerDetails.address.billing.stateOrProvince"));
        billingAddress.setStreetName(jp.read("$.buyerDetails.address.billing.street"));
        billingAddress.setHouseNumber(jp.read("$.buyerDetails.address.billing.houseNumber"));
        billingAddress.setCompanyName(jp.read("$.buyerDetails.address.billing.companyName"));
        billingAddress.setFirstName(jp.read("$.buyerDetails.address.billing.firstName"));
        billingAddress.setLastName(jp.read("$.buyerDetails.address.billing.lastName"));
        billingAddress.setSupplementField(jp.read("$.buyerDetails.address.billing.addressLine2"));
        billingAddress.setEmail(jp.read("$.buyerDetails.email"));
        billingAddress.setPhone(jp.read("$.buyerDetails.address.shipping.phone"));
        billingAddress.setTaxNumber(jp.read("$.buyerDetails.taxNumber.value"));
        billingAddress.setCompanyVat(jp.read("$.buyerDetails.companyVat"));
        billingAddress.setType(AddressType.Billing.getVal());
        return this;
    }

    public MetroOrderBuilder parseBuyer(Object jsonOrder) {
        JSON jp = new JSON(jsonOrder);
        buyer.setCode(jp.read("$.buyerDetails.id"));
        buyer.setEmail(jp.read("$.buyerDetails.email"));
        buyer.setFirstName(jp.read("$.buyerDetails.firstName"));
        buyer.setLastName(jp.read("$.buyerDetails.lastName"));
        buyer.setChannel(ChannelType.METRO.getVal());
        return this;
    }

    private OrderLineDO parseOrderLine(Object orderLine) {
        JSON jp = new JSON(orderLine);
        OrderLineDO orderLineDO = new OrderLineDO();
        orderLineDO.setEan(jp.read("$.gtin"));
        orderLineDO.setSku(jp.read("$.sku"));
        orderLineDO.setMid(jp.read("$.mid"));
        orderLineDO.setTitle(jp.read("$.productName"));
        orderLineDO.setDescription(orderLineDO.getTitle());
        orderLineDO.setNetPrice(jp.read("$.financialDetails.originalUnitPrice.net.amount", Float.class));
        orderLineDO.setGrossPrice(jp.read("$.financialDetails.originalUnitPrice.gross.amount", Float.class));
        orderLineDO.setQuantity(jp.read("$.quantity", Integer.class));
        orderLineDO.setCurrency(jp.read("$.subTotal.currency"));
        orderLineDO.setTaxRate(jp.read("$.financialDetails.vatRate", Integer.class));
        orderLineDO.setTax(jp.read("$.financialDetails.originalUnitPrice.vat.amount", Float.class));
        return orderLineDO;
    }


    public OrderDO buildOrder() {
        OrderDO orderDO = order;
        order = new OrderDO();
        return orderDO;
    }

    public List<OrderLineDO> buildOrderLines() {
        List<OrderLineDO> orderLineDOS = orderlines;
        orderlines = new ArrayList<>();
        return orderLineDOS;
    }

    public AddressDO buildBillingAddress() {
        AddressDO addressDO = billingAddress;
        billingAddress = new AddressDO();
        return addressDO;
    }

    public AddressDO buildShippingAddress() {
        AddressDO addressDO = shippingAddress;
        shippingAddress = new AddressDO();
        return addressDO;
    }

    public BuyerDO buildBuyer() {
        BuyerDO buyerDO = buyer;
        buyer = new BuyerDO();
        return buyerDO;
    }
}
