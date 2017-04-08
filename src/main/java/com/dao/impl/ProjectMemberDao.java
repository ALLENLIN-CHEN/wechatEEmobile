package com.dao.impl;

import com.dao.BaseDao;
import com.entity.ProjectMember;
import org.springframework.stereotype.Repository;

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
}
