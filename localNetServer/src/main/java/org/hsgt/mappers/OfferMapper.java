package org.hsgt.mappers;
import org.apache.ibatis.annotations.Mapper;
import org.hsgt.entities.pricing.Offer;

@Mapper
public interface OfferMapper extends BaseMapperV2<Offer> {
    public int updateLowestPriceAndNote(Offer offer);
}
