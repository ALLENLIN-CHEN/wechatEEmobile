package com.controller;

import com.entity.*;
import com.entity.newT.ScheduleMemberT;
import com.service.ProjectService;
import com.service.ScheduleService;
import com.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by congzihan on 17/8/20.
 */
@Controller
@RequestMapping(value = "schedulaPC")
public class SchedulePCController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ProjectService projectService;
    /**
     * 创建任务
     */
    @RequestMapping(value = "createSchedule",method = RequestMethod.POST)
    @ResponseBody
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        scheduleService.saveSechedule(schedule);
        return schedule;
    }

    /**
     * 更新任务人员
     */
    @RequestMapping(value = "updateScheduleMember")
    @ResponseBody
    public List updateProject(@RequestParam int scheduleId, @RequestParam List<String> openId) {
        scheduleService.updateMemeber(scheduleId,openId);
        return projectService.findScheduleMember(scheduleId+"");
    }

}
