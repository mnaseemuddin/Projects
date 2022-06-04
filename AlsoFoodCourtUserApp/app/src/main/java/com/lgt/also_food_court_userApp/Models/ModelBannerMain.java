package com.lgt.also_food_court_userApp.Models;

public class ModelBannerMain {
    String id,imageURL;

    public ModelBannerMain(String id, String imageURL) {
        this.id = id;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
