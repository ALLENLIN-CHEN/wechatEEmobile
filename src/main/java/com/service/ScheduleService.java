package com.service;

import com.dao.impl.ScheduleDao;
import com.dao.impl.ScheduleMemberDao;
import com.entity.Pager;
import com.entity.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

}
