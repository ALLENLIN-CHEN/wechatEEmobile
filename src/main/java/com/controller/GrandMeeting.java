package com.controller;

import com.entity.Pager;
import com.entity.ScheduleMember;
import com.entity.newT.ScheduleT;
import com.service.MyService;
import com.service.ScheduleMemberService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by congzihan on 17/10/15.
 */
@Controller
@RequestMapping(value = "grandMeeting")
public class GrandMeeting {
    @Autowired
    MyService myService;
    @Autowired
    ScheduleMemberService scheduleMemberService;
    @RequestMapping(value = "scheduleStatus", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> scheduleStatus(HttpServletRequest request) {
        String taskType = request.getParameter("taskType");
        int currentPageNumber = request.getParameter("currentPageNumber") != null ? Integer.parseInt(request.getParameter("currentPageNumber")) : 1;
        int pageSize = request.getParameter("pageSize") != null ? Integer.parseInt(request.getParameter("pageSize")) : 5;
        Pager pagerModel = new Pager(currentPageNumber, pageSize);
        Map<String, Object> dataMap = new HashedMap();
        pagerModel = myService.findDoneSchedules( pagerModel,taskType.charAt(0));
        List<ScheduleT> undoScheduleTs = pagerModel.getDataList();
        for (ScheduleT scheduleT : undoScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            List list = scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId));
            scheduleT.setScheduleMembers(new HashSet<ScheduleMember>(list));
        }
        int undoTotalSize = undoScheduleTs.size();

        Pager doingPagerModel = new Pager(currentPageNumber, pageSize);
        pagerModel = myService.findDoingSchedules( pagerModel,taskType.charAt(0));
        List<ScheduleT> doingScheduleTs = doingPagerModel.getDataList();
        for (ScheduleT scheduleT : doingScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            scheduleT.setScheduleMembers((Set<ScheduleMember>) scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId)));
        }
        int doingTotalSize = doingScheduleTs.size();

        Pager beyondPagerModel = new Pager(currentPageNumber, pageSize);
        pagerModel = myService.findBeyondSchedules( pagerModel,taskType.charAt(0));
        List<ScheduleT> beyondScheduleTs = beyondPagerModel.getDataList();
        for (ScheduleT scheduleT : beyondScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            scheduleT.setScheduleMembers((Set<ScheduleMember>) scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId)));
        }
        int beyondTotalSize = beyondScheduleTs.size();

        Pager transferPagerModel = new Pager(currentPageNumber, pageSize);
        pagerModel = myService.findMyTransferSchedule( pagerModel,taskType.charAt(0));
        List<ScheduleT> transferScheduleTs = transferPagerModel.getDataList();
        for (ScheduleT scheduleT : transferScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            scheduleT.setScheduleMembers((Set<ScheduleMember>) scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId)));
        }
        int transferTotalSize = transferScheduleTs.size();


        dataMap.put("result", "success");
        dataMap.put("resultTip", "");
        dataMap.put("done", undoScheduleTs);
        dataMap.put("doneSize", undoTotalSize);
        dataMap.put("doing", doingScheduleTs);
        dataMap.put("doingSize", doingTotalSize);
        dataMap.put("beyond", beyondScheduleTs);
        dataMap.put("beyondSize", beyondTotalSize);
        dataMap.put("transfer", transferScheduleTs);
        dataMap.put("transferSize", transferTotalSize);

        return dataMap;
    }
}
