
package com.lgt.also_food_court_userApp.beans.RestaurantOffer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("tbl_offer_banner_id")
    @Expose
    private String tblOfferBannerId;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("image")
    @Expose
    private String image;

    public Datum(String tblOfferBannerId, String offer, String image) {
        this.tblOfferBannerId = tblOfferBannerId;
        this.offer = offer;
        this.image = image;
    }

    public String getTblOfferBannerId() {
        return tblOfferBannerId;
    }

    public void setTblOfferBannerId(String tblOfferBannerId) {
        this.tblOfferBannerId = tblOfferBannerId;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
