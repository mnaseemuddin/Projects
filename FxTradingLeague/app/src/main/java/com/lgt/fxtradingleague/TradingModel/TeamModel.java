package com.lgt.fxtradingleague.TradingModel;

public class TeamModel {
    String tbl_join_contest_id,user_id,type,team_id,contest_id,rank,name,overall_profit;

    public String getOverall_profit() {
        return overall_profit;
    }

    public void setOverall_profit(String overall_profit) {
        this.overall_profit = overall_profit;
    }

    public String getTbl_join_contest_id() {
        return tbl_join_contest_id;
    }

    public void setTbl_join_contest_id(String tbl_join_contest_id) {
        this.tbl_join_contest_id = tbl_join_contest_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
