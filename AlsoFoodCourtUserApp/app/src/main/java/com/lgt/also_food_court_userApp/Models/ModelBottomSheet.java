package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Ranjan on 11/8/2019.
 */
public class ModelBottomSheet {

    private String isVeg, name, oldPrice, newPrice,differenceAmount,qty,isCustomize;

    public ModelBottomSheet(String isVeg, String name, String oldPrice, String newPrice, String differenceAmount, String qty, String isCustomize) {
        this.isVeg = isVeg;
        this.name = name;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.differenceAmount = differenceAmount;
        this.qty = qty;
        this.isCustomize = isCustomize;
    }

    public String getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(String isVeg) {
        this.isVeg = isVeg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(String differenceAmount) {
        this.differenceAmount = differenceAmount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getIsCustomize() {
        return isCustomize;
    }

    public void setIsCustomize(String isCustomize) {
        this.isCustomize = isCustomize;
    }
}
