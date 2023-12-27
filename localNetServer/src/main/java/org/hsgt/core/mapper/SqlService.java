package org.hsgt.core.mapper;

import org.hsgt.core.mapper.BaseMapperV2;

public class SqlService {

    public static int sqlInsertOrUpdate(Object obj, BaseMapperV2 mapper) {
        int cnt;
        try {
            cnt = mapper.insert(obj);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            cnt = mapper.updateById(obj);
        }
        return cnt;
    }

    public static  int sqlInsertOrSkip(Object obj, BaseMapperV2 mapper) {
        int cnt=0;
        try {
            cnt = mapper.insert(obj);
        } catch (org.springframework.dao.DuplicateKeyException e) {
        }
        return cnt;
    }
}
