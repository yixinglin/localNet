package org.hsgt.pricing.BO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hsgt.pricing.domain.ShippingGroupDO;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ShippingGroup implements Serializable {

    private String id;  // Shipping Group ID
    private String groupName;        // Shipping group name.
    private Float unitCost;         //  Shipping cost from
    private Float extraUnitCost;    //  Extra shipping cost when buying more items

    private Float maxShippingCost;
    private Float freeFrom;         // Free shipping from

    private Integer minTransportDays; // Minimum transport time in working days
    private Integer maxTransportDays; // Maximum transport time in working days
    private String destCountry;   // Destination country.
    private String platform;
    private String owner;

    public static ShippingGroup free() {
        ShippingGroup sg = new ShippingGroup();
        sg.id = "0";
        sg.groupName = "free";
        sg.unitCost = 0f;
        sg.extraUnitCost = 0f;
        sg.maxShippingCost = 99999f;
        sg.freeFrom = 99999f;
        sg.minTransportDays = 0;
        sg.maxTransportDays = 0;
        sg.owner = "any";
        sg.platform = "any";
        return sg;
    }

    public ShippingGroup(ShippingGroupDO shippingGroupDO) {
        this.id = shippingGroupDO.getId();
        this.groupName = shippingGroupDO.getName();
        this.unitCost = shippingGroupDO.getUnitCost();
        this.extraUnitCost = shippingGroupDO.getExtraUnitCost();
        this.maxShippingCost = shippingGroupDO.getMaxShippingCost();
        this.freeFrom = shippingGroupDO.getFreeFrom();
        this.minTransportDays = shippingGroupDO.getMinTransportDays();
        this.maxTransportDays = shippingGroupDO.getMaxTransportDays();
        this.destCountry = shippingGroupDO.getDestCountry();
        this.platform = shippingGroupDO.getPlatform();
        this.owner = shippingGroupDO.getOwner();
    }

    public static ShippingGroup convertToBO(ShippingGroupDO item) {
        if (item == null) {
            return null;
        }
        ShippingGroup result = new ShippingGroup();
        result.setId(item.getId());
        result.setGroupName(item.getName());
        result.setUnitCost(item.getUnitCost());
        result.setExtraUnitCost(item.getExtraUnitCost());
        result.setMaxShippingCost(item.getMaxShippingCost());
        result.setFreeFrom(item.getFreeFrom());
        result.setMinTransportDays(item.getMinTransportDays());
        result.setMaxTransportDays(item.getMaxTransportDays());
        result.setDestCountry(item.getDestCountry());
        result.setPlatform(item.getPlatform());
        result.setOwner(item.getOwner());
        return result;
    }


    public static ShippingGroupDO convertToDO(ShippingGroup item, Boolean logicDel) {
        if (item == null) {
            return null;
        }
        ShippingGroupDO result = new ShippingGroupDO();
        result.setId(item.getId());
        result.setName(item.getGroupName());
        result.setUnitCost(item.getUnitCost());
        result.setExtraUnitCost(item.getExtraUnitCost());
        result.setMaxShippingCost(item.getMaxShippingCost());
        result.setFreeFrom(item.getFreeFrom());
        result.setMinTransportDays(item.getMinTransportDays());
        result.setMaxTransportDays(item.getMaxTransportDays());
        result.setDestCountry(item.getDestCountry());
        result.setPlatform(item.getPlatform());
        result.setOwner(item.getOwner());
        result.setLogicDel(logicDel);
        return result;
    }

    public static ShippingGroupDO convertToDO(ShippingGroup item) {
        return convertToDO(item, false);
    }

}
