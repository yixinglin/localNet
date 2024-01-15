package org.hsgt.pricing.services;

import org.hsgt.pricing.BO.Offer;

public interface OfferService extends CommonService<Offer> {
    void updateLowestPriceAndNote(Offer offer);
}
