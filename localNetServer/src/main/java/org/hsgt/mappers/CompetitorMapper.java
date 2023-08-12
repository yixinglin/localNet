package org.hsgt.mappers;

import org.hsgt.entities.pricing.Competitor;

import java.util.List;

public interface CompetitorMapper extends BaseMapperV2<Competitor> {
    public List<Competitor> findAllCompetitorByProductId(String productId);
}
