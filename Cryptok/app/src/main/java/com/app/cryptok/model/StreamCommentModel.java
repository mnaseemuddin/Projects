package com.app.cryptok.model;

public class StreamCommentModel {
    private String stream_cmnt_id;
    private String stream_comment;
    private String stream_comment_user_name;
    private String stream_comment_user_id;
    private String comment_type;


    public StreamCommentModel() {
    }

    public StreamCommentModel(String stream_cmnt_id, String stream_comment, String stream_comment_user_name, String stream_comment_user_id, String comment_type) {
        this.stream_cmnt_id = stream_cmnt_id;
        this.stream_comment = stream_comment;
        this.stream_comment_user_name = stream_comment_user_name;
        this.stream_comment_user_id = stream_comment_user_id;
        this.comment_type = comment_type;
    }

    public String getStream_cmnt_id() {
        return stream_cmnt_id;
    }

    public void setStream_cmnt_id(String stream_cmnt_id) {
        this.stream_cmnt_id = stream_cmnt_id;
    }

    public String getStream_comment() {
        return stream_comment;
    }

    public void setStream_comment(String stream_comment) {
        this.stream_comment = stream_comment;
    }

    public String getStream_comment_user_name() {
        return stream_comment_user_name;
    }

    public void setStream_comment_user_name(String stream_comment_user_name) {
        this.stream_comment_user_name = stream_comment_user_name;
    }

    public String getStream_comment_user_id() {
        return stream_comment_user_id;
    }

    public void setStream_comment_user_id(String stream_comment_user_id) {
        this.stream_comment_user_id = stream_comment_user_id;
    }

    public String getComment_type() {
        return comment_type;
    }

    public void setComment_type(String comment_type) {
        this.comment_type = comment_type;
    }
}
