package org.hsgt.mappers;
import org.apache.ibatis.annotations.Mapper;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.entities.pricing.UpdatedOffer;

@Mapper
public interface OfferMapper extends BaseMapperV2<Offer> {
    public int updateLowestPriceAndNote(Offer offer);
    public int insertUpdatedPricingLog(UpdatedOffer updatedOffer);
}
