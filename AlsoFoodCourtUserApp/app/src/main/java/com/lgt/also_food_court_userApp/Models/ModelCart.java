package com.lgt.also_food_court_userApp.Models;

public class ModelCart {
   private String cart_id,products_name,price,quantity,image,products_price;

    public ModelCart(String cart_id, String products_name, String price, String quantity, String image, String products_price) {
        this.cart_id = cart_id;
        this.products_name = products_name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.products_price = products_price;
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

    public String getProducts_price() {
        return products_price;
    }

    public void setProducts_price(String products_price) {
        this.products_price = products_price;
    }
}
