package org.hsgt.pricing.servicesV2;

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
    boolean saveProductPageByApi(String id);
}
