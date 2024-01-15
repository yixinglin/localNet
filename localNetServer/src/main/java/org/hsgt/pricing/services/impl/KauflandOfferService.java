package org.hsgt.pricing.services.impl;

import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.services.OfferService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KauflandOfferService implements OfferService {
    @Override
    public Offer queryById(String id) {
        return null;
    }

    @Override
    public List<Offer> queryById(List<String> ids) {
        return null;
    }

    @Override
    public List<Offer> queryAll() {
        return null;
    }

    @Override
    public void updateLowestPriceAndNote(Offer offer) {

    }
}
