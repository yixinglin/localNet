package org.hsgt.order.services.impl;

import org.hsgt.order.domain.AddressDO;
import org.hsgt.order.domain.BuyerDO;
import org.hsgt.order.domain.OrderDO;
import org.hsgt.order.domain.OrderLineDO;
import org.hsgt.order.rest.builders.MetroOrderBuilder;
import org.hsgt.order.rest.metro.SellerApi;
import org.hsgt.order.services.IRestOrderService;
import org.json.JSONObject;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class RestOrderService implements IRestOrderService {
    @Autowired
    SellerApi metroOrderSellerApi;
    @Autowired
    OrderService orderService;

    @Override
    public boolean saveOrUpdateByMetroAPI(Serializable id) {
        // Get order from API
        SellerApi api = metroOrderSellerApi;
        HttpResponse httpResponse = api.selectOrderById((String) id);
        String content = httpResponse.getContent();
        Map jm = new JSONObject(content).toMap();
        MetroOrderBuilder builder = new MetroOrderBuilder();
        builder.parseOrder(jm).parseOrderLines(jm).parseBillingAddress(jm).parseShippingAddress(jm).parseBuyer(jm);

        OrderDO order = builder.buildOrder();
        List<OrderLineDO> orderLines = builder.buildOrderLines();
        AddressDO billingAddress = builder.buildBillingAddress();
        AddressDO shippingAddress = builder.buildShippingAddress();
        BuyerDO buyer = builder.buildBuyer();

        //  Firstly, save buyer and addresses to database
        BuyerDO buyerFromDB = orderService.saveOrUpdateBuyerToDatabase(buyer, shippingAddress, billingAddress);
        // Secondly, save order information to database;
        order.setBuyerId(buyerFromDB.getId());
        OrderDO orderDO = orderService.saveOrUpdateOrderToDatabase(order, orderLines);
        return true;
    }

    @Override
    public boolean saveOrUpdateByKauflandAPI(Serializable id) {
        return false;
    }
}
