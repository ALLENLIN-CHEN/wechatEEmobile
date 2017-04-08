package com.service;

import com.dao.ProjectDaoI;
import com.entity.Pager;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ProjectDaoI projectDao;
    public Pager findMyTransferSchedule(String openId,Pager pager){
        String hql="select new com.entity.newT.ScheduleT(task.taskContent, task.taskReply, task.taskType, task.scheduleId,p.projectId, p.project,sub.subprojectId, sub.subproject,task.taskTime) " +
                "from Subproject sub left join sub.project p left join sub.schedules task left join task.transferEntities transfer" +
                " where transfer.user.openId=:openId ";
        Map<String,Object> params=new HashedMap();
        params.put("openId",openId);
        return projectDao.findByPage(hql,pager,params);
    }
}
