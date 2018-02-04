package com.service;

import com.dao.impl.ProjectMemberDao;
import com.dao.impl.TeamUserDao;
import com.entity.ProjectMember;
import com.entity.TeamUser;
import com.entity.newT.TeamUserT2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rthtr on 2017/4/2.
 */
@Service
public class TeamUserService {
    @Autowired
    TeamUserDao teamUserDao;
    @Autowired
    ProjectMemberDao projectMemberDao;

    public List findTeamUsersByOpenId(String openId) {
        int role = 10;//非超级管理员
        List list = teamUserDao.findTeamUsersByOpenId(openId, role);
        List result = new ArrayList();
        result.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            Object[] row = (Object[]) list.get(i);
            Object[] rowPrev = (Object[]) list.get(i - 1);
            if (!row[0].equals(rowPrev[0])) {
                result.add(list.get(i));
            }
        }
        return result;
    }

    public List<TeamUserT2> findTeamUsersListByOpenId(String openId) {
        List<TeamUserT2> list = teamUserDao.findTeamUsersLeaderByOpenId(openId);
        System.out.println(list);
        for (TeamUserT2 team : list) {
            int teamid = team.getTeamId();
            List list1 = teamUserDao.findcount(teamid);
            team.setCount((long) list1.get(0));
        }
        return list;
    }

    public List<TeamUser> findTeamUsersByOpenIdAndTeamId(String openId, Integer teamId) {
        List<TeamUser> list = teamUserDao.findTeamUsersByOpenIdAndTeamId(openId, teamId);
        return list;
    }

    public List<TeamUser> findTeamUsersByTeamId(String teamId) {
        List<TeamUser> list = teamUserDao.findTeamUsersByTeamId(teamId);
        return list;
    }

    public void updateAdmin(String openId,int teamId) {
        teamUserDao.updateAdmin(openId,teamId);
    }

    /**
     * 查询用户对子项目的修改权限
     */
    public ProjectMember findP(String openId, int subprojectId) {
        String hq = "from ProjectMember p where p.user.openId=:openId and p.subproject.subprojectId=:subprojectId";
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openId);
        params.put("subprojectId", subprojectId);
        List<ProjectMember> projectMemberList = teamUserDao.findBy(hq, params);
        ProjectMember projectMember = null;
        if (projectMemberList != null && !projectMemberList.isEmpty())
            projectMember = projectMemberList.get(0);
        return projectMember;
    }

}

