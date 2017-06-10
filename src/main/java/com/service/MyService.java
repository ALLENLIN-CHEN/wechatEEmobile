package com.service;

import com.dao.impl.ProjectDao;
import com.entity.Pager;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengqin on 2017/4/8.
 */
@Service
public class MyService {
    /**
     * 查询我的已转移任务
     */
    @Autowired
    private ProjectDao projectDao;
    public Pager findMyTransferSchedule(String openId,Pager pager){
        String hql="select new com.entity.newT.ScheduleT(task.taskContent, task.taskReply, task.taskType, task.scheduleId,p.projectId, p.project,sub.subprojectId, sub.subproject,task.taskTime) " +
                "from Subproject sub left join sub.project p left join sub.schedules task left join task.transferEntities transfer" +
                " where transfer.user.openId=:openId ";
        Map<String,Object> params=new HashedMap();
        params.put("openId",openId);
        return projectDao.findByPage(hql,pager,params);
    }
    /**
     * 查询未完成/超期任务
     */
    public Pager findUndoSchedules(String openId,Pager pagerModel) {
        String hql = "select new com.entity.newT.ScheduleT(task.taskContent, task.taskReply, task.taskType, task.scheduleId,p.projectId, p.project, sub.subprojectId, sub.subproject,task.taskTime)"
                + "from Subproject sub left join sub.project p left join sub.schedules task "
                + "where task.scheduleId in(select s.scheduleId from ScheduleMember sc left join sc.schedule s left join sc.user u where u.openId=:openId and s.taskStatus!=:taskStatus )";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        params.put("taskStatus",'a');
        return projectDao.findByPage(hql, pagerModel, params);
    }
    /**
     * 查询我的任务的所有日期
     */
    public List findScheduleTime(String openId){
        String hql="select distinct(s.taskTime) from Schedule s left join s.scheduleMembers sc " +
                "where sc.user.openId=:openId ";
        Map<String,Object>params=new HashMap<>();
        params.put("openId",openId);
        List list=projectDao.findByHql(hql,params,null);
        return list;
    }
    /**
     * 根据日期查询任务
     */
    public List findByTime(Date taskTime,String openId){
        String hql="select new com.entity.newT.ScheduleT(task.taskContent, task.taskReply, task.taskType, task.scheduleId,p.projectId, p.project, sub.subprojectId, sub.subproject,task.taskTime)"
                + "from Subproject sub left join sub.project p left join sub.schedules task "
                + "where task.scheduleId in(select s.scheduleId from ScheduleMember sc left join sc.schedule s left join sc.user u where u.openId=:openId and s.taskTime=:taskTime )";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        params.put("taskTime",taskTime);
        return projectDao.findByHql(hql,params,null);
    }
}
