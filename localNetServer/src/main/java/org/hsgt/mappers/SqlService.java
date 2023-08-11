package org.hsgt.mappers;

public class SqlService {

    public static int sqlInsetOrUpdate(Object obj, BaseMapperV2 mapper) {
        int cnt;
        try {
            cnt = mapper.insert(obj);
        } catch (Exception e) {
            cnt = mapper.updateById(obj);
        }
        return cnt;
    }

    public static  int sqlInsertOrSkip(Object obj, BaseMapperV2 mapper) {
        int cnt=0;
        try {
            cnt = mapper.insert(obj);
        } catch (Exception e) {
        }
        return cnt;
    }
}
