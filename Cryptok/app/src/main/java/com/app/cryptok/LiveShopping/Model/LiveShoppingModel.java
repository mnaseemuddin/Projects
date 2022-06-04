package com.app.cryptok.LiveShopping.Model;

import java.io.Serializable;

public class LiveShoppingModel  implements Serializable {
    private String stream_title;
    private String stream_id;
    private String stream_url;
    private String user_id;
    private String country_name;
    private String stream_type;
    private String PRODUCT_MODEL;
    private String VIDEO_MODEL;

    public String getStream_title() {
        return stream_title;
    }

    public void setStream_title(String stream_title) {
        this.stream_title = stream_title;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getStream_type() {
        return stream_type;
    }

    public void setStream_type(String stream_type) {
        this.stream_type = stream_type;
    }

    public String getPRODUCT_MODEL() {
        return PRODUCT_MODEL;
    }

    public void setPRODUCT_MODEL(String PRODUCT_MODEL) {
        this.PRODUCT_MODEL = PRODUCT_MODEL;
    }

    public String getVIDEO_MODEL() {
        return VIDEO_MODEL;
    }

    public void setVIDEO_MODEL(String VIDEO_MODEL) {
        this.VIDEO_MODEL = VIDEO_MODEL;
    }
}
