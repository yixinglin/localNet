package org.hsgt.order.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.order.BO.Order;
import org.hsgt.order.domain.AddressDO;
import org.hsgt.order.domain.BuyerDO;
import org.hsgt.order.domain.OrderDO;
import org.hsgt.order.domain.OrderLineDO;
import org.hsgt.order.mapper.OrderMapper;
import org.hsgt.order.rest.builders.MetroOrderBuilder;
import org.hsgt.order.rest.metro.SellerApi;
import org.hsgt.order.services.IOrderService;
import org.json.JSONObject;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class MetroOrderService extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {
    @Autowired
    SellerApi metroOrderSellerApi;
    @Autowired
    MetroOrderLineService metroOrderLineService;
    @Autowired
    AddressService addressService;
    @Autowired
    BuyerService buyerService;

    @Override
    public boolean saveOrUpdate(OrderDO entity) {
        String serialNumber = entity.getSerialNumber();
        assert serialNumber != null: "serialNumber cannot be null";
        LambdaUpdateWrapper<OrderDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrderDO::getSerialNumber, serialNumber);
        return update(entity, wrapper) || save(entity);
    }

    @Override
    public List<Order> getByIdDetails(Wrapper<OrderDO> queryWrapper) {
        return null;
    }

    @Override
    public Order getByIdDetails(Serializable id) {
        return null;
    }

    @Override
    public OrderDO getBySerialNumber(String serialNumber) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getSerialNumber, serialNumber);
        List<OrderDO> orderDOS = this.list(wrapper);
        if (orderDOS.size() == 0) {
            return null;
        } else {
            return orderDOS.get(0);
        }
    }

    @Override
    public boolean saveOrUpdateByApi(Serializable id) {
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
        BuyerDO buyerFromDB = this.saveOrUpdateBuyerToDatabase(buyer, shippingAddress, billingAddress);
        // Secondly, save order information to database;
        order.setBuyerId(buyerFromDB.getId());
        OrderDO orderDO = this.saveOrUpdateOrderToDatabase(order, orderLines);
        return true;
    }

    /**
     * @param buyerDO:
     * @param shippingAddress:
     * @param billingAddress:
     * @return BuyerDO
     * @author Lin
     * @description Save or update buyer information to Database;
     * @date 28.Jan.2024 028 21:44
     */
    private BuyerDO saveOrUpdateBuyerToDatabase(BuyerDO buyerDO, AddressDO shippingAddress, AddressDO billingAddress) {
        // Check if buyer exists in database
        BuyerDO buyerFromDB = buyerService.getByCode(buyerDO.getCode());
        if (buyerFromDB == null) {
            //  If buyer does not exist, save it to database
            //  Save addresses to database
            addressService.saveOrUpdate(billingAddress);
            addressService.saveOrUpdate(shippingAddress);
            //  set address id to buyer and save it to database
            buyerDO.setShippingAddrId(shippingAddress.getId());
            buyerDO.setBillingAddrId(billingAddress.getId());
            buyerService.saveOrUpdate(buyerDO);
        } else {
            //  If buyer exists, update
            buyerService.saveOrUpdate(buyerDO);
            billingAddress.setId(buyerFromDB.getBillingAddrId());
            shippingAddress.setId(buyerFromDB.getShippingAddrId());
            addressService.saveOrUpdate(billingAddress);
            addressService.saveOrUpdate(shippingAddress);
        }
        // Get buyer information from database after updating
        buyerFromDB = buyerService.getByCode(buyerDO.getCode());
        return buyerFromDB;
    }

    private OrderDO saveOrUpdateOrderToDatabase(OrderDO orderDO, List<OrderLineDO> orderLineDOList) {
        // Check if order exists in database
        this.saveOrUpdate(orderDO);
        // Get order information from database after updating;
        OrderDO orderFromDB = this.getBySerialNumber(orderDO.getSerialNumber());
        // Save order lines to database;
        orderLineDOList.stream().forEach(o -> {
            o.setOrderId(orderFromDB.getId());
            metroOrderLineService.saveOrUpdate(o);
        });
        return orderFromDB;
    }

}
