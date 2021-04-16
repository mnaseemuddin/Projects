package com.lgt.NeWay.activity.TeacherList;

public class ModelTeacher {
    String tv_TeacherName,tv_Qualification_Name,tv_tvContactNo,tv_Email_id,sp_Statuspending,iv_delete,TeacherStatus,tbl_teacher_id,ivTeacher_Iamge;

    public ModelTeacher(String tv_TeacherName, String tv_Qualification_Name, String tv_tvContactNo, String tv_Email_id, String teacherStatus, String tbl_teacher_id, String ivTeacher_Iamge) {
        this.tv_TeacherName = tv_TeacherName;
        this.tv_Qualification_Name = tv_Qualification_Name;
        this.tv_tvContactNo = tv_tvContactNo;
        this.tv_Email_id = tv_Email_id;
        TeacherStatus = teacherStatus;
        this.tbl_teacher_id = tbl_teacher_id;
        this.ivTeacher_Iamge = ivTeacher_Iamge;
    }

    public String getTv_TeacherName() {
        return tv_TeacherName;
    }

    public void setTv_TeacherName(String tv_TeacherName) {
        this.tv_TeacherName = tv_TeacherName;
    }

    public String getTv_Qualification_Name() {
        return tv_Qualification_Name;
    }

    public void setTv_Qualification_Name(String tv_Qualification_Name) {
        this.tv_Qualification_Name = tv_Qualification_Name;
    }

    public String getTv_tvContactNo() {
        return tv_tvContactNo;
    }

    public void setTv_tvContactNo(String tv_tvContactNo) {
        this.tv_tvContactNo = tv_tvContactNo;
    }

    public String getTv_Email_id() {
        return tv_Email_id;
    }

    public void setTv_Email_id(String tv_Email_id) {
        this.tv_Email_id = tv_Email_id;
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

    public String getTeacherStatus() {
        return TeacherStatus;
    }

    public void setTeacherStatus(String teacherStatus) {
        TeacherStatus = teacherStatus;
    }

    public String getTbl_teacher_id() {
        return tbl_teacher_id;
    }

    public void setTbl_teacher_id(String tbl_teacher_id) {
        this.tbl_teacher_id = tbl_teacher_id;
    }

    public String getIvTeacher_Iamge() {
        return ivTeacher_Iamge;
    }

    public void setIvTeacher_Iamge(String ivTeacher_Iamge) {
        this.ivTeacher_Iamge = ivTeacher_Iamge;
    }
}
