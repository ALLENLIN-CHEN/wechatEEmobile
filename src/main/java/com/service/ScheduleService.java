package com.service;

import com.dao.impl.ScheduleDao;
import com.dao.impl.ScheduleMemberDao;
import com.entity.Pager;
import com.entity.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zengqin on 2017/4/6.
 */
@Service
public class ScheduleService {
    @Autowired
    ScheduleDao scheduleDao;
    @Autowired
    ScheduleMemberDao scheduleMemberDao;


    public Schedule findById(int scheduleId){
        return scheduleDao.findById(scheduleId);
    }

    public ArrayList findTaskIntensityStatisticsForPerson(Pager pager, Integer teamId, String memberOpenId, String startTime, String endTime){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager scheduleList=scheduleMemberDao.findTaskIntensityStatisticsForPerson(pager, teamId,memberOpenId,startTime,endTime);
        for(int i=0;i<scheduleList.getDataList().size();i++){
            Object []row=(Object [])scheduleList.getDataList().get(i);
            Map<String,Object> scheduleMap=new HashMap<String, Object>();
            scheduleMap.put("scheduleId",row[0]);
            scheduleMap.put("projectName",row[1]);
            scheduleMap.put("projectId",row[8]);
            scheduleMap.put("subprojectName",row[2]);
            scheduleMap.put("subprojectId",row[9]);
            scheduleMap.put("scheduleName",row[3]);
            scheduleMap.put("tag",row[4]);
            scheduleMap.put("message",row[5]);
            scheduleMap.put("endDate",row[6]);
            scheduleMap.put("scheduleStatus",row[7]);
            arrayList.add(scheduleMap);
        }
        return arrayList;
    }

    public ArrayList findTaskIntensityStatisticsForSubproject(Pager pager,Integer subprojectId, String scheduleType, String startTime, String endTime){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager scheduleList=scheduleDao.findTaskIntensityStatisticsForSubproject(pager,subprojectId,scheduleType,startTime,endTime);
        for(int i=0;i<scheduleList.getDataList().size();i++){
            Object []row=(Object [])scheduleList.getDataList().get(i);
            Map<String,Object> scheduleMap=new HashMap<String, Object>();
            scheduleMap.put("scheduleId",row[0]);
            scheduleMap.put("projectName",row[1]);
            scheduleMap.put("projectId",row[9]);
            scheduleMap.put("subprojectName",row[2]);
            scheduleMap.put("subprojectId",row[8]);
            scheduleMap.put("scheduleName",row[3]);
            scheduleMap.put("scheduleType",row[4]);
            scheduleMap.put("message",row[5]);
            scheduleMap.put("endDate",row[6]);
            scheduleMap.put("scheduleStatus",row[7]);
            arrayList.add(scheduleMap);
        }
        return arrayList;
    }

/*    public Pager findConferenceStatisticsForSubproject(){

    }*/
    public ArrayList findStatisticsForSubproject(Pager pager,Integer teamId, String scheduleType, String startTime, String endTime){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager scheduleList=scheduleDao.findStatisticsForSubproject(pager,teamId,scheduleType,startTime,endTime);
        for(int i=0;i<scheduleList.getDataList().size();i++){
            Object []row=(Object [])scheduleList.getDataList().get(i);
            Map<String,Object> scheduleMap=new HashMap<String, Object>();
            scheduleMap.put("scheduleId",row[0]);
            scheduleMap.put("projectName",row[1]);
            scheduleMap.put("projectId",row[9]);
            scheduleMap.put("subprojectName",row[2]);
            scheduleMap.put("subprojectId",row[8]);
            scheduleMap.put("scheduleName",row[3]);
            scheduleMap.put("scheduleType",row[4]);
            scheduleMap.put("message",row[5]);
            scheduleMap.put("endDate",row[6]);
            scheduleMap.put("scheduleStatus",row[7]);
            arrayList.add(scheduleMap);
        }
        return arrayList;
    }

    public Map findTaskIntensityAnalyzeForSubproject(Integer teamId, Integer subprojectId, String period){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList unfinished=new ArrayList();
        ArrayList finished=new ArrayList();
        ArrayList overTime=new ArrayList();

        String startTime="";
        String endTime="";

        if(period.equals("yearly")){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, -6);
            for(int i=0;i<6;i++){
                startTime=simpleDateFormat.format(c.getTime());
                c.add(Calendar.MONTH, 1);
                endTime=simpleDateFormat.format(c.getTime());
                List unfinishedForSubproject=scheduleDao.findUnfinishedForSubprojectByOpenId(teamId,subprojectId,startTime,endTime);
                unfinished.add(unfinishedForSubproject.isEmpty() ? 0 : (Long) unfinishedForSubproject.get(0));
                List finishedForSubproject=scheduleDao.findFinishedForSubprojectByOpenId(teamId,subprojectId,startTime,endTime);
                finished.add(finishedForSubproject.isEmpty() ? 0 : (Long) finishedForSubproject.get(0));
                List overTimeFoSubproject=scheduleDao.findTaskOverTimeForSubprojectByOpenId(teamId,subprojectId,startTime,endTime);
                overTime.add(overTimeFoSubproject.isEmpty() ? 0 : (Long) overTimeFoSubproject.get(0));
            }
        }else{
            String dateTime ="";
            int days=0;
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            Date startDate=null;
            Date endDate=new Date();
            Date date=null;
            if(period.equals("weekly")){
                c.add(Calendar.DATE, -13);//默认时间：近两周
                startDate=c.getTime();
                days=(int)(endDate.getTime()-startDate.getTime())/(24*60*60*1000);
            }else if(period.equals("monthly")){
                c.add(Calendar.MONTH, -1);//默认时间：近一个月
                days=c.getActualMaximum(Calendar.DATE);
            }

            for(int x=0;x<=days;x++){
                date=c.getTime();
                dateTime=simpleDateFormat.format(date);
                c.add(Calendar.DATE,1);
                List unfinishedForSubproject=scheduleDao.getCountUnfinishedForSubproject(teamId,subprojectId,dateTime);
                unfinished.add(unfinishedForSubproject.isEmpty() ? 0 : (Long) unfinishedForSubproject.get(0));
                List finishedForSubproject=scheduleDao.getCountFinishedForSubproject(teamId,subprojectId,dateTime);
                finished.add(finishedForSubproject.isEmpty() ? 0 : (Long) finishedForSubproject.get(0));
                List overTimeForSubproject=scheduleDao.getCountTaskOverTimeForSubproject(teamId,subprojectId,dateTime);
                overTime.add(overTimeForSubproject.isEmpty() ? 0 : (Long) overTimeForSubproject.get(0));
            }
        }
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("unfinished",unfinished);
        map.put("finished",finished);
        map.put("overTime",overTime);
        return map;
    }

}
