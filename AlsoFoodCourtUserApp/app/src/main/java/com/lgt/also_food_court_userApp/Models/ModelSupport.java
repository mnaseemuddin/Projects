package com.lgt.also_food_court_userApp.Models;

public class ModelSupport {

    public static final int MESSAGE_RECEIVED=0;
    public static final int MESSAGE_SENT =1;
    public static final int IMAGE_TYPE=2;

    public static int getMessageReceived() {
        return MESSAGE_RECEIVED;
    }

    public static int getMessageSent() {
        return MESSAGE_SENT;
    }

    public static int getImageType() {
        return IMAGE_TYPE;
    }

    String id,chatMessage,chatImage,userImage,senderName,messageTime;
    int content_type;

    public ModelSupport(String id, String chatMessage, String chatImage, String userImage, String senderName, String messageTime, int content_type) {
        this.id = id;
        this.chatMessage = chatMessage;
        this.chatImage = chatImage;
        this.userImage = userImage;
        this.senderName = senderName;
        this.messageTime = messageTime;
        this.content_type = content_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }
}


