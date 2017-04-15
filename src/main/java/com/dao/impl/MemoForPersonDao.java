package com.dao.impl;

import com.dao.BaseDao;
import com.entity.MemoForPerson;
import com.entity.Pager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rthtr on 2017/4/15.
 */
@Repository("MemoForPersonDao")
public class MemoForPersonDao extends BaseDao{
    /**
     *  0：子项目名  1：项目名  2：内容  3、时间  4、memoId   5、hasRead
     */
    public Pager findPersonMemo(Pager pagerModel, String openId, String searchString){
        String hql="";
        if(searchString==null||searchString.equals("")){
            hql="select m.subprojectId.project.project, m.subprojectId.subproject, m.content, m.operTime, m.id, " +
                    "m.hasRead from MemoForPerson m where m.openId.openId='"+openId+"' order by m.operTime asc";
        }else {
            hql="select m.subprojectId.project.project, m.subprojectId.subproject, m.content, m.operTime, m.id,  " +
                    "m.hasRead from MemoForPerson m where m.openId.openId='"+openId+"' and " +
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

    public MemoForPerson getMemoForPersoById(Integer id){
        String hql="from MemoForPerson m where m.id="+id;
        List list = this.findByHql(hql,null,null);
        MemoForPerson memoForPerson = (MemoForPerson)list.get(0);
        return memoForPerson;
    }

    public int getCountForPersonMemos(String openId){
        String hql="from MemoForPerson m where m.hasRead=0 and m.openId.openId='"+openId+"'";
        List list=this.findByHql(hql,null,null);
        return list.size();
    }
}
