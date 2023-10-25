package org.hsgt.services;

import org.hsgt.controllers.response.NewOffer;
import org.hsgt.controllers.response.SuggestedPrice;
import org.hsgt.entities.pricing.Configure;

import java.util.List;

public interface PriceManagementService {
    public List<Configure> queryAllConfigurations();
    public void updateConfiguration(List<Configure> conf);
    public SuggestedPrice suggestPriceUpdate(String productId);

    public NewOffer pricing(NewOffer latestOffer, String ip);
}
