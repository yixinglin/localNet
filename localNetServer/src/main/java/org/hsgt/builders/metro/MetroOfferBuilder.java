package org.hsgt.builders.metro;

import org.apache.ibatis.javassist.NotFoundException;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.ShippingGroupMapper;
import org.json.JSONObject;

public class MetroOfferBuilder {

    private Offer offer;

    public MetroOfferBuilder() {
        this.offer = new Offer();
    }

    /*
     * @param offerList: Result of sellerApi.selectAllOffers()
     * @param mid: ID of the target offer
     * @author Lin
     * @Description: Build from API response
     * @date 25.Jul.2023 025 22:51
     */
    public  MetroOfferBuilder offer(JSONObject offerList, String mid) {
        for (Object item: offerList.getJSONArray("items")) {
            JSONObject jsOffer = (JSONObject) item;
            if (jsOffer.getString("mid").equals(mid)) {
                this.offer(jsOffer);
                break;
            }
        }
        return this;
    }

    // Build from API response
    public MetroOfferBuilder offer(JSONObject jsonOffer) {
        offer.setId(jsonOffer.getString("mid"));
        offer.setProductKey(jsonOffer.getString("productKey"));
        offer.setProductName(jsonOffer.getString("productName"));
        offer.setQuantity(jsonOffer.getInt("quantity"));
        offer.setPrice(jsonOffer.getJSONObject("netPrice").getFloat("amount"));
        offer.setNote("");
        if (jsonOffer.isNull("manufacturer")) {
            offer.setManufacturer("unknown");
        } else {
            offer.setManufacturer(jsonOffer.getString("manufacturer"));
        }

        ShippingGroup shippingGroup = new ShippingGroup();
        String shippingGroupId;

        if (!jsonOffer.isNull("shippingGroup")) {
            // The shipping costs money
            shippingGroupId = jsonOffer.getJSONObject("shippingGroup").getString("shippingGroupId");
            shippingGroup.setId(shippingGroupId);
            shippingGroup.setGroupName("unknown");
        } else {
            // The shipping is free
        }
        offer.setShippingGroup(shippingGroup);
        offer.setPlatform("metro");
        return this;
    }

    // Build from database
    public MetroOfferBuilder shippingGroup(ShippingGroupMapper shippingGroupMapper) throws RuntimeException, NotFoundException {
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

        return this;
    }

    public MetroOfferBuilder shippingGroup(ShippingGroup shippingGroup) {
        this.offer.setShippingGroup(shippingGroup);
        return this;
    }

    // Build from database
    public MetroOfferBuilder moreField(OfferMapper offerMapper) {
        Offer offerDb = offerMapper.selectById(this.offer.getId());
        this.offer.setAmount(offerDb.getAmount());
        this.offer.setNote(offerDb.getNote());
        this.offer.setLowestPrice(offerDb.getLowestPrice());
        return this;
    }

    public Offer build() {
        Offer ans = offer;
        offer = new Offer();
        return ans;
    }


}
