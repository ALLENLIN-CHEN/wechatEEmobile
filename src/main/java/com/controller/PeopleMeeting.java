package com.controller;

import com.entity.Pager;
import com.entity.Project;
import com.entity.ScheduleMember;
import com.entity.newT.ScheduleT;
import com.service.MyService;
import com.service.ScheduleMemberService;
import com.service.ScheduleService;
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
 * Created by congzihan on 17/10/6.
 */
@Controller
@RequestMapping(value = "peopleMeeting")
public class PeopleMeeting {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    MyService myService;
    @Autowired
    ScheduleMemberService scheduleMemberService;

    /**
     * 查看任务情况
     */
    @RequestMapping(value = "scheduleStatus", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> scheduleStatus(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        int currentPageNumber = request.getParameter("currentPageNumber") != null ? Integer.parseInt(request.getParameter("currentPageNumber")) : 1;
        int pageSize = request.getParameter("pageSize") != null ? Integer.parseInt(request.getParameter("pageSize")) : 5;
        Pager pagerModel = new Pager(currentPageNumber, pageSize);
        Map<String, Object> dataMap = new HashedMap();
        pagerModel = myService.findDoneSchedules(openId, pagerModel);
        List<ScheduleT> undoScheduleTs = pagerModel.getDataList();
        for (ScheduleT scheduleT : undoScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            List list = scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId));
            scheduleT.setScheduleMembers(new HashSet<ScheduleMember>(list));
        }
        int undoTotalSize = pagerModel.getTotalSize();

        Pager doingPagerModel = new Pager(currentPageNumber, pageSize);
        pagerModel = myService.findDoingSchedules(openId, pagerModel);
        List<ScheduleT> doingScheduleTs = doingPagerModel.getDataList();
        for (ScheduleT scheduleT : doingScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            scheduleT.setScheduleMembers((Set<ScheduleMember>) scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId)));
        }
        int doingTotalSize = pagerModel.getTotalSize();

        Pager beyondPagerModel = new Pager(currentPageNumber, pageSize);
        pagerModel = myService.findBeyondSchedules(openId, pagerModel);
        List<ScheduleT> beyondScheduleTs = beyondPagerModel.getDataList();
        for (ScheduleT scheduleT : beyondScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            scheduleT.setScheduleMembers((Set<ScheduleMember>) scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId)));
        }
        int beyondTotalSize = pagerModel.getTotalSize();

        Pager transferPagerModel = new Pager(currentPageNumber, pageSize);
        pagerModel = myService.findMyTransferSchedule(openId, pagerModel);
        List<ScheduleT> transferScheduleTs = transferPagerModel.getDataList();
        for (ScheduleT scheduleT : transferScheduleTs) {
            int scheduleId = scheduleT.getScheduleId();
            scheduleT.setScheduleMembers((Set<ScheduleMember>) scheduleMemberService.findScheduleMembers(String.valueOf(scheduleId)));
        }
        int transferTotalSize = pagerModel.getTotalSize();


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
