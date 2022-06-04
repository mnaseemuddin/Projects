package com.app.cryptok.LiveShopping.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DeliveryChargesModel implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
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

        @SerializedName("tbl_delivery_charges_id")
        @Expose
        private String tblDeliveryChargesId;
        @SerializedName("cart_total")
        @Expose
        private String cartTotal;
        @SerializedName("charges")
        @Expose
        private String charges;
        @SerializedName("status")
        @Expose
        private String status;
        private final static long serialVersionUID = -7628146080813921540L;

        public String getTblDeliveryChargesId() {
            return tblDeliveryChargesId;
        }

        public void setTblDeliveryChargesId(String tblDeliveryChargesId) {
            this.tblDeliveryChargesId = tblDeliveryChargesId;
        }

        public String getCartTotal() {
            return cartTotal;
        }

        public void setCartTotal(String cartTotal) {
            this.cartTotal = cartTotal;
        }

        public String getCharges() {
            return charges;
        }

        public void setCharges(String charges) {
            this.charges = charges;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}
