package com.lgt.also_food_court_userApp.Models;

public class FamousRestaurentsModel {
    private String restaurantId,food_image,food_title,food_name,discount,delivery_time,price,tv_restaurantFamousAddress;

    public FamousRestaurentsModel(String restaurantId, String food_image, String food_title, String food_name, String discount, String delivery_time, String price, String tv_restaurantFamousAddress) {
        this.restaurantId = restaurantId;
        this.food_image = food_image;
        this.food_title = food_title;
        this.food_name = food_name;
        this.discount = discount;
        this.delivery_time = delivery_time;
        this.price = price;
        this.tv_restaurantFamousAddress = tv_restaurantFamousAddress;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
    }

    public String getFood_title() {
        return food_title;
    }

    public void setFood_title(String food_title) {
        this.food_title = food_title;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTv_restaurantFamousAddress() {
        return tv_restaurantFamousAddress;
    }

    public void setTv_restaurantFamousAddress(String tv_restaurantFamousAddress) {
        this.tv_restaurantFamousAddress = tv_restaurantFamousAddress;
    }
}
