package com.lgt.NeWay.activity.UserBatchRequest;

public class ModelUserBatchRequest {
    String tv_Uname,tv_batch_Name,tv_MobileNo,sp_Statuspending,iv_delete;

    public ModelUserBatchRequest(String tv_Uname, String tv_batch_Name, String tv_MobileNo) {
        this.tv_Uname = tv_Uname;
        this.tv_batch_Name = tv_batch_Name;
        this.tv_MobileNo = tv_MobileNo;
    }

    public String getTv_Uname() {
        return tv_Uname;
    }

    public void setTv_Uname(String tv_Uname) {
        this.tv_Uname = tv_Uname;
    }

    public String getTv_batch_Name() {
        return tv_batch_Name;
    }

    public void setTv_batch_Name(String tv_batch_Name) {
        this.tv_batch_Name = tv_batch_Name;
    }

    public String getTv_MobileNo() {
        return tv_MobileNo;
    }

    public void setTv_MobileNo(String tv_MobileNo) {
        this.tv_MobileNo = tv_MobileNo;
    }

    public String getSp_Statuspending() {
        return sp_Statuspending;
    }

    public void setSp_Statuspending(String sp_Statuspending) {
        this.sp_Statuspending = sp_Statuspending;
    }

    public String getIv_delete() {
        return iv_delete;
    }

    public void setIv_delete(String iv_delete) {
        this.iv_delete = iv_delete;
    }
}
