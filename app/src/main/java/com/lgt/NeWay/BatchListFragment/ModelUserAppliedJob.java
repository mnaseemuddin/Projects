package com.lgt.NeWay.BatchListFragment;

public class ModelUserAppliedJob {
    String tvBatchname,sp_Status,bt_Edit,BatchStatus,iv_delete,tbl_batches_id;

    public ModelUserAppliedJob(String tvBatchname, String sp_Status, String tbl_batches_id) {
        this.tvBatchname = tvBatchname;
        this.sp_Status = sp_Status;
        this.tbl_batches_id = tbl_batches_id;
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

    public String getBatchStatus() {
        return BatchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        BatchStatus = batchStatus;
    }

    public String getIv_delete() {
        return iv_delete;
    }

    public void setIv_delete(String iv_delete) {
        this.iv_delete = iv_delete;
    }

    public String getTbl_batches_id() {
        return tbl_batches_id;
    }

    public void setTbl_batches_id(String tbl_batches_id) {
        this.tbl_batches_id = tbl_batches_id;
    }
}
