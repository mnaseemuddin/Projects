package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Ranjan on 11/12/2019.
 */
public class ModelReviewOrder {

    private String cart_id,products_name,price,quantity,image;

    public ModelReviewOrder(String cart_id, String products_name, String price, String quantity, String image) {
        this.cart_id = cart_id;
        this.products_name = products_name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
