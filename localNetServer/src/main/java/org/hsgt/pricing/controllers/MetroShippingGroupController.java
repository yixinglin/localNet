package org.hsgt.pricing.controllers;

import io.swagger.annotations.Api;
import org.hsgt.pricing.services.ShippingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Shipment Management")
@RestController
@RequestMapping("/shipment/metro")
public class MetroShippingGroupController {

    @Qualifier("metroShippingGroupService")
    @Autowired
    ShippingGroupService shippingGroupService;


//    @ApiOperation(value = "Get list of shipping groups", notes = "Get a list of shipping groups with group names and group IDs using Metro API.")
//    @GetMapping("/groups")
//    public ResponseResult<List<ShippingGroup>> getShippingGroupList() {
//        List<ShippingGroup> shippingGroups = shippingGroupService.queryAll();
//        ResponseResult<List<ShippingGroup>> resp =  ResponseResult.success().setData(shippingGroups);
//        return resp;
//    }

    // Get shipping group from api and store it to database
//    @ApiOperation(value = "", notes = "Get shipping group by id using Metro API, and store it to the database.")
//    @GetMapping("/groups/{id}")
//    public ResponseResult<ShippingGroup> getShippingGroupById(@PathVariable String id) {
//        ShippingGroup shippingGroup = shippingGroupService.queryById(id);
//        ResponseResult<ShippingGroup> resp = ResponseResult.success().setData(shippingGroup);
//        return resp;
//    }

//    @ApiOperation(value = "", notes = "Get shipping group from database by id.")
//    @GetMapping("/groups/db-{id}")
//    public ResponseResult<ShippingGroup> getShippingGroupById_DB(@PathVariable String id) {
//        List<String> sgs = new ArrayList<>();
//        sgs.add(id);
//        ShippingGroup shippingGroup = shippingGroupService.queryById(sgs).get(0);
//        ResponseResult<ShippingGroup> resp;
//        if (shippingGroup == null) {
//            resp = ResponseResult.error(new RuntimeException("ShippingGroup does not exist in the database."));
//        } else {
//            resp = ResponseResult.success().setData(shippingGroup);
//        }
//        return resp;
//    }


}
