package com.app.cryptok.LiveShopping.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryModel implements Serializable {
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

        @SerializedName("tbl_orders_id")
        @Expose
        private String tblOrdersId;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("delivery_charges")
        @Expose
        private String deliveryCharges;
        @SerializedName("order_amount")
        @Expose
        private String orderAmount;
        @SerializedName("order_subtotal")
        @Expose
        private String orderSubtotal;
        @SerializedName("total_order_products")
        @Expose
        private String totalOrderProducts;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;
        @SerializedName("order_status")
        @Expose
        private String orderStatus;
        private final static long serialVersionUID = -2572723791048217252L;

        public String getTblOrdersId() {
            return tblOrdersId;
        }

        public void setTblOrdersId(String tblOrdersId) {
            this.tblOrdersId = tblOrdersId;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getDeliveryCharges() {
            return deliveryCharges;
        }

        public void setDeliveryCharges(String deliveryCharges) {
            this.deliveryCharges = deliveryCharges;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderSubtotal() {
            return orderSubtotal;
        }

        public void setOrderSubtotal(String orderSubtotal) {
            this.orderSubtotal = orderSubtotal;
        }

        public String getTotalOrderProducts() {
            return totalOrderProducts;
        }

        public void setTotalOrderProducts(String totalOrderProducts) {
            this.totalOrderProducts = totalOrderProducts;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

    }


}
