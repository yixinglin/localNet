package org.hsgt.builders.metro;

import org.hsgt.api.SellerApi;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.mappers.ShippingGroupMapper;
import org.json.JSONObject;
import org.net.HttpResponse;

public class MetroShippingGroupBuilder {

    private ShippingGroup shippingGroup;

    public MetroShippingGroupBuilder() {
        this.shippingGroup = new ShippingGroup();
    }

    // Build from database
    public MetroShippingGroupBuilder db(String id, ShippingGroupMapper shippingGroupMapper) {
        this.shippingGroup = shippingGroupMapper.selectById(id);
        return this;
    }

    // Build from API response
    public MetroShippingGroupBuilder web(JSONObject json) {
        shippingGroup.setGroupName(json.getString("shippingGroupName"));
        shippingGroup.setId(json.getString("shippingGroupId"));
        JSONObject cost = json.getJSONArray("shippingCosts").getJSONObject(0);
        shippingGroup.setUnitCost(cost.getJSONObject("unitCost").getFloat("amount"));
        shippingGroup.setExtraUnitCost(cost.getJSONObject("extraUnitCost").getFloat("amount"));
        shippingGroup.setDestCountry(cost.getString("destination"));
        if (cost.getJSONObject("threshold").getBoolean("isEnabled")) {
            shippingGroup.setMaxShippingCost(cost.getJSONObject("threshold").getJSONObject("value").getFloat("amount"));
        }
        shippingGroup.setPlatform("metro");
        return this;
    }

    // Build from API response
    public MetroShippingGroupBuilder web(JSONObject shippingGroupList, String id, SellerApi api) {
        for (Object item: shippingGroupList.getJSONArray("shippingGroups")) {
            JSONObject sg = (JSONObject) item;
            if (id.equals(sg.getString("shippingGroupId"))) {
                HttpResponse resp = api.selectShippingGroupById(id);
                this.web(new JSONObject(resp.getContent()));
            }
        }
        return this;
    }

    public ShippingGroup build() {
        return this.shippingGroup;
    }

}
