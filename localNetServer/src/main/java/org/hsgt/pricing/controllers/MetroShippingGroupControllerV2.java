package org.hsgt.pricing.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hsgt.core.domain.ResponseResult;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.servicesV2.IShippingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "Shipment Management")
@RestController
@RequestMapping("/shipment/metro")
public class MetroShippingGroupControllerV2 {

    @Autowired
    IShippingGroupService shippingGroupService;

    @ApiOperation(value = "Get list of shipping groups", notes = "Get a list of shipping groups with group names and group IDs using Metro API.")
    @GetMapping("/groups")
    public ResponseResult<List<ShippingGroup>> getShippingGroupList() {
        shippingGroupService.saveOrUpdateBatchByApi();
        List<ShippingGroup> shippingGroups = shippingGroupService.listMyCurrentActivatedShippingGroups();
        ResponseResult<List<ShippingGroup>> resp =  ResponseResult.success().setData(shippingGroups);
        return resp;
    }

    @ApiOperation(value = "", notes = "Get shipping group from database by id.")
    @GetMapping("/groups/db-{id}")
    public ResponseResult<ShippingGroup> getShippingGroupById_DB(@PathVariable String id) {
        List<String> sgs = new ArrayList<>();
        sgs.add(id);
        ShippingGroup shippingGroup = shippingGroupService.getByIdDetails(id);
        ResponseResult<ShippingGroup> resp;
        if (shippingGroup == null) {
            resp = ResponseResult.error(new RuntimeException("ShippingGroup does not exist in the database."));
        } else {
            resp = ResponseResult.success().setData(shippingGroup);
        }
        return resp;
    }


}
