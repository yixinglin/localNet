package org.hsgt.pricing.rest.builders.metro;

import org.apache.commons.lang3.StringUtils;
import org.hsgt.pricing.domain.OfferDO;
import org.utils.JSON;

public class MetroOfferBuilderV2 {

    private OfferDO offerDO;

    public MetroOfferBuilderV2() {
        this.offerDO = new OfferDO();
    }

    /**
     * @param json: An object of map
     * @return MetroOfferBuilderV2
     * @author Lin
     * @description
     * @date 27.Jan.2024 027 23:25
     */
    public MetroOfferBuilderV2 fromJson(Object json) {
        JSON jp = new JSON(json);
        offerDO.setProductId(jp.read("$.mid"));
        offerDO.setProductKey(jp.read("$.productKey"));
        offerDO.setProductName(jp.read("$.productName"));
        offerDO.setQuantity(jp.read("$.quantity", Integer.class));
        offerDO.setPrice(jp.read("$.netPrice.amount", Float.class));
        String manufacturer = jp.read("$.manufacturer");
        if (StringUtils.isEmpty(manufacturer)) {
            offerDO.setManufacturer("unknown");
        } else {
            offerDO.setManufacturer(manufacturer);
        }
        String shippingGroupId = jp.read("$.shippingGroup.shippingGroupId");
        if (StringUtils.isEmpty(shippingGroupId)) {
            offerDO.setShippingGroupId("0");  // Free shipping cost
        } else {
            offerDO.setShippingGroupId(shippingGroupId);
        }
        offerDO.setPlatform("metro");
        return this;
    }

    public OfferDO build() {
        OfferDO ans = offerDO;
        offerDO = new OfferDO();
        return ans;
    }


}
