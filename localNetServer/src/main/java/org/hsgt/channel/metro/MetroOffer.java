package org.hsgt.channel.metro;

import org.apache.ibatis.javassist.NotFoundException;
import org.hsgt.api.SellerApi;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.ShippingGroupMapper;
import org.json.JSONObject;

public class MetroOffer extends Offer {


    public static Offer getInstanceByJson(JSONObject json, ShippingGroupMapper shippingGroupMapper) throws NotFoundException {
        MetroOffer offer = new MetroOffer();
        offer.setId(json.getString("mid"));
        offer.setProductKey(json.getString("productKey"));
        offer.setProductName(json.getString("productName"));
        offer.setQuantity(json.getInt("quantity"));
        offer.setPrice(json.getJSONObject("netPrice").getFloat("amount"));
        if (json.isNull("manufacturer")) {
            offer.setManufacturer("unknown");
        } else {
            offer.setManufacturer(json.getString("manufacturer"));
        }

        ShippingGroup shippingGroup;
        String shippingGroupId;

        if (!json.isNull("shippingGroup")) {
            // The shipping costs money
            shippingGroupId = json.getJSONObject("shippingGroup").getString("shippingGroupId");
            shippingGroup = shippingGroupMapper.selectById(shippingGroupId);
            if (shippingGroup == null) {
                String errmsg = String.format("ShippingGroup of id [%s] does not exist in Database.", shippingGroupId);
                throw new NotFoundException(errmsg);
            }
        }  else {
            // The shipping is free
            shippingGroup = new ShippingGroup();
        }

        offer.setShippingGroup(shippingGroup);
        offer.setPlatform("metro");
        return offer;
    }
}