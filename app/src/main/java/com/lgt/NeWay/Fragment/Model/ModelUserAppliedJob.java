package com.lgt.NeWay.Fragment.Model;

public class ModelUserAppliedJob {
    String tvBatchname,sp_Status,bt_Edit,BatchStatus,iv_delete;

    public ModelUserAppliedJob(String tvBatchname, String batchstatus) {
        this.tvBatchname = tvBatchname;
        BatchStatus = batchstatus;
    }

    public String getTvBatchname() {
        return tvBatchname;
    }

    public void setTvBatchname(String tvBatchname) {
        this.tvBatchname = tvBatchname;
    }

    public String getSp_Status() {
        return sp_Status;
    }

    public void setSp_Status(String sp_Status) {
        this.sp_Status = sp_Status;
    }

    public String getBt_Edit() {
        return bt_Edit;
    }

    public void setBt_Edit(String bt_Edit) {
        this.bt_Edit = bt_Edit;
    }

    public String getBatchstatus() {
        return BatchStatus;
    }

    public void setBatchstatus(String batchstatus) {
        BatchStatus = batchstatus;
    }

    public String getIv_delete() {
        return iv_delete;
    }

    public void setIv_delete(String iv_delete) {
        this.iv_delete = iv_delete;
    }
}
