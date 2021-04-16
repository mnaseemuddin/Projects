package com.lgt.NeWay.activity.UploadOtherCertificate;

public class MOdelUploadCertificate {
    String iv_certificate,tv_TeacherName,tv_Title_Name,tv_tvContactNo,sp_Statuspending,iv_delete,tbl_upload_other_certificate_id,Certificate_status;

    public MOdelUploadCertificate(String iv_certificate, String tv_TeacherName, String tv_Title_Name, String tv_tvContactNo, String tbl_upload_other_certificate_id, String certificate_status) {
        this.iv_certificate = iv_certificate;
        this.tv_TeacherName = tv_TeacherName;
        this.tv_Title_Name = tv_Title_Name;
        this.tv_tvContactNo = tv_tvContactNo;
        this.tbl_upload_other_certificate_id = tbl_upload_other_certificate_id;
        Certificate_status = certificate_status;
    }

    public String getIv_certificate() {
        return iv_certificate;
    }

    public void setIv_certificate(String iv_certificate) {
        this.iv_certificate = iv_certificate;
    }

    public String getTv_TeacherName() {
        return tv_TeacherName;
    }

    public void setTv_TeacherName(String tv_TeacherName) {
        this.tv_TeacherName = tv_TeacherName;
    }

    public String getTv_Title_Name() {
        return tv_Title_Name;
    }

    public void setTv_Title_Name(String tv_Title_Name) {
        this.tv_Title_Name = tv_Title_Name;
    }

    public String getTv_tvContactNo() {
        return tv_tvContactNo;
    }

    public void setTv_tvContactNo(String tv_tvContactNo) {
        this.tv_tvContactNo = tv_tvContactNo;
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

    public String getTbl_upload_other_certificate_id() {
        return tbl_upload_other_certificate_id;
    }

    public void setTbl_upload_other_certificate_id(String tbl_upload_other_certificate_id) {
        this.tbl_upload_other_certificate_id = tbl_upload_other_certificate_id;
    }

    public String getCertificate_status() {
        return Certificate_status;
    }

    public void setCertificate_status(String certificate_status) {
        Certificate_status = certificate_status;
    }
}
