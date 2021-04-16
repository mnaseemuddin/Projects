package com.lgt.NeWay.activity.Amity;

public class ModelAmitys {
    String tv_Aminity_Name,iv_delete,sp_Statuspending,Amitystatus,tbl_amenity_id;

    public ModelAmitys(String tv_Aminity_Name, String amitystatus, String tbl_amenity_id) {
        this.tv_Aminity_Name = tv_Aminity_Name;
        Amitystatus = amitystatus;
        this.tbl_amenity_id = tbl_amenity_id;
    }

    public String getTv_Aminity_Name() {
        return tv_Aminity_Name;
    }

    public void setTv_Aminity_Name(String tv_Aminity_Name) {
        this.tv_Aminity_Name = tv_Aminity_Name;
    }

    public String getIv_delete() {
        return iv_delete;
    }

    public void setIv_delete(String iv_delete) {
        this.iv_delete = iv_delete;
    }

    public String getSp_Statuspending() {
        return sp_Statuspending;
    }

    public void setSp_Statuspending(String sp_Statuspending) {
        this.sp_Statuspending = sp_Statuspending;
    }

    public String getAmitystatus() {
        return Amitystatus;
    }

    public void setAmitystatus(String amitystatus) {
        Amitystatus = amitystatus;
    }

    public String getTbl_amenity_id() {
        return tbl_amenity_id;
    }

    public void setTbl_amenity_id(String tbl_amenity_id) {
        this.tbl_amenity_id = tbl_amenity_id;
    }
}
