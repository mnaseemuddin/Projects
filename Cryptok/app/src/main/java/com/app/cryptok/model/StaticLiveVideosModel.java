package com.app.cryptok.model;

public class StaticLiveVideosModel {

    private String iv_live_preview;
    private String tv_live_title;
    private Integer videopath;

    public Integer getVideopath() {
        return videopath;
    }

    public void setVideopath(Integer videopath) {
        this.videopath = videopath;
    }

    public StaticLiveVideosModel(String iv_live_preview, String tv_live_title,Integer videopath) {
        this.iv_live_preview = iv_live_preview;
        this.tv_live_title = tv_live_title;
        this.videopath = videopath;
    }

    public String getIv_live_preview() {
        return iv_live_preview;
    }

    public void setIv_live_preview(String iv_live_preview) {
        this.iv_live_preview = iv_live_preview;
    }

    public String getTv_live_title() {
        return tv_live_title;
    }

    public void setTv_live_title(String tv_live_title) {
        this.tv_live_title = tv_live_title;
    }
}
