package com.app.cryptok.model;

import java.io.Serializable;
import java.util.Date;

public class PostsModel implements Serializable {
    public String user_image,user_name,post_caption,post_id,user_id,post_image;

    public Date timestamp;
    public PostsModel() {
    }

    public PostsModel(String user_image, String user_name, String post_caption, String post_id, String user_id, String post_image, Date timestamp) {
        this.user_image = user_image;
        this.user_name = user_name;
        this.post_caption = post_caption;
        this.post_id = post_id;
        this.user_id = user_id;
        this.post_image = post_image;
        this.timestamp = timestamp;


    }


    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPost_caption() {
        return post_caption;
    }

    public void setPost_caption(String post_caption) {
        this.post_caption = post_caption;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }



    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
