package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Qasim on 6/13/2020.
 */
public class ModelReviewProduct {
    String userName,reviewTime,customerReview;
    String RatingBar;

    public ModelReviewProduct(String userName, String reviewTime, String customerReview, String ratingBar) {
        this.userName = userName;
        this.reviewTime = reviewTime;
        this.customerReview = customerReview;
        RatingBar = ratingBar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getCustomerReview() {
        return customerReview;
    }

    public void setCustomerReview(String customerReview) {
        this.customerReview = customerReview;
    }

    public String getRatingBar() {
        return RatingBar;
    }

    public void setRatingBar(String ratingBar) {
        RatingBar = ratingBar;
    }
}
