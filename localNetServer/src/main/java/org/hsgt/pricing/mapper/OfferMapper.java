package org.hsgt.pricing.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.hsgt.core.mapper.BaseMapperV2;
import org.hsgt.pricing.domain.Offer;
import org.hsgt.pricing.domain.pricing.UpdatedOffer;

@Mapper
public interface OfferMapper extends BaseMapperV2<Offer> {
    public int updateLowestPriceAndNote(Offer offer);
    public int insertUpdatedPricingLog(UpdatedOffer updatedOffer);
}
