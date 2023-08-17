package org.hsgt.services;

import org.hsgt.entities.pricing.Offer;

public interface OfferService extends CommonService<Offer> {
    void updateLowestPriceAndNote(Offer offer);
}
