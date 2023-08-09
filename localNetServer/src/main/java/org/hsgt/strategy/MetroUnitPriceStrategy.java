package org.hsgt.strategy;

import lombok.Data;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;

import java.util.List;

// 商品最低单价策略
@Data
public class MetroUnitPriceStrategy implements Strategy {
    private int state;

    @Override
    public Competitor execute(Competitor me, List<Competitor> competitorList, Offer offer) {
        return null;
    }

}
