package com.app.cryptok.model.Follow;

public class FollowModel {
    private String follow_id,user_id;

    public FollowModel() {
    }

    public FollowModel(String follow_id, String user_id) {
        this.follow_id = follow_id;
        this.user_id = user_id;
    }

    public String getFollow_id() {
        return follow_id;
    }

    public void setFollow_id(String follow_id) {
        this.follow_id = follow_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
