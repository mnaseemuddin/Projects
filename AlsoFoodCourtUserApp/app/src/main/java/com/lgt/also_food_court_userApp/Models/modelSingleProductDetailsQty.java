package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Ranjan on 8/17/2020.
 */
public class modelSingleProductDetailsQty {
    String tv_Quntity,tvFullPrice;

    public modelSingleProductDetailsQty(String tv_Quntity, String tvFullPrice) {
        this.tv_Quntity = tv_Quntity;
        this.tvFullPrice = tvFullPrice;
    }

    public String getTv_Quntity() {
        return tv_Quntity;
    }

    public void setTv_Quntity(String tv_Quntity) {
        this.tv_Quntity = tv_Quntity;
    }

    public String getTvFullPrice() {
        return tvFullPrice;
    }

    public void setTvFullPrice(String tvFullPrice) {
        this.tvFullPrice = tvFullPrice;
    }
}
