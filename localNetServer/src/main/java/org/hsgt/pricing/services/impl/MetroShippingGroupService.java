package org.hsgt.pricing.services.impl;

import org.hsgt.core.mapper.SqlService;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.mapper.ShippingGroupMapper;
import org.hsgt.pricing.rest.builders.metro.MetroShippingGroupBuilder;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.services.ShippingGroupService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetroShippingGroupService implements ShippingGroupService {

    @Autowired
    ShippingGroupMapper shippingGroupMapper;

//    @Autowired
//    private MetroPricingConfig pricingConfig;

    @Autowired
    private SellerApi metroOfferSellerApi;

    public MetroShippingGroupService()  {
    }

    /*
     * @param :
      * @return List<ShippingGroup>
     * @author Lin
     * @description TODO [UTD] Get a list of shipping groups with group names and group IDs without no more details.
     *               The data is acquired from Metro API.
     * @date 16.Aug.2023 016 03:35
     */
    @Override
    public List<ShippingGroup> queryAll() {
        SellerApi api = metroOfferSellerApi;
        String s = api.selectAllShippingGroups().getContent();
        JSONArray jGroups = new JSONObject(s).getJSONArray("shippingGroups");
        List<ShippingGroup> shippingGroups = new ArrayList<>();
        ShippingGroup free = new ShippingGroup();
        shippingGroups.add(free);
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

    /*
     * @param ids:
      * @return List<ShippingGroup>
     * @author Lin
     * @description TODO Get shipping groups from database given a list of IDs
     * @date 16.Aug.2023 016 03:38
     */
    @Override
    public List<ShippingGroup> queryById(List<String> ids) {
        List<ShippingGroup> shippingGroups = new ArrayList<>();
        for (String id: ids)  {
            shippingGroups.add(this.shippingGroupMapper.selectById(id));
        }
        return shippingGroups;
    }

    /*
     * @param id:
      * @return ShippingGroup
     * @author Lin
     * @description TODO [UTD] Get shipping group from api and store it to database.
     * @date 16.Aug.2023 016 03:33
     */
    @Override
    public ShippingGroup queryById(String id) {
        SellerApi api = metroOfferSellerApi;
        String s = api.selectShippingGroupById(id).getContent();
        MetroShippingGroupBuilder builder = new MetroShippingGroupBuilder();
        ShippingGroup shippingGroup = builder.web(new JSONObject(s)).build();
        shippingGroup.setOwner(api.accountName());
        SqlService.sqlInsertOrUpdate(shippingGroup, shippingGroupMapper);
        return shippingGroup;
    }
}
