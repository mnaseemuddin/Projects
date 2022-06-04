package com.app.arthasattva.Notifications;

import java.io.Serializable;

public class NotificationRequest implements Serializable {
    private String message;
    private String context_message;
    private String user_id;
    private String mantra_id;
    private String notification_type;
    private String alert_type;
    private String notification_data;
    public String getAlert_type() {
        return alert_type;
    }

    public String getNotification_data() {
        return notification_data;
    }

    public void setNotification_data(String notification_data) {
        this.notification_data = notification_data;
    }

    public void setAlert_type(String alert_type) {
        this.alert_type = alert_type;
    }

    public String getMessage() {
        return message;
    }

    public String getContext_message() {
        return context_message;
    }

    public void setContext_message(String context_message) {
        this.context_message = context_message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMantra_id() {
        return mantra_id;
    }

    public void setMantra_id(String mantra_id) {
        this.mantra_id = mantra_id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }
}
