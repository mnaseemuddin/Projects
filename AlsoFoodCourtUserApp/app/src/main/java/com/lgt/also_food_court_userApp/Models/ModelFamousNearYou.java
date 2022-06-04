package com.lgt.also_food_court_userApp.Models;

public class ModelFamousNearYou {

    String product_id,imageURL,productName;

    public ModelFamousNearYou(String product_id, String imageURL, String productName) {
        this.product_id = product_id;
        this.imageURL = imageURL;
        this.productName = productName;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
