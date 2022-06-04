package com.lgt.fxtradingleague.Education.Model;

import java.io.Serializable;

public class EducationSliderModel implements Serializable {
    private String slider_id;
    private String slider_image;

    public String getSlider_id() {
        return slider_id;
    }

    public void setSlider_id(String slider_id) {
        this.slider_id = slider_id;
    }

    public String getSlider_image() {
        return slider_image;
    }

    public void setSlider_image(String slider_image) {
        this.slider_image = slider_image;
    }
}
