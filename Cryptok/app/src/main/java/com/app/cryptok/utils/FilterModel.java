package com.app.cryptok.utils;

public class FilterModel {
    private String filter_image,filter_name;

    public FilterModel(String filter_image, String filter_name) {
        this.filter_image = filter_image;
        this.filter_name = filter_name;
    }

    public String getFilter_image() {
        return filter_image;
    }

    public void setFilter_image(String filter_image) {
        this.filter_image = filter_image;
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }
}
