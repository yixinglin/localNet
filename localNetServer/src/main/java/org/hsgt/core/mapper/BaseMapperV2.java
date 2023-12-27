package org.hsgt.core.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapperV2<T> {

    List<T> selectList(Object wrapper);
    T selectById(Serializable id);
    T selectById(T id);
    T selectByMap(Map<String, Object> map);

    int insert(T entity);

    int deleteById(Serializable id);
    int deleteById(T id);

    // int updateById(Serializable id);
    int updateById(T id);
    // int updateByMap(Map<String, Object> map);
    int count();

}
