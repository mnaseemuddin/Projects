package com.app.cryptok.model.Comment;

public class ReplyCommentModel {

    public String comment,comment_id,post_id,user_id,user_image,user_name,reply_id;
    public int comment_type;
    private long timestamp;


    public ReplyCommentModel() {
    }

    public ReplyCommentModel(String comment, String comment_id, String post_id, String user_id, String user_image, String user_name,String reply_id, int comment_type, long timestamp) {
        this.comment = comment;
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_image = user_image;
        this.user_name = user_name;
        this.comment_type = comment_type;
        this.timestamp = timestamp;
        this.reply_id = reply_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
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

    public int getComment_type() {
        return comment_type;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public void setComment_type(int comment_type) {
        this.comment_type = comment_type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
