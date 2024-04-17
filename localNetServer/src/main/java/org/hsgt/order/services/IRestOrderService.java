package org.hsgt.order.services;

import java.io.Serializable;

public interface IRestOrderService {

    boolean saveOrUpdateByMetroAPI(Serializable id);
    boolean saveOrUpdateByKauflandAPI(Serializable id);
}
