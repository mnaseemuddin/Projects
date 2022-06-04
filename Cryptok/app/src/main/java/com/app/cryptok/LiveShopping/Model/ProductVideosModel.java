package com.app.cryptok.LiveShopping.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductVideosModel implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("videos_data")
    @Expose
    private List<VideosDatum> videosData = null;
    private final static long serialVersionUID = 2307149529067058458L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VideosDatum> getVideosData() {
        return videosData;
    }

    public void setVideosData(List<VideosDatum> videosData) {
        this.videosData = videosData;
    }

    public static class VideosDatum implements Serializable {

        @SerializedName("tbl_user_video_list_id")
        @Expose
        private String tblUserVideoListId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("video_title")
        @Expose
        private String videoTitle;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("video")
        @Expose
        private String video;
        @SerializedName("video_thumb")
        @Expose
        private String videoThumb;
        private final static long serialVersionUID = 2467257350885994428L;

        public String getTblUserVideoListId() {
            return tblUserVideoListId;
        }

        public void setTblUserVideoListId(String tblUserVideoListId) {
            this.tblUserVideoListId = tblUserVideoListId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getVideoThumb() {
            return videoThumb;
        }

        public void setVideoThumb(String videoThumb) {
            this.videoThumb = videoThumb;
        }

    }

}
