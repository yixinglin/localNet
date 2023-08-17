package org.hsgt.services.impl;

import org.hsgt.entities.pricing.Offer;
import org.hsgt.services.OfferService;
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
