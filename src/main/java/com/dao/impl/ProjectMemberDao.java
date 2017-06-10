package com.dao.impl;

import com.dao.BaseDao;
import com.entity.Pager;
import com.entity.ProjectMember;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zengqin on 2017/4/6.
 */
@Repository("ProjectMemberDao")
public class ProjectMemberDao extends BaseDao<ProjectMember> {
    public boolean saveProjectMember(ProjectMember projectMember) {
        try {
            this.save(projectMember);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public List findManpowerForSubproject(Integer subprojectId){
        String hql="select count(*) from ProjectMember p where p.subproject.subprojectId="+subprojectId;
        return this.findByHql(hql, null,null);
    }

    /**
     * 0：子项目id  1：子项目名  2：项目id  3：项目名  4：子项目所处阶段
     */
    public Pager findEventsForMemo(Pager pagerModel, String openId, String searchString){
        String hql="";
        if(searchString==null||searchString.equals("")){
            hql="select p.subproject.subprojectId, p.subproject.subproject, p.subproject.project.projectId, " +
                    "p.subproject.project.project, p.subproject.teamStatus from ProjectMember p where p.user.openId='"+openId+"'";
        }else {
            hql="select p.subproject.subprojectId, p.subproject.subproject, p.subproject.project.projectId, " +
                    "p.subproject.project.project, p.subproject.teamStatus from ProjectMember p where p.user.openId='"+openId+
                    "' and (p.subproject.subproject like '%"+searchString+"%' or p.subproject.project.project like '%"+searchString+
                    "%' or p.subproject.teamStatus like '%"+searchString+"%')";
        }
        List dataList = this.listByPage(hql, pagerModel.getCurrentPageNumber(), pagerModel.getPageSize(), null, null);
        //      List dataList=findByHql(hql, null,null);
        int count = this.getAllTotal(hql, null, null);
        pagerModel.setTotalSize(count);
        pagerModel.setDataList(dataList);
        return pagerModel;
    }

    /**
     * 根据子项目Id获取子项目负责人
     */
    public List getPrincipalListBySubprojectId(Integer subprojectId){
        String hql="select p.user.userName from ProjectMember p where p.subproject.subprojectId="+subprojectId;
        List dataList=findByHql(hql, null,null);

        return dataList;
    }

    /**
     * 根据subprojectId和openId获取实例
     */
    public ProjectMember findByOpenIdAndSubprojectId(String openId, Integer subprojectId){
        String hql="from ProjectMember p where p.subproject.subprojectId="+subprojectId
                +" and p.user.openId='"+openId+"'";
        List <ProjectMember>list=findByHql(hql, null,null);
        return list.get(0);
    }


}
