package com.entity.newT;

public class TeamUserT2 {
    private int teamId;
    private String userName;
    private String teamName;
    private long count;

    public TeamUserT2(int teamId, String userName, String teamName) {
        this.teamId = teamId;
        this.userName = userName;
        this.teamName = teamName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public TeamUserT2() {
    }
}
