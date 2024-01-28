package org.hsgt.pricing.services.impl;

//public class CommonServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl {
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean saveOrIgnore(T entity) {
//        if (null != entity) {
//            TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
//            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
//            String keyProperty = tableInfo.getKeyProperty();
//            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
//            Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
//            return StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal)) ? save(entity) : true;
//        }
//        return false;
//    }
//
//}
