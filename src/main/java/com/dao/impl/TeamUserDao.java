package com.dao.impl;

import com.dao.BaseDao;
import com.entity.Pager;
import com.entity.TeamUser;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by  xionglian on 2017/3/26.
 */
@Repository
public class TeamUserDao extends BaseDao<TeamUser> {
    public List findTeamUsersByOpenId(String openId,Integer role){
        String hql="";
        if(role==0){
            hql="select t.teamId, t.teamName from Team t ";
        }else {
            hql="select t.team.teamId, t.team.teamName, t.role from TeamUser t where t.user.openId like '%"+openId+"%' and t.team.teamId!=1 order by t.team.teamId";
        }
        return this.findByHql(hql, null,null);
    }
    public Pager findTeamUsersByTeamId(Pager pagerModel, Integer teamId, String memberName){
        String hql="";
        if(memberName==null||memberName.equals("")){
            hql="select distinct t.user.openId, t.user.userName from TeamUser t where t.team.teamId="+teamId+" order by t.user.openId";
        }else {
            hql="select distinct t.user.openId, t.user.userName from TeamUser t where t.team.teamId="+teamId+" and t.user.userName='"+memberName+"' order by t.user.openId";
        }
        List dataList = this.listByPage(hql, pagerModel.getCurrentPageNumber(), pagerModel.getPageSize(), null, null);

        int count = this.getAllTotal(hql, null, null);
        pagerModel.setTotalSize(count);
        pagerModel.setDataList(dataList);
        return pagerModel;
    }

    public  List<TeamUser> findTeamUsersByOpenIdAndTeamId(String openId, Integer teamId){
        String hql="from TeamUser t where t.team.teamId="+teamId+" and t.user.openId='"+openId+"'";
        return this.findByHql(hql, null,null);
    }

    public  List<TeamUser> findTeamUsersByTeamId(String teamId){
        String hql="from TeamUser t where t.team.teamId="+teamId;
        return this.findByHql(hql, null,null);
    }

    public List findBy(String hql, Map<String,Object>params){
        return this.findByHql(hql,params,null);
    }
}

