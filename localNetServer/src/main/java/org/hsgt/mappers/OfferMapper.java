package org.hsgt.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hsgt.entities.pricing.Offer;

@Mapper
public interface OfferMapper extends BaseMapper<Offer> {
}
