package com.dao.impl;

import com.dao.BaseDao;
import com.entity.ProjectMember;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zengqin on 2017/4/6.
 */
@Repository
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

}
