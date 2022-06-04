package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Ranjan on 11/8/2019.
 */
public class ModelProductList {

    String tbl_restaurant_products_id,products_name, product_type ,image,rating,review,review_count,price,tv_fullPrice;

    public ModelProductList(String tbl_restaurant_products_id, String products_name, String product_type, String image, String rating, String review, String review_count, String price, String tv_fullPrice) {
        this.tbl_restaurant_products_id = tbl_restaurant_products_id;
        this.products_name = products_name;
        this.product_type = product_type;
        this.image = image;
        this.rating = rating;
        this.review = review;
        this.review_count = review_count;
        this.price = price;
        this.tv_fullPrice = tv_fullPrice;
    }

    public String getTbl_restaurant_products_id() {
        return tbl_restaurant_products_id;
    }

    public void setTbl_restaurant_products_id(String tbl_restaurant_products_id) {
        this.tbl_restaurant_products_id = tbl_restaurant_products_id;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTv_fullPrice() {
        return tv_fullPrice;
    }

    public void setTv_fullPrice(String tv_fullPrice) {
        this.tv_fullPrice = tv_fullPrice;
    }
}
