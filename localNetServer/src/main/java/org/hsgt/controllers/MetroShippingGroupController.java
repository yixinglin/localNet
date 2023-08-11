package org.hsgt.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.hsgt.api.ApiKey;
import org.hsgt.api.SellerApi;
import org.hsgt.api.SellerApiFactory;
import org.hsgt.builders.metro.MetroShippingGroupBuilder;
import org.hsgt.config.AccountConfig;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.mappers.ShippingGroupMapper;
import org.hsgt.mappers.SqlService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "Shipment Management")
@RestController
@RequestMapping("/shipment/metro")
public class MetroShippingGroupController {

    @Autowired
    ShippingGroupMapper shippingGroupMapper;
    private final SellerApi api;
    public MetroShippingGroupController() {
        ApiKey apiKey = AccountConfig.generateApiKey(AccountConfig.METRO_KEY);
        api = SellerApiFactory.createSellerApi(SellerApi.METRO_MOCKED, apiKey, false);
    }

    @ApiOperation(value = "Get list of shipping groups", notes = "Get a list of shipping groups with group names and group IDs using Metro API.")
    @GetMapping("/groups")
    public List<ShippingGroup> getShippingGroupList() {
        String s = this.api.selectAllShippingGroups().getContent();
        JSONArray jGroups = new JSONObject(s).getJSONArray("shippingGroups");
        List<ShippingGroup> shippingGroups = new ArrayList<>();
        for (int i = 0; i < jGroups.length(); i++) {
            ShippingGroup sg = new ShippingGroup();
            JSONObject jg = jGroups.getJSONObject(i);
            sg.setGroupName(jg.getString("shippingGroupName"));
            sg.setId(jg.getString("shippingGroupId"));
            sg.setPlatform("metro");
            sg.setOwner(api.accountName());
            shippingGroups.add(sg);
        }
        return shippingGroups;
    }

    // Get shipping group from api and store it to database
    @ApiOperation(value = "", notes = "Get shipping group by id using Metro API")
    @GetMapping("/groups/{id}")
    public ShippingGroup getShippingGroupById(@PathVariable String id) {
        String s = this.api.selectShippingGroupById(id).getContent();
        MetroShippingGroupBuilder builder = new MetroShippingGroupBuilder();
        ShippingGroup shippingGroup = builder.web(new JSONObject(s)).build();
        shippingGroup.setOwner(api.accountName());
        SqlService.sqlInsetOrUpdate(shippingGroup, shippingGroupMapper);
        return shippingGroup;
    }


}
