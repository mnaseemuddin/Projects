package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Ranjan on 25/09/2019.
 */
public class ModelTrendingFoods {
    private String productId,name,reviews,rating,offers,image;

    public ModelTrendingFoods(String productId, String name, String reviews, String rating, String offers, String image) {
        this.productId = productId;
        this.name = name;
        this.reviews = reviews;
        this.rating = rating;
        this.offers = offers;
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
