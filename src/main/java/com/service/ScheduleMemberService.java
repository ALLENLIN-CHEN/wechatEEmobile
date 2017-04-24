package com.service;

import com.dao.impl.ScheduleDao;
import com.dao.impl.ScheduleMemberDao;
import com.dao.impl.SubprojectDao;
import com.dao.impl.TeamUserDao;
import com.entity.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rthtr on 2017/4/3.
 */
@Service
public class ScheduleMemberService {
    @Autowired
    ScheduleMemberDao scheduleMemberDao;
    @Autowired
    TeamUserDao teamUserDao;
    @Autowired
    ScheduleDao scheduleDao;
    @Autowired
    SubprojectDao subprojectDao;

    public ArrayList findTaskIntensityForPerson(Pager pager, Integer teamId, String memberName, String startTime, String endTime){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager teamUsers=teamUserDao.findTeamUsersByTeamId(pager,teamId,memberName);
        for(int i=0;i<teamUsers.getDataList().size();i++){
            Object []row = (Object[]) teamUsers.getDataList().get(i);
            List dataForOverTime=scheduleMemberDao.findTaskOverTimeForPerson(teamId,row[1].toString(),startTime,endTime);
            Long dataForOverTimeAmount = dataForOverTime.isEmpty() ? 0 : (Long) dataForOverTime.get(0);
            List dataUnfinished=scheduleMemberDao.findUnfinishedForPerson(teamId, row[1].toString(),startTime,endTime);
            Long dataUnfinishedAmount = dataUnfinished.isEmpty() ? 0 : (Long) dataUnfinished.get(0);

            Map<String,Object> mapForOnePerson=new HashMap<String, Object>();
            mapForOnePerson.put("taskUnfinished",dataUnfinishedAmount);
            mapForOnePerson.put("taskOverTime",dataForOverTimeAmount);
            mapForOnePerson.put("memberOpenId",row[0].toString());
            mapForOnePerson.put("memberName",row[1].toString());
            arrayList.add(mapForOnePerson);
        }
        return arrayList;
    }

    public ArrayList findTaskIntensityStatisticsForPerson(Pager pager, Integer teamId, String memberOpenId, String startTime, String endTime){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager scheduleList=scheduleMemberDao.findTaskIntensityStatisticsForPerson(pager,teamId,memberOpenId,startTime,endTime);
        for(int i=0;i<scheduleList.getDataList().size();i++){
            Object []row=(Object [])scheduleList.getDataList().get(i);
            Map<String,Object> scheduleMap=new HashMap<String, Object>();
            scheduleMap.put("scheduleId",row[0]);
            scheduleMap.put("projectName",row[1]);
            scheduleMap.put("projectId",row[9]);
            scheduleMap.put("subprojectName",row[2]);
            scheduleMap.put("subprojectId",row[8]);
            scheduleMap.put("scheduleName",row[3]);
            scheduleMap.put("tag",row[4]);
            scheduleMap.put("message",row[5]);
            scheduleMap.put("endDate",row[6]);
            scheduleMap.put("scheduleStatus",row[7]);
            arrayList.add(scheduleMap);
        }
        return arrayList;
    }
    @Transactional
    public List findScheduleMembers(String scheduleId) {
        String hql = "select sm.user from ScheduleMember sm where sm.schedule.scheduleId=" + scheduleId;
        List list = scheduleMemberDao.findByHql(hql, null, null);
        return list;
    }

    public Map findTaskIntensityAnalyzeForPerson(Integer teamId, String memberOpenId, String period){
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
                List unfinishedForPerson=scheduleMemberDao.findUnfinishedForPersonByOpenId(teamId,memberOpenId,startTime,endTime);
                unfinished.add(unfinishedForPerson.isEmpty() ? 0 : (Long) unfinishedForPerson.get(0));
                List finishedForPerson=scheduleMemberDao.findFinishedForPersonByOpenId(teamId,memberOpenId,startTime,endTime);
                finished.add(finishedForPerson.isEmpty() ? 0 : (Long) finishedForPerson.get(0));
                List overTimeForPerson=scheduleMemberDao.findTaskOverTimeForPersonByOpenId(teamId,memberOpenId,startTime,endTime);
                overTime.add(overTimeForPerson.isEmpty() ? 0 : (Long) overTimeForPerson.get(0));
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
                List unfinishedForPerson=scheduleMemberDao.getCountUnfinishedForPerson(teamId,memberOpenId,dateTime);
                unfinished.add(unfinishedForPerson.isEmpty() ? 0 : (Long) unfinishedForPerson.get(0));
                List finishedForPerson=scheduleMemberDao.getCountFinishedForPerson(teamId,memberOpenId,dateTime);
                finished.add(finishedForPerson.isEmpty() ? 0 : (Long) finishedForPerson.get(0));
                List overTimeForPerson=scheduleMemberDao.getCountTaskOverTimeForPerson(teamId,memberOpenId,dateTime);
                overTime.add(overTimeForPerson.isEmpty() ? 0 : (Long) overTimeForPerson.get(0));
            }
        }
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("unfinished",unfinished);
        map.put("finished",finished);
        map.put("overTime",overTime);
        return map;
    }

}
