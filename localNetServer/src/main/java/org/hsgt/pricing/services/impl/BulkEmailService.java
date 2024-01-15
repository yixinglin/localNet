package org.hsgt.pricing.services.impl;

import org.hsgt.pricing.BO.BulkEmailContact;
import org.hsgt.pricing.mapper.BulkEmailMapper;
import org.hsgt.core.mapper.SqlService;
import org.hsgt.pricing.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkEmailService implements CommonService<BulkEmailContact> {

    @Autowired
    BulkEmailMapper bulkEmailMapper;
    @Override
    public List<BulkEmailContact> queryAll() {
        List<BulkEmailContact> bulkEmailContactList = bulkEmailMapper.selectList(null);
        return bulkEmailContactList;
    }

    public int insertOrSkip(BulkEmailContact bulkEmailContact) {
        int n = 0;
        if (this.verify(bulkEmailContact)) {
            n = SqlService.sqlInsertOrSkip(bulkEmailContact, bulkEmailMapper);
        }
        return n;
    }

    public int insertOrUpdate(BulkEmailContact bulkEmailContact) {
        int n = 0;
        if (this.verify(bulkEmailContact)) {
            n = SqlService.sqlInsertOrUpdate(bulkEmailContact, bulkEmailMapper);
        }
        return n;
    }

    private boolean verify(BulkEmailContact bulkEmailContact) {
        return true;
    }
    @Override
    public BulkEmailContact queryById(String id) {
        BulkEmailContact bulkEmailContact = new BulkEmailContact();
        bulkEmailContact.setEmail(id);
        BulkEmailContact ans = bulkEmailMapper.selectById(bulkEmailContact);
        return ans;
    }

    @Override
    public List<BulkEmailContact> queryById(List<String> ids) {
        return null;
    }


}
