package com.lgt.also_food_court_userApp.Models;

public class ModelCategories {
    String id,details,imageURL;

    public ModelCategories(String id, String details, String imageURL) {
        this.id = id;
        this.details = details;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
