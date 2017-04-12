package com.service;

import com.dao.impl.ProjectMemberDao;
import com.dao.impl.TeamUserDao;
import com.entity.ProjectMember;
import com.entity.TeamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List findTeamUsersByOpenId(String openId){
        int role=10;//非超级管理员
        List list =teamUserDao.findTeamUsersByOpenId(openId,role);
        for(int i=0;i<list.size();i++){
            Object [] row=(Object[]) list.get(i);
            if (Integer.parseInt(row[2].toString())==0){
                role=0;
                break;
            }
        }
        return teamUserDao.findTeamUsersByOpenId(openId,role);
    }

    public List<TeamUser> findTeamUsersByOpenIdAndTeamId(String openId, Integer teamId){
        List <TeamUser> list = teamUserDao.findTeamUsersByOpenIdAndTeamId(openId,teamId);
        return list;
    }
    /**
     * 查询用户对子项目的修改权限
     */
public ProjectMember findP(String openId,int subprojectId){
     String hq="from ProjectMember p where p.user.openId=:openId and p.subproject.subprojectId=:subprojectId";
     Map<String,Object> params=new HashMap<>();
     params.put("openId",openId);
     params.put("subprojectId",subprojectId);
     List<ProjectMember> projectMemberList=teamUserDao.findBy(hq,params);
     ProjectMember projectMember=projectMemberList.get(0);
    return projectMember;
     }

}

