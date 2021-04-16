package com.lgt.NeWay.activity.Gallary;

public class ModelGallary {
    String gallaryimage,iv_Deleteimage,tbl_gallery_id;

    public ModelGallary(String gallaryimage, String tbl_gallery_id) {
        this.gallaryimage = gallaryimage;
        this.tbl_gallery_id = tbl_gallery_id;
    }

    public String getGallaryimage() {
        return gallaryimage;
    }

    public void setGallaryimage(String gallaryimage) {
        this.gallaryimage = gallaryimage;
    }

    public String getIv_Deleteimage() {
        return iv_Deleteimage;
    }

    public void setIv_Deleteimage(String iv_Deleteimage) {
        this.iv_Deleteimage = iv_Deleteimage;
    }

    public String getTbl_gallery_id() {
        return tbl_gallery_id;
    }

    public void setTbl_gallery_id(String tbl_gallery_id) {
        this.tbl_gallery_id = tbl_gallery_id;
    }
}
