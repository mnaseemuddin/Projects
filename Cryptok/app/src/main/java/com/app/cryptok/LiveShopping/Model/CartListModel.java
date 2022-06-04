package com.app.cryptok.LiveShopping.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CartListModel implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("cart_total")
    @Expose
    private String cart_total;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    private final static long serialVersionUID = 7448612669423941700L;

    public String getMessage() {
        return message;
    }

    public String getCart_total() {
        return cart_total;
    }

    public void setCart_total(String cart_total) {
        this.cart_total = cart_total;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public static class Datum implements Serializable {

        @SerializedName("tbl_cart_id")
        @Expose
        private String tblCartId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("pro_name")
        @Expose
        private String proName;
        @SerializedName("pro_images")
        @Expose
        private String proImages;

        @SerializedName("quantity")
        @Expose
        private String quantity;


        private final static long serialVersionUID = 3515154585788214829L;

        public String getTblCartId() {
            return tblCartId;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public void setTblCartId(String tblCartId) {
            this.tblCartId = tblCartId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public String getProImages() {
            return proImages;
        }

        public void setProImages(String proImages) {
            this.proImages = proImages;
        }

    }

}
