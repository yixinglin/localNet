package org.hsgt.pricing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.hsgt.pricing.domain.OfferDO;

public interface OfferMapperMP extends BaseMapper<OfferDO> {

    @Update({"update t_offer set delFlag=0 where productId=#{id}"})
    int recoverById(String id);
}
