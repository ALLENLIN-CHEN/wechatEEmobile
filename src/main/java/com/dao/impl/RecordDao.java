package com.dao.impl;

import com.dao.BaseDao;
import com.entity.Pager;
import com.entity.RecordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zengqin on 2017/4/7.
*/
@Repository("RecordDao")
public class RecordDao extends BaseDao<RecordEntity> {
    /**
     * 新建或保存随手记
     */
    public boolean updateRecord(RecordEntity recordEntity){
        try{
            this.saveOrUpdate(recordEntity);
        }catch (Exception e){
            e.getStackTrace();
        }
        return true;
    }
    /**
     * 分页查询
     */
    public Pager findByPage(String hql, Pager pagerModel, Map<String, Object> params){
        List dataList = this.listByPage(hql, pagerModel.getCurrentPageNumber(), pagerModel.getPageSize(), params, null);
        int count = this.getAllTotal(hql, params, null);
        pagerModel.setTotalSize(count);
        pagerModel.setDataList(dataList);
        return pagerModel;
    }

    /**
     * 获取单个随手记
     * @return
     */
    public List findById(Map<String,Object> params,String hql){
        return this.findByHql(hql,params,null);
    }

}

