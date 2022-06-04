package com.lgt.fxtradingleague.TradingModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("tbl_contest_id")
    @Expose
    private String tblContestId;
    @SerializedName("contest_name")
    @Expose
    private String contestName;
    @SerializedName("contest_tag")
    @Expose
    private String contestTag;
    @SerializedName("winner")
    @Expose
    private String winner;
    @SerializedName("prize_pool")
    @Expose
    private String prizePool;
    @SerializedName("total_team")
    @Expose
    private String totalTeam;
    @SerializedName("join_team")
    @Expose
    private String joinTeam;
    @SerializedName("entry_fee")
    @Expose
    private String entryFee;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("contest_time")
    @Expose
    private String contestTime;
    @SerializedName("contest_date")
    @Expose
    private String contestDate;
    @SerializedName("winning_information")
    @Expose
    private List<WinningInformation> winningInformation = null;

    public String getTblContestId() {
        return tblContestId;
    }

    public void setTblContestId(String tblContestId) {
        this.tblContestId = tblContestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getContestTag() {
        return contestTag;
    }

    public void setContestTag(String contestTag) {
        this.contestTag = contestTag;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(String prizePool) {
        this.prizePool = prizePool;
    }

    public String getTotalTeam() {
        return totalTeam;
    }

    public void setTotalTeam(String totalTeam) {
        this.totalTeam = totalTeam;
    }

    public String getJoinTeam() {
        return joinTeam;
    }

    public void setJoinTeam(String joinTeam) {
        this.joinTeam = joinTeam;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getContestTime() {
        return contestTime;
    }

    public void setContestTime(String contestTime) {
        this.contestTime = contestTime;
    }

    public String getContestDate() {
        return contestDate;
    }

    public void setContestDate(String contestDate) {
        this.contestDate = contestDate;
    }

    public List<WinningInformation> getWinningInformation() {
        return winningInformation;
    }

    public void setWinningInformation(List<WinningInformation> winningInformation) {
        this.winningInformation = winningInformation;
    }

    public class JoinTeamData {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

    }

    public static class WinningInformation {

        @SerializedName("tbl_winning_info_id")
        @Expose
        private String tblWinningInfoId;
        @SerializedName("from_rank")
        @Expose
        private String fromRank;
        @SerializedName("to_rank")
        @Expose
        private String toRank;
        @SerializedName("price")
        @Expose
        private String price;

        public String getTblWinningInfoId() {
            return tblWinningInfoId;
        }

        public void setTblWinningInfoId(String tblWinningInfoId) {
            this.tblWinningInfoId = tblWinningInfoId;
        }

        public String getFromRank() {
            return fromRank;
        }

        public void setFromRank(String fromRank) {
            this.fromRank = fromRank;
        }

        public String getToRank() {
            return toRank;
        }

        public void setToRank(String toRank) {
            this.toRank = toRank;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }
}
