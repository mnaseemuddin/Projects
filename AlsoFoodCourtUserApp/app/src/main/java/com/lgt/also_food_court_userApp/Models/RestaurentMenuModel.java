package com.lgt.also_food_court_userApp.Models;

public class RestaurentMenuModel {
    private String food_name,food_quantity;

    public RestaurentMenuModel() {
    }

    public RestaurentMenuModel(String food_name, String food_quantity) {
        this.food_name = food_name;
        this.food_quantity = food_quantity;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(String food_quantity) {
        this.food_quantity = food_quantity;
    }
}
