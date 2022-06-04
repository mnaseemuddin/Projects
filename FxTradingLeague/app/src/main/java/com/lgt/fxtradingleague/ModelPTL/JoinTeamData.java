package com.lgt.fxtradingleague.ModelPTL;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinTeamData {
    @SerializedName("tbl_my_team_player_id")
    @Expose
    private String tblMyTeamPlayerId;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("share_qnt")
    @Expose
    private String shareQnt;
    @SerializedName("currency_rate")
    @Expose
    private String currencyRate;
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("contest_id")
    @Expose
    private String contestId;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;
    @SerializedName("isSelected2")
    @Expose
    private boolean isSelected2;
    @SerializedName("currency_value")
    @Expose
    private String currency_value;

    @SerializedName("currency_image")
    @Expose
    private String currency_image;

    public String getCurrency_value() {
        return currency_value;
    }

    public String getCurrency_image() {
        return currency_image;
    }

    public void setCurrency_image(String currency_image) {
        this.currency_image = currency_image;
    }

    public String isCurrency_value() {
        return currency_value;
    }

    public void setCurrency_value(String currency_value) {
        this.currency_value = currency_value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected2() {
        return isSelected2;
    }

    public void setSelected2(boolean selected2) {
        isSelected2 = selected2;
    }

    public String getTblMyTeamPlayerId() {
        return tblMyTeamPlayerId;
    }

    public void setTblMyTeamPlayerId(String tblMyTeamPlayerId) {
        this.tblMyTeamPlayerId = tblMyTeamPlayerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getShareQnt() {
        return shareQnt;
    }

    public void setShareQnt(String shareQnt) {
        this.shareQnt = shareQnt;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }
}
