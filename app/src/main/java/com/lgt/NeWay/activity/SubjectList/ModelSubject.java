package com.lgt.NeWay.activity.SubjectList;

public class ModelSubject {
    String tv_Subject_Name,sp_Statuspending,status,iv_Edit,iv_delete,tbl_board_id;

    public ModelSubject(String tv_Subject_Name, String sp_Statuspending, String status, String iv_Edit, String iv_delete, String tbl_board_id) {
        this.tv_Subject_Name = tv_Subject_Name;
        this.sp_Statuspending = sp_Statuspending;
        this.status = status;
        this.iv_Edit = iv_Edit;
        this.iv_delete = iv_delete;
        this.tbl_board_id = tbl_board_id;
    }

    public String getTv_Subject_Name() {
        return tv_Subject_Name;
    }

    public void setTv_Subject_Name(String tv_Subject_Name) {
        this.tv_Subject_Name = tv_Subject_Name;
    }

    public String getSp_Statuspending() {
        return sp_Statuspending;
    }

    public void setSp_Statuspending(String sp_Statuspending) {
        this.sp_Statuspending = sp_Statuspending;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIv_Edit() {
        return iv_Edit;
    }

    public void setIv_Edit(String iv_Edit) {
        this.iv_Edit = iv_Edit;
    }

    public String getIv_delete() {
        return iv_delete;
    }

    public void setIv_delete(String iv_delete) {
        this.iv_delete = iv_delete;
    }

    public String getTbl_board_id() {
        return tbl_board_id;
    }

    public void setTbl_board_id(String tbl_board_id) {
        this.tbl_board_id = tbl_board_id;
    }
}
