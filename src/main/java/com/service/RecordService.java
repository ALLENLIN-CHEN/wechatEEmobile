package com.service;

import com.dao.impl.RecordDao;
import com.dao.impl.UserDao;
import com.entity.Pager;
import com.entity.RecordEntity;
import com.entity.User;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean createRecord(String content,String openId){
        try {
            String hql = "from User where openId=:openId";
            Map<String, Object> params = new HashedMap();
            params.put("openId", openId);
            List<User> list = userDao.findBy(hql, params);
            User user = list.get(0);
            RecordEntity record = new RecordEntity(content, user);
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
        String hql="from RecordEntity r left join r.user u where u.openId=:openId";
        Map<String, Object> params = new HashedMap();
        params.put("openId", openId);
        return pagerModle=recordDao.findByPage(hql,pagerModle,params);
    }
    /**
     * 随手记-编辑
     */
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
}
