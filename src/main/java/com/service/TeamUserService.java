package com.service;

import com.dao.impl.TeamUserDao;
import com.entity.TeamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rthtr on 2017/4/2.
 */
@Service
public class TeamUserService {
    @Autowired
    TeamUserDao teamUserDao;

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

    public TeamUser findTeamUsersByOpenIdAndTeamId(String openId, Integer teamId){
        List <TeamUser> list = teamUserDao.findTeamUsersByOpenIdAndTeamId(openId,teamId);
        return list.get(0);
    }
}
