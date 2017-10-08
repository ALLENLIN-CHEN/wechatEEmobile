package com.controller;

import com.entity.Project;
import com.entity.Schedule;
import com.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by congzihan on 17/8/20.
 */
@Controller
@RequestMapping(value = "schedulaPC")
public class SchedulePCController {
    @Autowired
    ScheduleService scheduleService;
    /**
     * 创建任务
     */
    @RequestMapping(value = "createSchedule",method = RequestMethod.POST)
    @ResponseBody
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        scheduleService.saveSechedule(schedule);
        return schedule;
    }

}
