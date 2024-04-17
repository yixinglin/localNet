package org.hsgt.order.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.order.BO.*;
import org.hsgt.order.domain.AddressDO;
import org.hsgt.order.domain.BuyerDO;
import org.hsgt.order.domain.OrderDO;
import org.hsgt.order.domain.OrderLineDO;
import org.hsgt.order.mapper.OrderMapper;
import org.hsgt.order.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {
    @Autowired
    OrderLineService orderLineService;
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
    public Order getDetailsBySerialNumber(String serialNumber) {
        OrderDO orderDO = this.getBySerialNumber(serialNumber);

        BuyerDO buyerDO = buyerService.getById(orderDO.getBuyerId());
        BillingAddress billingAddress = addressService.getBillingAddressDetailsById(buyerDO.getBillingAddrId());
        ShippingAddress shippingAddress = addressService.getShippingAddressDetailsById(buyerDO.getShippingAddrId());
        Buyer buyer = buyerService.getDetailsById(orderDO.getBuyerId());

        Order order = Order.convertToBO(orderDO);
        List<OrderLine> orderLineList = orderLineService.getDetailsByOrderId(orderDO.getId());
        order.setOrderLines(orderLineList);
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);
        order.setBuyer(buyer);
        return order;
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

    /**
     * @param buyerDO:
     * @param shippingAddress:
     * @param billingAddress:
     * @return BuyerDO
     * @author Lin
     * @description Save or update buyer information to Database;
     * @date 28.Jan.2024 028 21:44
     */
    protected BuyerDO saveOrUpdateBuyerToDatabase(BuyerDO buyerDO, AddressDO shippingAddress, AddressDO billingAddress) {
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

    protected OrderDO saveOrUpdateOrderToDatabase(OrderDO orderDO, List<OrderLineDO> orderLineDOList) {
        // Check if order exists in database
        this.saveOrUpdate(orderDO);
        // Get order information from database after updating;
        OrderDO orderFromDB = this.getBySerialNumber(orderDO.getSerialNumber());
        // Save order lines to database;
        orderLineDOList.stream().forEach(o -> {
            o.setOrderId(orderFromDB.getId());
            orderLineService.saveOrUpdate(o);
        });
        return orderFromDB;
    }

}
