package org.hsgt.pricing.services;

import java.util.List;

public interface CommonService<T> {
    public T queryById(String id);
    List<T> queryById(List<String> ids);
    public List<T> queryAll();
}
