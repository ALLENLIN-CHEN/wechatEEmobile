package com.controller;

import com.entity.Pager;
import com.entity.newT.ScheduleT;
import com.service.MyService;
import com.service.ProjectService;
import com.util.JsonUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zengqin on 2017/4/8.
 */
@Controller
@RequestMapping(value = "my")
public class MyController {
    @Autowired
    MyService myService;
    @Autowired
    ProjectService projectService;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Map<String,Object> dataMap=new HashedMap();

    /**
     * 我的已转移
     * @param request
     * @return
     */
    @RequestMapping(value = "myTransfer")
    @ResponseBody
    public Map<String ,Object> myTransfer(HttpServletRequest request){
        dataMap.clear();
        try{
            String openId=request.getParameter("openId");
            int currentPageNumber = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber")):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize")):5;
            Pager pagerModel = new Pager(currentPageNumber, pageSize);
            pagerModel=myService.findMyTransferSchedule(openId,pagerModel);
            List<ScheduleT> schedules = pagerModel.getDataList();
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result","success");
            dataMap.put("resultTip","");
            dataMap.put("schedules",schedules);
            dataMap.put("totalSize",totalSize);
        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }
    /**
     * 我的未完成/超期
     */
    @RequestMapping(value = "undo")
    @ResponseBody
    public Map<String,Object> myUndo(HttpServletRequest request){
        dataMap.clear();
        try{
            String openId=request.getParameter("openId");
            int currentPageNumber = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber")):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize")):5;
            Pager pagerModel = new Pager(currentPageNumber, pageSize);
            pagerModel=myService.findUndoSchedules(openId,pagerModel);
            List<ScheduleT> scheduleTs=pagerModel.getDataList();
            int totalSize=pagerModel.getTotalSize();
            dataMap.put("result","success");
            dataMap.put("resultTip","");
            dataMap.put("schedules",scheduleTs);
            dataMap.put("totalSize",totalSize);
        }catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }
    /**
     * 我的日程
     */
    @RequestMapping("myCalendarTime")
    @ResponseBody
    public Map<String,Object> myCalendar(HttpServletRequest request) {
        dataMap.clear();
        try {
            String openId = request.getParameter("openId");
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM");
            String time=request.getParameter("time");
            Date date;
            if(time!=null) {
                 date = df.parse(time);
            }else{
                date=new Date();
                String dateString =df.format(date);
                date = df.parse(dateString);
            }
            List list=myService.findScheduleTime(openId);
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                Date d=df.parse(list.get(i).toString());
                if(d.equals(date)){
                    map.put("taskTime",list.get(i));
                    map.put("isTask","true");
                    dataList.add(map);
                }
            }
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH)+1;
            //int day=calendar.get(Calendar.DAY_OF_MONTH);
            int days=28;
            if(month==2) {
                if (JsonUtil.isLeapYear(year)) {
                    days = 29;
                }
            }else{
                    days = JsonUtil.maxDay(month);
                }
            List<Map<String, Object>> dataList1 = new ArrayList<>();
               Calendar calendar1=Calendar.getInstance();
            for(int i=1;i<days+1;i++){
                int flag=0;
                Map<String,Object> data=new HashMap<>();
                String s=year+"-"+month+"-"+i;
               for(Map<String,Object>map:dataList) {
                   Date date1 = (Date) map.get("taskTime");
                   calendar.setTime(date1);
                   int day = calendar.get(Calendar.DAY_OF_MONTH);
                   if(day==i){
                     data.put("taskTime",date1);
                       data.put("isTask","true");
                       dataList1.add(data);
                       flag=1;
                       break;
                   }
               }
               if(flag==0) {
                   data.put("taskTime", s);
                   data.put("isTask", "false");
                   dataList1.add(data);
               }
            }
            dataMap.put("taskTimes",dataList1);
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;
    }
    /**
     * 我的日程中的任务
     */
    @RequestMapping(value = "myCalendarSchedule")
    @ResponseBody
    public Map<String,Object> myCalendarSchedule(HttpServletRequest request){
        dataMap.clear();
        try{
            String openId=request.getParameter("openId");
            Date taskTime= simpleDateFormat.parse(request.getParameter("taskTime"));
            List<ScheduleT> scheduleTs=myService.findByTime(taskTime,openId);
            dataMap.put("result","success");
            dataMap.put("resultTip","");
            dataMap.put("schedules",scheduleTs);
        }catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }
    /**
     * 我的日程-创建任务选择项目
     */
    @RequestMapping(value = "selectProject")
    @ResponseBody
    public Map<String,Object> selectProject(HttpServletRequest request){
        dataMap.clear();
        try {
            String openId = request.getParameter("openId");
            List list = projectService.findProject(openId);
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("projectId", obj[0].toString());
                map.put("project", obj[1].toString());
                dataList.add(map);
            }
            dataMap.put("project",dataList);
        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }
    @RequestMapping(value = "selectSubproject")
    @ResponseBody
    public Map<String,Object> selectSubproject(HttpServletRequest request){
        dataMap.clear();
        try {
            int projectId =Integer.parseInt(request.getParameter("projectId"));
            List list = projectService.findSubproject(projectId);
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("subprojectId", obj[0].toString());
                map.put("subproject", obj[1].toString());
                dataList.add(map);
            }
            dataMap.put("subproject",dataList);
        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }
}
