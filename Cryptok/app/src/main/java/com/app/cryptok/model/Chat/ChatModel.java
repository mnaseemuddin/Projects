package com.app.cryptok.model.Chat;

public class ChatModel {
    private String messageid, filename, from, message, seen,user_id;
    private int messageType;
    private long timestamp;

    public ChatModel() {

    }

    public ChatModel(String messageid, String filename, String from, String message, String seen, String user_id, int messageType, long timestamp) {
        this.messageid = messageid;
        this.filename = filename;
        this.from = from;
        this.message = message;
        this.seen = seen;
        this.user_id = user_id;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
