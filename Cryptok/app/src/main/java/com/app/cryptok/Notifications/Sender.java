package com.app.cryptok.Notifications;

public class Sender {

    public NotificationRequest data;
    public String to;

    public Sender() {
    }

    public Sender(NotificationRequest data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationRequest getData() {
        return data;
    }

    public void setData(NotificationRequest data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
