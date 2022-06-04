package com.lgt.also_food_court_userApp.Models;


public class modelBottomSheetAddProduct {
    private String Quantity_ID,quantityType,ProductAmount,MainPrice;
    private Boolean isSelected;

    public modelBottomSheetAddProduct(String quantity_ID, String quantityType, String productAmount, String mainPrice, Boolean isSelected) {
        Quantity_ID = quantity_ID;
        this.quantityType = quantityType;
        ProductAmount = productAmount;
        MainPrice = mainPrice;
        this.isSelected = isSelected;
    }

    public String getQuantity_ID() {
        return Quantity_ID;
    }

    public void setQuantity_ID(String quantity_ID) {
        Quantity_ID = quantity_ID;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public String getProductAmount() {
        return ProductAmount;
    }

    public void setProductAmount(String productAmount) {
        ProductAmount = productAmount;
    }

    public String getMainPrice() {
        return MainPrice;
    }

    public void setMainPrice(String mainPrice) {
        MainPrice = mainPrice;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
