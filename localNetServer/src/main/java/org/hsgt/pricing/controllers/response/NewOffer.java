package org.hsgt.pricing.controllers.response;

import lombok.Data;
import org.hsgt.pricing.BO.Offer;

@Data
public class NewOffer {
    String productId;
    float price;
    int quantity;
    String shippingGroupId;
    String token;

    public static NewOffer from(Offer offer) {
        NewOffer newOffer = new NewOffer();
        newOffer.setProductId(offer.getId());
        newOffer.setPrice(offer.getPrice());
        newOffer.setQuantity(offer.getQuantity());
        newOffer.setShippingGroupId(offer.getShippingGroup().getId());
        return newOffer;
    }

}


