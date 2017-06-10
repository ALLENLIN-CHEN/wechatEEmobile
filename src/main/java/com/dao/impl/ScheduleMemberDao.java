package com.dao.impl;

import com.dao.BaseDao;
import com.entity.Pager;
import com.entity.ScheduleMember;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by  xionglian on 2017/3/25.
 */
@Repository
public class ScheduleMemberDao extends BaseDao<ScheduleMember>{

    public ScheduleMember getCertain(String hql, Map<String, Object> params, Query query)
    {
        ScheduleMember scheduleMember = this.findByHql(hql, params, query).get(0);

        return scheduleMember;
    }

    /**
     * @author rthtr 2017/4/3
     * 统计超期任务数:taskStatus='d'
     */
    public List findTaskOverTimeForPerson(Integer teamId, String memberName, String startTime, String endTime){
        String hql="select count (*) from ScheduleMember s where ((s.schedule.taskTime>='"+startTime
                +"' and s.schedule.taskTime<='"+endTime+"') or (s.schedule.taskStartTime>='"+startTime
                +"' and s.schedule.taskStartTime<='"+endTime+"')) and s.schedule.taskStatus='d'"
                +" and s.user.userName='"+memberName+"' and s.schedule.subproject.project.team.teamId="+teamId;

        /*String hql="select count (*) from ScheduleMember s where s.user.userName='"+memberName+"'";*/

        return this.findByHql(hql, null,null);
    }

    /**
     * 统计未完成任务数:taskStatus=a,b,f
     */
    public List findUnfinishedForPerson(Integer teamId, String memberName,String startTime, String endTime){
        String hql="select count (*) from ScheduleMember s where ((s.schedule.taskTime>='"+startTime
                +"' and s.schedule.taskTime<='"+endTime+"') or (s.schedule.taskStartTime>='"+startTime
                +"' and s.schedule.taskStartTime<='"+endTime+"')) and (s.schedule.taskStatus='a'"
                +" or s.schedule.taskStatus='b' or s.schedule.taskStatus='f')"
                +" and s.user.userName='"+memberName+"' and s.schedule.subproject.project.team.teamId="+teamId;

        return this.findByHql(hql, null,null);
    }


    /**
     *@author rthtr 2017/4/3
     * 0：任务Id  1：项目名称  2：子项目名称  3：任务名称  4：标签  5：留言
     * 6：截止日期  7：任务状态  8：子项目id  9：项目id
     */
    public Pager findTaskIntensityStatisticsForPerson(Pager pagerModel, Integer teamId, String memberOpenId, String startTime, String endTime){
        String hql="select s.schedule.scheduleId, s.schedule.subproject.project.project, s.schedule.subproject.subproject, s.schedule.taskContent,"
                +"s.schedule.taskType, s.schedule.taskReply, s.schedule.taskTime, s.schedule.taskStatus, s.schedule.subproject.subprojectId,"
                +" s.schedule.subproject.project.projectId from ScheduleMember s where ((s.schedule.taskTime>='"+startTime
                +"' and s.schedule.taskTime<='"+endTime+"') or (s.schedule.taskStartTime>='"+startTime
                +"' and s.schedule.taskStartTime<='"+endTime+"')) and (s.schedule.taskStatus!='c' and s.schedule.taskStatus!='e')"
                +" and s.user.openId='"+memberOpenId+"' and s.schedule.subproject.project.team.teamId="+teamId;
        List dataList = this.listByPage(hql, pagerModel.getCurrentPageNumber(), pagerModel.getPageSize(), null, null);
        //      List dataList=findByHql(hql, null,null);
        int count = this.getAllTotal(hql, null, null);
        pagerModel.setTotalSize(count);
        pagerModel.setDataList(dataList);

        return pagerModel;
    }

    /**
     * @author rthtr 2017/4/16
     * 统计某天超期任务数:taskStatus='d'
     */
    public List getCountTaskOverTimeForPerson(Integer teamId, String openId,String date){
        String hql="select count (1) from ScheduleMember s where s.schedule.taskTime='"+date+"' " +
                "and s.schedule.taskStatus='d' and s.user.openId='"+openId+"' and " +
                "s.schedule.subproject.project.team.teamId="+teamId;

        return this.findByHql(hql, null,null);
    }

    /**
     * 统计某天未完成任务数:taskStatus=a,b,f
     */
    public List getCountUnfinishedForPerson(Integer teamId, String openId,String date){
        String hql="select count (1) from ScheduleMember s where s.schedule.taskTime='"+date+"' " +
                "and (s.schedule.taskStatus='a' or s.schedule.taskStatus='b' or s.schedule.taskStatus='f') " +
                "and s.user.openId='"+openId+"' and s.schedule.subproject.project.team.teamId="+teamId;

        return this.findByHql(hql, null,null);
    }

    /**
     * 统计某天已完成任务数:taskStatus=c
     */
    public List getCountFinishedForPerson(Integer teamId, String openId,String date){
        String hql="select count (1) from ScheduleMember s where s.schedule.taskTime='"+date+"' " +
                "and s.schedule.taskStatus='c' and s.user.openId='"+openId+"' and " +
                "s.schedule.subproject.project.team.teamId="+teamId;

        return this.findByHql(hql, null,null);
    }


    /**
     * @author rthtr 2017/4/16
     * 统计超期任务数:taskStatus='d'
     */
    public List findTaskOverTimeForPersonByOpenId(Integer teamId, String openId, String startTime, String endTime){
        String hql="select count (*) from ScheduleMember s where s.schedule.taskTime>'"+startTime
                +"' and s.schedule.taskTime<='"+endTime+"' and s.schedule.taskStatus='d'"
                +" and s.user.openId='"+openId+"' and s.schedule.subproject.project.team.teamId="+teamId;

        /*String hql="select count (*) from ScheduleMember s where s.user.userName='"+memberName+"'";*/

        return this.findByHql(hql, null,null);
    }

    /**
     * 统计未完成任务数:taskStatus=a,b,f
     */
    public List findUnfinishedForPersonByOpenId(Integer teamId, String openId,String startTime, String endTime){
        String hql="select count (*) from ScheduleMember s where s.schedule.taskTime>'"+startTime
                +"' and s.schedule.taskTime<='"+endTime+"' and (s.schedule.taskStatus='a'"
                +" or s.schedule.taskStatus='b' or s.schedule.taskStatus='f')"
                +" and s.user.openId='"+openId+"' and s.schedule.subproject.project.team.teamId="+teamId;

        return this.findByHql(hql, null,null);
    }

    /**
     * 统计某天已完成任务数:taskStatus=c
     */
    public List findFinishedForPersonByOpenId(Integer teamId, String openId,String startTime, String endTime){
        String hql="select count (*) from ScheduleMember s where s.schedule.taskTime>'"+startTime
                +"' and s.schedule.taskTime<='"+endTime+"' and s.schedule.taskStatus='c'"
                +" and s.user.openId='"+openId+"' and s.schedule.subproject.project.team.teamId="+teamId;

        return this.findByHql(hql, null,null);
    }

}
