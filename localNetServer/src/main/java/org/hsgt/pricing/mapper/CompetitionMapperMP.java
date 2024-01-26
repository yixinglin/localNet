package org.hsgt.pricing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.hsgt.pricing.domain.CompetitionDO;

public interface CompetitionMapperMP extends BaseMapper<CompetitionDO> {
    @Update({"update t_competition set delFlag=0 where productId=#{id}"})
    int recoverById(String id);
}
