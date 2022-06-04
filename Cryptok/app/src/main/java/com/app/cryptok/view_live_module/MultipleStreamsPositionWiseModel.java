package com.app.cryptok.view_live_module;

public class MultipleStreamsPositionWiseModel {
    private String stream_id;
    private String stream_url;
    private String total_seats;
    private String stream_image;
    private String user_id;
    private String stream_user_name;
    private String stream_position;

    public MultipleStreamsPositionWiseModel() {
    }

    public MultipleStreamsPositionWiseModel(String stream_id, String stream_url, String total_seats, String stream_image, String user_id, String stream_user_name, String stream_position) {
        this.stream_id = stream_id;
        this.stream_url = stream_url;
        this.total_seats = total_seats;
        this.stream_image = stream_image;
        this.user_id = user_id;
        this.stream_user_name = stream_user_name;
        this.stream_position = stream_position;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(String total_seats) {
        this.total_seats = total_seats;
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

    public String getStream_position() {
        return stream_position;
    }

    public void setStream_position(String stream_position) {
        this.stream_position = stream_position;
    }
}
