package org.hsgt.pricing.services;

import java.util.List;

public interface CommonService<T> {
     T queryById(String id);
    List<T> queryById(List<String> ids);
     List<T> queryAll();
}
