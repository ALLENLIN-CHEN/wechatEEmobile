package com.dao.impl;

import com.dao.BaseDao;
import com.entity.Pager;
import com.entity.Subproject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by  xionglian on 2017/3/25.
 */
@Repository("SubprojectDao")
public class SubprojectDao extends BaseDao<Subproject> {
    public boolean saveSubproject(Subproject subproject) {
        try {
            this.save(subproject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
    public Subproject findById(int subprojectId){
        return this.get(Subproject.class,subprojectId);
    }

    public List findSubProjectListByProjectId(Integer projectId){
        String hql="select s.subprojectId, s.subproject from Subproject s where s.project.projectId="+projectId;
        return this.findByHql(hql,null,null);
    }

    /**
     * 根据teamId搜索该团队所有的子项目
     * 0：子项目id  1：子项目名称  2：项目id  3：项目名称
     */
    public Pager findSubprojectListByTeamId(Pager pagerModel, Integer teamId, String subprojectName, String subprojectStatus){
        String hql="";
        if ((subprojectName==null||subprojectName.equals(""))&&(subprojectStatus==null||subprojectStatus.equals(""))){
            hql="select s.subprojectId, s.subproject, s.project.projectId, s.project.project from"
                    +" Subproject s where s.project.team.teamId="+teamId+" order by s.project.projectId asc";
        }else if(subprojectStatus==null||subprojectStatus.equals("")){
            hql="select s.subprojectId, s.subproject, s.project.projectId, s.project.project from"
                    +" Subproject s where s.project.team.teamId="+teamId+" and s.subproject like '%"
                    +subprojectName+"%' order by s.project.projectId asc";
        }else if(subprojectName==null||subprojectName.equals("")){
            hql="select s.subprojectId, s.subproject, s.project.projectId, s.project.project from"
                    +" Subproject s where s.project.team.teamId="+teamId+" and s.teamStatus like '%"
                    +subprojectStatus+"%' order by s.project.projectId asc";
        }else{
            hql="select s.subprojectId, s.subproject, s.project.projectId, s.project.project from"
                    +" Subproject s where s.project.team.teamId="+teamId+" and s.teamStatus like '%"
                    +subprojectStatus+"%' and s.subproject like '%"+subprojectName+"%' order by s.project.projectId asc";
        }
        List dataList = this.listByPage(hql, pagerModel.getCurrentPageNumber(), pagerModel.getPageSize(), null, null);
        //      List dataList=findByHql(hql, null,null);
        int count = this.getAllTotal(hql, null, null);
        pagerModel.setTotalSize(count);
        pagerModel.setDataList(dataList);
        return pagerModel;
    }

}
