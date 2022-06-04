package com.app.cryptok.model;

import java.io.Serializable;
import java.util.Date;

public class RecievedNotificationModel implements Serializable {
    private String alert_type;
    private String context_message;
    private String message;
    private String my_user_id;
    private String notification_id;
    private String notification_type;
    private String super_live_id;
    private String user_id;
    private String notification_data;
    private Date timestamp;

    public RecievedNotificationModel() {
    }

    public RecievedNotificationModel(String alert_type, String context_message, String message, String my_user_id, String notification_id, String notification_type, String super_live_id, String user_id, String notification_data, Date timestamp) {
        this.alert_type = alert_type;
        this.context_message = context_message;
        this.message = message;
        this.my_user_id = my_user_id;
        this.notification_id = notification_id;
        this.notification_type = notification_type;
        this.super_live_id = super_live_id;
        this.user_id = user_id;
        this.notification_data = notification_data;
        this.timestamp = timestamp;
    }

    public String getNotification_data() {
        return notification_data;
    }

    public void setNotification_data(String notification_data) {
        this.notification_data = notification_data;
    }

    public String getAlert_type() {
        return alert_type;
    }

    public void setAlert_type(String alert_type) {
        this.alert_type = alert_type;
    }

    public String getContext_message() {
        return context_message;
    }

    public void setContext_message(String context_message) {
        this.context_message = context_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMy_user_id() {
        return my_user_id;
    }

    public void setMy_user_id(String my_user_id) {
        this.my_user_id = my_user_id;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getSuper_live_id() {
        return super_live_id;
    }

    public void setSuper_live_id(String super_live_id) {
        this.super_live_id = super_live_id;
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
}
