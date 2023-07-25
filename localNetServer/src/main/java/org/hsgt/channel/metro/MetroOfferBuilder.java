package org.hsgt.channel.metro;

import org.apache.ibatis.javassist.NotFoundException;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.ShippingGroupMapper;
import org.json.JSONObject;

public class MetroOfferBuilder {

    private Offer offer;

    public MetroOfferBuilder() {

    }

    public MetroOfferBuilder offer(JSONObject json) {
        offer = new Offer();
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

        ShippingGroup shippingGroup = new ShippingGroup();
        String shippingGroupId;

        if (!json.isNull("shippingGroup")) {
            // The shipping costs money
            shippingGroupId = json.getJSONObject("shippingGroup").getString("shippingGroupId");
            shippingGroup.setId(shippingGroupId);
        } else {
            // The shipping is free
        }
        offer.setShippingGroup(shippingGroup);
        offer.setPlatform("metro");
        return this;
    }

    public MetroOfferBuilder shippingGroup(ShippingGroupMapper shippingGroupMapper) throws RuntimeException, NotFoundException {

        if (this.offer == null) {
            throw new RuntimeException("Please call metroOfferBuilder().offer(json) first!");
        } else {
            String shippingGroupId = this.offer.getShippingGroup().getId();
            if (shippingGroupId == null) {
                // The shipping is free ;
            } else {
                // the shipping is not free;
                ShippingGroup shippingGroup = shippingGroupMapper.selectById(shippingGroupId);
                this.offer.setShippingGroup(shippingGroup);
                if (shippingGroup == null) {
                    String errmsg = String.format("ShippingGroup of id [%s] does not exist in Database.", shippingGroupId);
                    throw new NotFoundException(errmsg);
                }
            }
        }
        return this;
    }

    public MetroOfferBuilder shippingGroup(ShippingGroup shippingGroup) {
        this.offer.setShippingGroup(shippingGroup);
        return this;
    }

    public Offer build() {
        return offer;
    }


}
