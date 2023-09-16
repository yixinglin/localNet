package org.hsgt.services;

import org.hsgt.controllers.response.SuggestedPrice;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;

import java.util.List;

public interface PriceManagementService {
    public List<Configure> queryAllConfigurations();
    public void updateConfiguration(Configure conf);
    public SuggestedPrice suggestPriceUpdate(String productId);

    public Offer pricing(Offer latestOffer);
}
