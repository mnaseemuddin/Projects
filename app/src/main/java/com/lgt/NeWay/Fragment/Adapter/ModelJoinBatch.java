package com.lgt.NeWay.Fragment.Adapter;

public class ModelJoinBatch {
    String tvStudentName,tvStatuspending,tv_Mobile,tv_Subject,tv_FeeType,tv_Feepaydate,bt_View;

    public ModelJoinBatch(String tvStudentName, String tvStatuspending, String tv_Mobile, String tv_Subject, String tv_FeeType, String tv_Feepaydate, String bt_View) {
        this.tvStudentName = tvStudentName;
        this.tvStatuspending = tvStatuspending;
        this.tv_Mobile = tv_Mobile;
        this.tv_Subject = tv_Subject;
        this.tv_FeeType = tv_FeeType;
        this.tv_Feepaydate = tv_Feepaydate;
        this.bt_View = bt_View;
    }

    public String getTvStudentName() {
        return tvStudentName;
    }

    public void setTvStudentName(String tvStudentName) {
        this.tvStudentName = tvStudentName;
    }

    public String getTvStatuspending() {
        return tvStatuspending;
    }

    public void setTvStatuspending(String tvStatuspending) {
        this.tvStatuspending = tvStatuspending;
    }

    public String getTv_Mobile() {
        return tv_Mobile;
    }

    public void setTv_Mobile(String tv_Mobile) {
        this.tv_Mobile = tv_Mobile;
    }

    public String getTv_Subject() {
        return tv_Subject;
    }

    public void setTv_Subject(String tv_Subject) {
        this.tv_Subject = tv_Subject;
    }

    public String getTv_FeeType() {
        return tv_FeeType;
    }

    public void setTv_FeeType(String tv_FeeType) {
        this.tv_FeeType = tv_FeeType;
    }

    public String getTv_Feepaydate() {
        return tv_Feepaydate;
    }

    public void setTv_Feepaydate(String tv_Feepaydate) {
        this.tv_Feepaydate = tv_Feepaydate;
    }

    public String getBt_View() {
        return bt_View;
    }

    public void setBt_View(String bt_View) {
        this.bt_View = bt_View;
    }
}
