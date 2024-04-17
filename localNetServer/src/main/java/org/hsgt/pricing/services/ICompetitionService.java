package org.hsgt.pricing.services;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.pricing.BO.Competitor;
import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.domain.CompetitionDO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ICompetitionService extends IService<CompetitionDO> {
     List<Competitor> listDetails(Wrapper<CompetitionDO> queryWrapper);
     List<ProductPage> listProductPageDetailsByIds(Collection<? extends Serializable> idList);

     ProductPage getProductPageDetailsById(Serializable id);

     /**
      * @param id:
      * @return boolean
      * @author Lin
      * @description Fetch data from API and save it to database.
      * @date 26.Jan.2024 026 21:11
      */
   // boolean saveProductPageByApi(String id);
}
