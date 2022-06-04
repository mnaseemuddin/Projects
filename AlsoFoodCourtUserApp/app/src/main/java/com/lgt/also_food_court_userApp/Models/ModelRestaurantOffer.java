package com.lgt.also_food_court_userApp.Models;

public class ModelRestaurantOffer {
    private String restaurant_Id,food_image,food_name,tv_restaurantDiscounted;

    public ModelRestaurantOffer(String restaurant_Id, String food_image, String food_name, String tv_restaurantDiscounted) {
        this.restaurant_Id = restaurant_Id;
        this.food_image = food_image;
        this.food_name = food_name;
        this.tv_restaurantDiscounted = tv_restaurantDiscounted;
    }

    public String getRestaurant_Id() {
        return restaurant_Id;
    }

    public void setRestaurant_Id(String restaurant_Id) {
        this.restaurant_Id = restaurant_Id;
    }

    public String getFood_image() {
        return food_image;
    }

    public void setFood_image(String food_image) {
        this.food_image = food_image;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getTv_restaurantDiscounted() {
        return tv_restaurantDiscounted;
    }

    public void setTv_restaurantDiscounted(String tv_restaurantDiscounted) {
        this.tv_restaurantDiscounted = tv_restaurantDiscounted;
    }
}
