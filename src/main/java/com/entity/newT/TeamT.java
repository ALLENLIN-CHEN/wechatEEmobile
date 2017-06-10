package com.entity.newT;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zengqin on 2017/3/25.
 */
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class TeamT {
    private int teamId;
    private String TeamName;

    public TeamT(int teamId, String teamName) {
        this.teamId = teamId;
        TeamName = teamName;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }
}
