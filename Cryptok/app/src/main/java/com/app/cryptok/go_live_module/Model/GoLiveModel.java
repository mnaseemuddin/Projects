package com.app.cryptok.go_live_module.Model;

import java.io.Serializable;

public class GoLiveModel implements Serializable {
    private String entry_diamonds;
    private String total_peoples_allow;
    private String stream_title;
    private String stream_type;

    public GoLiveModel() {

    }

    public GoLiveModel(String entry_diamonds, String total_peoples_allow, String stream_title, String stream_type) {
        this.entry_diamonds = entry_diamonds;
        this.total_peoples_allow = total_peoples_allow;
        this.stream_title = stream_title;
        this.stream_type = stream_type;
    }

    public String getEntry_diamonds() {
        return entry_diamonds;
    }

    public void setEntry_diamonds(String entry_diamonds) {
        this.entry_diamonds = entry_diamonds;
    }

    public String getTotal_peoples_allow() {
        return total_peoples_allow;
    }

    public void setTotal_peoples_allow(String total_peoples_allow) {
        this.total_peoples_allow = total_peoples_allow;
    }

    public String getStream_title() {
        return stream_title;
    }

    public void setStream_title(String stream_title) {
        this.stream_title = stream_title;
    }

    public String getStream_type() {
        return stream_type;
    }

    public void setStream_type(String stream_type) {
        this.stream_type = stream_type;
    }
}
