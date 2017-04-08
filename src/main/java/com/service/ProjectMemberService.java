package com.service;

import com.dao.impl.ProjectMemberDao;
import com.entity.ProjectMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zengqin on 2017/4/6.
 */
@Service
public class ProjectMemberService {
    ProjectMemberDao projectMemberDao;

    @Transactional
    public boolean saveProjectMember(ProjectMember projectMember){
        return projectMemberDao.saveProjectMember(projectMember);
    }
}
