package org.hsgt.pricing.mapper;

import org.hsgt.core.mapper.BaseMapperV2;
import org.hsgt.pricing.domain.pricing.Competitor;

import java.util.List;

public interface CompetitorMapper extends BaseMapperV2<Competitor> {
    public List<Competitor> findAllCompetitorByProductId(String productId);
    // public int deleteAllCompetitorByProductId(String productId);
}
