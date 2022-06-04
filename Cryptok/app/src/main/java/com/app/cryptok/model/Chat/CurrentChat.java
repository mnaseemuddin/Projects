package com.app.cryptok.model.Chat;

import java.util.Date;

public class CurrentChat {
    private String user_id;
    private Date timestamp;
    private String message;
    private String messageid;

    public CurrentChat() {
    }

    public CurrentChat(String user_id, Date timestamp, String message, String messageid) {
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.message = message;
        this.messageid = messageid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
}
