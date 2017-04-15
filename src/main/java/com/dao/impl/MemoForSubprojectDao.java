package com.dao.impl;

import com.dao.BaseDao;
import com.entity.MemoForSubproject;
import com.entity.Pager;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by rthtr on 2017/4/15.
 */
@Repository("MemoForSubprojectDao")
public class MemoForSubprojectDao extends BaseDao{
    /**
     *  0：子项目名  1：项目名  2：内容  3、时间  4、memoId  5、hasRead
     */
    public Pager findEventsExtendForMemo(Pager pagerModel, Integer subprojectId, String searchString){
        String hql="";
        if(searchString==null||searchString.equals("")){
            hql="select m.subprojectId.project.project, m.subprojectId.subproject, m.content, m.operTime, m.id, " +
                    "m.hasRead from MemoForSubproject m where m.subprojectId.subprojectId="+subprojectId+" order by m.operTime asc";
        }else {
            hql="select m.subprojectId.project.project, m.subprojectId.subproject, m.content, m.operTime, m.id,  " +
                    "m.hasRead from MemoForSubproject m where m.subprojectId.subprojectId="+subprojectId+" and " +
                    "(m.subprojectId.project.project like '%"+searchString+"%' or m.subprojectId.subproject " +
                    "like '%"+searchString+"%' or m.content like '%"+searchString+"%') order by m.operTime asc";
        }
        List dataList = this.listByPage(hql, pagerModel.getCurrentPageNumber(), pagerModel.getPageSize(), null, null);
        //      List dataList=findByHql(hql, null,null);
        int count = this.getAllTotal(hql, null, null);
        pagerModel.setTotalSize(count);
        pagerModel.setDataList(dataList);
        return pagerModel;
    }

    public MemoForSubproject getMemoForSubprojectById(Integer id){
        String hql="from MemoForSubproject m where m.id="+id;
        List list = this.findByHql(hql,null,null);
        MemoForSubproject memoForSubproject = (MemoForSubproject)list.get(0);
        return memoForSubproject;
    }

}
