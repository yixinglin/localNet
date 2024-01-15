package org.hsgt.pricing.servicesV2;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.pricing.BO.Configure;
import org.hsgt.pricing.domain.ConfigureDO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IPricingConfigureService extends IService<ConfigureDO> {
    public List<Configure> listDetails(Wrapper<ConfigureDO> queryWrapper);
    Configure getDetailsById(Serializable id);

    boolean updateConfigureDetailsById(Configure entity);
    boolean updateListConfigureById(Collection<Configure> entityList);
}
