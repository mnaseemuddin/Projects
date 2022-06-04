package com.lgt.also_food_court_userApp.beans.OrderFullDetail;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleProductDetails {
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("product_image")
    @Expose
    private String product_image;

    public SingleProductDetails(String product_name, String quantity, String size, String product_image) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.size = size;
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
