package org.hsgt.pricing.rest.builders.metro;

import org.hsgt.pricing.domain.ShippingGroupDO;
import org.utils.JSON;

public class MetroShippingGroupBuilderV2 {

    private ShippingGroupDO shippingGroupDO;

    public MetroShippingGroupBuilderV2() {
        this.shippingGroupDO = new ShippingGroupDO();
    }

    // Build from API response
    public MetroShippingGroupBuilderV2 fromJson(String json) {
        JSON jp = new JSON(json);
        shippingGroupDO.setName(jp.read("$.shippingGroupName"));
        shippingGroupDO.setId(jp.read("$.shippingGroupId"));
        shippingGroupDO.setUnitCost(jp.read("$.shippingCosts[0].unitCost.amount", Float.class));
        shippingGroupDO.setExtraUnitCost(jp.read("$.shippingCosts[0].extraUnitCost.amount", Float.class));
        shippingGroupDO.setDestCountry(jp.read("$.shippingCosts[0].destination"));
        Boolean isThreadholdEnabled = jp.read("$.shippingCosts[0].threshold.isEnabled", Boolean.class);
        if (isThreadholdEnabled) {
            shippingGroupDO.setMaxShippingCost(jp.read("$.shippingCosts[0].threshold.value.amount", Float.class));
        }
        shippingGroupDO.setPlatform("metro");
        return this;
    }

    public ShippingGroupDO build() {
        ShippingGroupDO ans = this.shippingGroupDO;
        this.shippingGroupDO = new ShippingGroupDO();
        return ans;
    }
}
