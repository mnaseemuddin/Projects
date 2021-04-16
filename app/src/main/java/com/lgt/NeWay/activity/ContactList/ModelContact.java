package com.lgt.NeWay.activity.ContactList;

public class ModelContact {
    String tv_Contact_Name,tv_Contact_Number,sp_Statuspending,iv_delete,tbl_coaching_user_contact_number_id;

    public ModelContact(String tv_Contact_Name, String tv_Contact_Number, String tbl_coaching_user_contact_number_id) {
        this.tv_Contact_Name = tv_Contact_Name;
        this.tv_Contact_Number = tv_Contact_Number;
        this.tbl_coaching_user_contact_number_id = tbl_coaching_user_contact_number_id;
    }

    public String getTv_Contact_Name() {
        return tv_Contact_Name;
    }

    public void setTv_Contact_Name(String tv_Contact_Name) {
        this.tv_Contact_Name = tv_Contact_Name;
    }

    public String getTv_Contact_Number() {
        return tv_Contact_Number;
    }

    public void setTv_Contact_Number(String tv_Contact_Number) {
        this.tv_Contact_Number = tv_Contact_Number;
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

    public String getTbl_coaching_user_contact_number_id() {
        return tbl_coaching_user_contact_number_id;
    }

    public void setTbl_coaching_user_contact_number_id(String tbl_coaching_user_contact_number_id) {
        this.tbl_coaching_user_contact_number_id = tbl_coaching_user_contact_number_id;
    }
}
