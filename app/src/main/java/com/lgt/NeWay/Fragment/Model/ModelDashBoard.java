package com.lgt.NeWay.Fragment.Model;

public class ModelDashBoard {
    String tv_countoflistitem,list,iv_dash_listimage,tv_more,iv_dashmore,id;

    public ModelDashBoard(String tv_countoflistitem, String list, String iv_dash_listimage, String id) {
        this.tv_countoflistitem = tv_countoflistitem;
        this.list = list;
        this.iv_dash_listimage = iv_dash_listimage;
        this.id = id;
    }

    public String getTv_countoflistitem() {
        return tv_countoflistitem;
    }

    public void setTv_countoflistitem(String tv_countoflistitem) {
        this.tv_countoflistitem = tv_countoflistitem;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getIv_dash_listimage() {
        return iv_dash_listimage;
    }

    public void setIv_dash_listimage(String iv_dash_listimage) {
        this.iv_dash_listimage = iv_dash_listimage;
    }

    public String getTv_more() {
        return tv_more;
    }

    public void setTv_more(String tv_more) {
        this.tv_more = tv_more;
    }

    public String getIv_dashmore() {
        return iv_dashmore;
    }

    public void setIv_dashmore(String iv_dashmore) {
        this.iv_dashmore = iv_dashmore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
