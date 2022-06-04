package com.lgt.also_food_court_userApp.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelOrderHistory {

    private String tbl_orders_id,order_number,total_price,products_name,created_date,order_status,image;

    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("size")
    @Expose
    private String Size;
    @SerializedName("product_image")
    @Expose
    private String product_image;
    @SerializedName("product_price")
    @Expose
    private String product_price;

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public ModelOrderHistory(String tbl_orders_id, String order_number, String quantity, String total_price, String products_name, String size, String created_date, String order_status, String image) {
        this.tbl_orders_id = tbl_orders_id;
        this.order_number = order_number;
        this.quantity = quantity;
        this.total_price = total_price;
        this.products_name = products_name;
        Size = size;
        this.created_date = created_date;
        this.order_status = order_status;
        this.image = image;
    }

    public String getTbl_orders_id() {
        return tbl_orders_id;
    }

    public void setTbl_orders_id(String tbl_orders_id) {
        this.tbl_orders_id = tbl_orders_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
