package com.entity.newT;

import com.entity.ScheduleMember;

import java.util.Date;
import java.util.Set;

/**
 * Created by  xionglian on 2017/3/25.
 */
public class ScheduleT {
    private String taskContent;
    private String taskReply;
    private Character taskType;
    private int scheduleId;
    private String project;
    private  String subproject;
    private Date taskTime;

    public Set<ScheduleMember> getScheduleMembers() {
        return scheduleMembers;
    }

    public void setScheduleMembers(Set<ScheduleMember> scheduleMembers) {
        this.scheduleMembers = scheduleMembers;
    }

    private Set<ScheduleMember> scheduleMembers;
    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Date taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    private Date taskStartTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    private String openId;
    public Character getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Character taskStatus) {
        this.taskStatus = taskStatus;
    }

    private Character taskStatus;

    private int subprojectId;

    public ScheduleT(String taskContent, String taskReply, Character taskType, int scheduleId, int projectId, String project,
                     int subprojectId, String subproject, Date taskTime, Character taskStatus, String openId) {
        this.taskContent = taskContent;
        this.taskReply = taskReply;
        this.taskType = taskType;
        this.scheduleId = scheduleId;
        this.subprojectId = subprojectId;
        this.project = project;
        this.subproject = subproject;
        this.taskTime = taskTime;
        this.taskStatus = taskStatus;
        this.projectId = projectId;
        this.openId = openId;
    }

    public ScheduleT(String taskContent, String taskReply, Character taskType, int scheduleId, int projectId, String project,
                     int subprojectId, String subproject, Date taskTime, Date taskStartTime ) {
        this.taskContent = taskContent;
        this.taskReply = taskReply;
        this.taskType = taskType;
        this.scheduleId = scheduleId;
        this.subprojectId = subprojectId;
        this.project = project;
        this.subproject = subproject;
        this.taskTime = taskTime;
        this.projectId = projectId;
        this.taskStartTime = taskStartTime;

    }

    private  int projectId;

    public ScheduleT() {
    }

    public ScheduleT(String taskContent, String taskReply, Character taskType, int scheduleId, String project, String subproject, Date taskTime) {
        this.taskContent = taskContent;
        this.taskReply = taskReply;
        this.taskType = taskType;
        this.scheduleId = scheduleId;
        this.project = project;
        this.subproject = subproject;
        this.taskTime = taskTime;
    }

    public ScheduleT(String taskContent, String taskReply, Character taskType, int scheduleId, int projectId, String project, int subprojectId, String subproject, Date taskTime) {
        this.taskContent = taskContent;
        if(taskReply==null){
            this.taskReply=null;
        }
        this.taskReply = taskReply;
        this.taskType = taskType;
        this.scheduleId = scheduleId;
        this.projectId = projectId;
        this.project = project;
        this.subprojectId = subprojectId;
        if(subproject==null){
            this.subproject=null;
        }
        this.subproject = subproject;
        this.taskTime = taskTime;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskReply() {
        return taskReply;
    }

    public void setTaskReply(String taskReply) {
        this.taskReply = taskReply;
    }

    public Character getTaskType() {
        return taskType;
    }

    public void setTaskType(Character taskType) {
        this.taskType = taskType;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getSubprojectId() {
        return subprojectId;
    }

    public void setSubprojectId(int subprojectId) {
        this.subprojectId = subprojectId;
    }

    public String getSubproject() {
        return subproject;
    }

    public void setSubproject(String subproject) {
        this.subproject = subproject;
    }

    public Date getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
    }
}
