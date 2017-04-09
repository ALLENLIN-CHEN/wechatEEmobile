package com.entity.newT;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengqin on 2017/3/23.
 */
public class ProjectT {
    private Integer projectId;
    private  Integer  subprjectId;
    private String subproject;
    private String project;
    private String teamStatus;
    private  int teamId;
    private List<UserT> userTs=new ArrayList<>(0);

    public ProjectT() {
    }

    public ProjectT(Integer projectId, Integer  subprjectId, String subproject, String project, String teamStatus) {
        this.projectId = projectId;
        if(subproject==null){
            this.subproject =null;
        }
        this.subprjectId = subprjectId;
        this.subproject = subproject;
        if(project==null){
            this.project =null;
        }
        this.project = project;

        this.teamStatus = teamStatus;
    }

    public List<UserT> getUserTs() {
        return userTs;
    }

    public void setUserTs(List<UserT> userTs) {
        this.userTs = userTs;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getSubprjectId() {
        return subprjectId;
    }

    public void setSubprjectId(Integer subprjectId) {
        this.subprjectId = subprjectId;
    }

    public String getSubproject() {
        return subproject;
    }

    public void setSubproject(String subproject) {
        this.subproject = subproject;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String  getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

}