package org.hsgt.pricing.BO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hsgt.pricing.domain.OfferDO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Offer implements Serializable {
    private String id;      // primary key
    private Float price;
    private LocalDateTime datetime;
    private Float lowestPrice;  // manually update
    private String productName;
    private String productKey;
    private Integer amount;
    private Integer quantity;    // to update
    private String unit;
    private String note;    // manually update
    private String manufacturer;
    private ShippingGroup shippingGroup;
    private String platform;
    private Boolean active;

//    public Offer(OfferDO offerDO) {
//        this.id = offerDO.getProductId();
//        this.price = offerDO.getPrice();
//        this.datetime = offerDO.getDatetime();
//        this.lowestPrice = offerDO.getLowestPrice();
//        this.productName = offerDO.getProductName();
//        this.productKey = offerDO.getProductKey();
//        this.amount = offerDO.getAmount();
//        this.quantity = offerDO.getQuantity();
//        this.unit = offerDO.getUnit();
//        this.note = offerDO.getNote();
//        this.manufacturer = offerDO.getManufacturer();
//        this.platform = offerDO.getPlatform();
//        this.active = offerDO.getActive();
//    }

    public static OfferDO convertToOfferDO(Offer item) {
        if (item == null) {
            return null;
        }
        OfferDO result = new OfferDO();
        result.setProductId(item.id);
        result.setPrice(item.getPrice());
        result.setLowestPrice(item.getLowestPrice());
        result.setProductName(item.getProductName());
        result.setDatetime(item.getDatetime());
        result.setNote(item.getNote());
        result.setQuantity(item.getQuantity());
        result.setManufacturer(item.getManufacturer());
        result.setPlatform(item.getPlatform());
        result.setAmount(item.getAmount());
        result.setProductKey(item.getProductKey());
        result.setShippingGroupId(item.getShippingGroup().getId());
        result.setUnit(item.getUnit());
        result.setActive(item.getActive());
        return result;
    }

    public static Offer convertToOffer(OfferDO item) {
        if (item == null) {
            return null;
        }
        Offer result = new Offer();
        result.setId(item.getProductId());
        result.setPrice(item.getPrice());
        result.setDatetime(item.getDatetime());
        result.setLowestPrice(item.getLowestPrice());
        result.setProductName(item.getProductName());
        result.setProductKey(item.getProductKey());
        result.setAmount(item.getAmount());
        result.setQuantity(item.getQuantity());
        result.setUnit(item.getUnit());
        result.setNote(item.getNote());
        result.setManufacturer(item.getManufacturer());
        // result.setShippingGroup();
        result.setPlatform(item.getPlatform());
        result.setActive(item.getActive());
        return result;
    }

}
