package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Ranjan on 7/11/2020.
 */
public class ModelMyCurrentOrder {
   private String producd_No,productImage,orderId,productName,productPrice;

    public ModelMyCurrentOrder(String producd_No, String productImage, String orderId, String productName, String productPrice) {
        this.producd_No = producd_No;
        this.productImage = productImage;
        this.orderId = orderId;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProducd_No() {
        return producd_No;
    }

    public void setProducd_No(String producd_No) {
        this.producd_No = producd_No;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
