package com.app.arthasattva.model;

public class StreamViewersModel {
    public String user_id,user_name,user_image;

    public StreamViewersModel() {
    }

    public StreamViewersModel(String user_id, String user_name, String user_image) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_image = user_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
