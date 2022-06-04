package com.lgt.also_food_court_userApp.Models;

public class ModelVegeterians {
    String id,imageURL,name,details,rating,discount,time,coupon,amount,tv_closeTime,tv_Address;

    public ModelVegeterians(String id, String imageURL, String name, String details, String rating, String discount, String time, String coupon, String amount, String tv_closeTime, String tv_Address) {
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
        this.details = details;
        this.rating = rating;
        this.discount = discount;
        this.time = time;
        this.coupon = coupon;
        this.amount = amount;
        this.tv_closeTime = tv_closeTime;
        this.tv_Address = tv_Address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTv_closeTime() {
        return tv_closeTime;
    }

    public void setTv_closeTime(String tv_closeTime) {
        this.tv_closeTime = tv_closeTime;
    }

    public String getTv_Address() {
        return tv_Address;
    }

    public void setTv_Address(String tv_Address) {
        this.tv_Address = tv_Address;
    }
}
