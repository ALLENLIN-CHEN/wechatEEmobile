package com.dao.impl;

import com.dao.BaseDao;
import com.entity.Pager;
import com.entity.Project;
import com.entity.ProjectMember;
import com.entity.Team;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public List findManpowerMemberForSubproject(Integer subprojectId){
        String hql="select p.user.openId as openId, p.user.userName as userName, p.roleType as roleType " +
                "from ProjectMember p where p.subproject.subprojectId="+subprojectId;
        Query query = this.getCurrentSession().createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return this.findByHql(hql, null,query);
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

    /**
     *查询团队现有人员
     */
    @Transactional
    public List teamMembers(String teamid){

        String sql = "select  DISTINCT (projectMember.openId),user.userName,tagContent, tagRelation.tagType,tagRelation.tagId  from projectMember inner join user on projectMember.openId = user.openId\n" +
                "  LEFT JOIN tagRelation ON tagRelation.wechatId = projectMember.openId\n" +
                "  left JOIN tagDict ON tagDict.tagId = tagRelation.tagId\n" +
                "  where subprojectId in\n" +
                "        (select subprojectId from subproject where projectId in (select projectId from project where team = 2))\n" +
                "        and (tagRelation.tagType = \"人员绑定\" OR isnull(tagRelation.tagType)) ;";

        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        //设定结果结果集中的每个对象为Map类型
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return this.excuteBySQL(sql,null,query);
    }


}
