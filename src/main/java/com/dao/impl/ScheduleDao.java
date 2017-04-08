package com.dao.impl;

import com.dao.BaseDao;
import com.entity.Pager;
import com.entity.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2017/3/26.
 */
@Repository("ScheduleDao")
public class ScheduleDao extends BaseDao<Schedule> {

    public boolean doUpdate(Schedule schedule){
        try {
            this.update(schedule);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public Schedule findById(int scheduleId) {
        return this.get(Schedule.class, scheduleId);
    }

    public List findTaskOverTimeForSubproject(Integer subprojectId, String startTime, String endTime){
        String hql="select count(*) from Schedule s where ((s.taskTime>='"+startTime
                +"' and s.taskTime<='"+endTime+"') or (s.taskStartTime>='"+startTime
                +"' and s.taskStartTime<='"+endTime+"')) and s.taskStatus='d'"
                +" and s.subproject.subprojectId="+subprojectId;
        return this.findByHql(hql,null,null);
    }

    public List findUnfinishedForSubproject(Integer subprojectId, String startTime, String endTime){
        String hql="select count(*) from Schedule s where ((s.taskTime>='"+startTime
                +"' and s.taskTime<='"+endTime+"') or (s.taskStartTime>='"+startTime
                +"' and s.taskStartTime<='"+endTime+"')) and (s.taskStatus='a'"
                +" or s.taskStatus='b' or s.taskStatus='f')"
                +" and s.subproject.subprojectId="+subprojectId;
        return this.findByHql(hql,null,null);
    }

    /**
     *@author rthtr 2017/4/7
     * 0：任务Id  1：项目名称  2：子项目名称  3：任务名称  4：标签  5：留言
     * 6：截止日期  7：任务状态  8：子项目id  9：项目id
     */
    public Pager findTaskIntensityStatisticsForSubproject(Pager pagerModel, Integer subprojectId, String scheduleType, String startTime, String endTime){
        String hql="";
        if(scheduleType==null||scheduleType.equals("")){
            hql="select s.scheduleId, s.subproject.project.project, s.subproject.subproject, s.taskContent,"
                    +"s.taskType, s.taskReply, s.taskTime, s.taskStatus, s.subproject.subprojectId,"
                    +" s.subproject.project.projectId from Schedule s where ((s.taskTime>='"+startTime
                    +"' and s.taskTime<='"+endTime+"') or (s.taskStartTime>='"+startTime
                    +"' and s.taskStartTime<='"+endTime+"')) and (s.taskStatus!='c' and s.taskStatus!='e')"
                    +" and s.subproject.subprojectId="+subprojectId;
        }else{
            hql="select s.scheduleId, s.subproject.project.project, s.subproject.subproject, s.taskContent,"
                    +"s.taskType, s.taskReply, s.taskTime, s.taskStatus, s.subproject.subprojectId,"
                    +" s.subproject.project.projectId from Schedule s where ((s.taskTime>='"+startTime
                    +"' and s.taskTime<='"+endTime+"') or (s.taskStartTime>='"+startTime
                    +"' and s.taskStartTime<='"+endTime+"')) and (s.taskStatus!='c' and s.taskStatus!='e')"
                    +" and s.subproject.subprojectId="+subprojectId+" and s.taskType='"+scheduleType+"'";
        }
        List dataList = this.listByPage(hql, pagerModel.getCurrentPageNumber(), pagerModel.getPageSize(), null, null);
        //      List dataList=findByHql(hql, null,null);
        int count = this.getAllTotal(hql, null, null);
        pagerModel.setTotalSize(count);
        pagerModel.setDataList(dataList);
        return pagerModel;
    }



}
