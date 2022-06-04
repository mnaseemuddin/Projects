
package com.lgt.also_food_court_userApp.beans.OrderFullDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lgt.also_food_court_userApp.Models.ModelOrderHistory;

import java.util.List;

public class Datum {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("tbl_orders_id")
    @Expose
    private String tblOrdersId;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("GST")
    @Expose
    private String gST;
    @SerializedName("total_order_amt")
    @Expose
    private String totalOrderAmt;
    @SerializedName("products_name")
    @Expose
    private String productsName;
    @SerializedName("products_category")
    @Expose
    private String productsCategory;
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("restaurant_name")
    @Expose
    private String restaurantName;
    @SerializedName("block")
    @Expose
    private String block;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("houseNo")
    @Expose
    private String houseNo;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("nearBy")
    @Expose
    private String nearBy;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("user_pin_code")
    @Expose
    private String userPinCode;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @SerializedName("product_details")
    @Expose
    private List<ModelOrderHistory> singleProduct = null;

    @SerializedName("price")
    @Expose
    private String price;


    public Datum(String productId, String tblOrdersId, String orderNumber, String quantity, String totalPrice, String paymentType, String deliveryCharges, String gST, String totalOrderAmt, String productsName, String productsCategory, String size, String createdDate, String orderStatus, String image, String restaurantName, String block, String address, String pincode, String title, String name, String houseNo, String mobileNo, String nearBy, String country, String state, String district, String city, String userPinCode) {
        this.productId = productId;
        this.tblOrdersId = tblOrdersId;
        this.orderNumber = orderNumber;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
        this.deliveryCharges = deliveryCharges;
        this.gST = gST;
        this.totalOrderAmt = totalOrderAmt;
        this.productsName = productsName;
        this.productsCategory = productsCategory;
        this.size = size;
        this.createdDate = createdDate;
        this.orderStatus = orderStatus;
        this.image = image;
        this.restaurantName = restaurantName;
        this.block = block;
        this.address = address;
        this.pincode = pincode;
        this.title = title;
        this.name = name;
        this.houseNo = houseNo;
        this.mobileNo = mobileNo;
        this.nearBy = nearBy;
        this.country = country;
        this.state = state;
        this.district = district;
        this.city = city;
        this.userPinCode = userPinCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTblOrdersId() {
        return tblOrdersId;
    }

    public void setTblOrdersId(String tblOrdersId) {
        this.tblOrdersId = tblOrdersId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getgST() {
        return gST;
    }

    public void setgST(String gST) {
        this.gST = gST;
    }

    public String getTotalOrderAmt() {
        return totalOrderAmt;
    }

    public void setTotalOrderAmt(String totalOrderAmt) {
        this.totalOrderAmt = totalOrderAmt;
    }

    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public String getProductsCategory() {
        return productsCategory;
    }

    public void setProductsCategory(String productsCategory) {
        this.productsCategory = productsCategory;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNearBy() {
        return nearBy;
    }

    public void setNearBy(String nearBy) {
        this.nearBy = nearBy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserPinCode() {
        return userPinCode;
    }

    public void setUserPinCode(String userPinCode) {
        this.userPinCode = userPinCode;
    }



    public List<ModelOrderHistory> getSingleProduct() {
        return singleProduct;
    }

    public void setSingleProduct(List<ModelOrderHistory> singleProduct) {
        this.singleProduct = singleProduct;
    }




}
