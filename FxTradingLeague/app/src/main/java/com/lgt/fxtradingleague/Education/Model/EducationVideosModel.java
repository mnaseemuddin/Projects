package com.lgt.fxtradingleague.Education.Model;

import java.io.Serializable;
import java.util.List;

public class EducationVideosModel implements Serializable {
    private String education_category;
    private String education_category_id;
    public List<Videos> videosList = null;

    public String getEducation_category() {
        return education_category;
    }

    public void setEducation_category(String education_category) {
        this.education_category = education_category;
    }

    public String getEducation_category_id() {
        return education_category_id;
    }

    public void setEducation_category_id(String education_category_id) {
        this.education_category_id = education_category_id;
    }

    public List<Videos> getVideosList() {
        return videosList;
    }

    public void setVideosList(List<Videos> videosList) {
        this.videosList = videosList;
    }

    public static class Videos implements Serializable {
        private String video_thumb;
        private String video_url;
        private String video_id;
        private String course_title;
        private String course_desc;


        public String getCourse_title() {
            return course_title;
        }

        public void setCourse_title(String course_title) {
            this.course_title = course_title;
        }

        public String getCourse_desc() {
            return course_desc;
        }

        public void setCourse_desc(String course_desc) {
            this.course_desc = course_desc;
        }

        public String getVideo_thumb() {
            return video_thumb;
        }

        public void setVideo_thumb(String video_thumb) {
            this.video_thumb = video_thumb;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }
    }
}
