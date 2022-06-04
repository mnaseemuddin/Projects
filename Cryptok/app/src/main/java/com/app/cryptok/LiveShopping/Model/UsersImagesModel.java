package com.app.cryptok.LiveShopping.Model;

public class UsersImagesModel {
    private String image;
    private String image_id;

    public UsersImagesModel() {
    }

    public UsersImagesModel(String image, String image_id) {
        this.image = image;
        this.image_id = image_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
