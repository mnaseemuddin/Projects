package com.lgt.also_food_court_userApp.Models;

public class ModelOffer {


   private String  Restaurant_Id,image, name,discountPercent,tv_Description;

    public ModelOffer(String restaurant_Id, String image, String name, String discountPercent, String tv_Description) {
        Restaurant_Id = restaurant_Id;
        this.image = image;
        this.name = name;
        this.discountPercent = discountPercent;
        this.tv_Description = tv_Description;
    }

    public String getRestaurant_Id() {
        return Restaurant_Id;
    }

    public void setRestaurant_Id(String restaurant_Id) {
        Restaurant_Id = restaurant_Id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getTv_Description() {
        return tv_Description;
    }

    public void setTv_Description(String tv_Description) {
        this.tv_Description = tv_Description;
    }
}
