package com.service;

import com.dao.impl.RecordDao;
import com.dao.impl.UserDao;
import com.entity.Pager;
import com.entity.RecordEntity;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengqin on 2017/4/8.
 */
@Service
public class RecordService {
    @Autowired
    UserDao userDao;
    @Autowired
    RecordDao recordDao;

    /**
     *新建随手记
     */
    @Transactional
    public boolean createRecord(String content,String openId){
        try {
            RecordEntity record = new RecordEntity(content, openId);
            recordDao.updateRecord(record);
        }catch (Exception e){
            e.getStackTrace();
        }
        return true;
    }
    /**
     * 查看随手记
     */
    public Pager findRecord(String openId,Pager pagerModle){
        String hql="from RecordEntity where openId=:openId";
        Map<String, Object> params = new HashedMap();
        params.put("openId", openId);
        return pagerModle=recordDao.findByPage(hql,pagerModle,params);
    }
    /**
     * 随手记-编辑
     */
    @Transactional
    public boolean updateRecord(String content,int recordId){
        try {
            RecordEntity record=recordDao.get(RecordEntity.class,recordId);
            record.setContent(content);
            recordDao.updateRecord(record);
        }catch (Exception e){
            e.getStackTrace();
        }
        return true;

    }
    /**
     * 获取单个随手记转入任务
     */
    public RecordEntity findByRecordId(int recordId) {
        String hql = "from RecordEntity where recordId=:recordId";
        Map<String, Object> params = new HashedMap();
        params.put("recordId", recordId);
        List<RecordEntity> list = recordDao.findById(params, hql);
        return list.get(0);
    }
    /**
     * 删除随手记
     */
    @Transactional
    public void recordDelete(int recordId){
        try{
            String hql="delete RecordEntity as R where R.recordId=:recordId ";
            Map<String,Object> params=new HashMap<String,Object>();
            params.put("recordId",recordId);
            recordDao.deleteByHql(hql,params,null);
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
