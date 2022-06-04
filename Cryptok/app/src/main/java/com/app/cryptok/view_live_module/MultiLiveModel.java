package com.app.cryptok.view_live_module;

public class MultiLiveModel {
    private String stream_url;
    private String stream_title;
    private String stream_id;
    private String stream_image;
    private String user_id;
    private String stream_user_name;
    private String total_seats;

    public MultiLiveModel() {
    }

    public MultiLiveModel(String stream_url, String stream_title, String stream_id, String stream_image, String user_id, String stream_user_name, String total_seats) {
        this.stream_url = stream_url;
        this.stream_title = stream_title;
        this.stream_id = stream_id;
        this.stream_image = stream_image;
        this.user_id = user_id;
        this.stream_user_name = stream_user_name;
        this.total_seats = total_seats;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getStream_title() {
        return stream_title;
    }

    public void setStream_title(String stream_title) {
        this.stream_title = stream_title;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_image() {
        return stream_image;
    }

    public void setStream_image(String stream_image) {
        this.stream_image = stream_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStream_user_name() {
        return stream_user_name;
    }

    public void setStream_user_name(String stream_user_name) {
        this.stream_user_name = stream_user_name;
    }

    public String getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(String total_seats) {
        this.total_seats = total_seats;
    }
}
