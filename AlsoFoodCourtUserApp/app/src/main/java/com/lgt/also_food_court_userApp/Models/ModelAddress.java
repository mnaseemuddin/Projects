package com.lgt.also_food_court_userApp.Models;

public class ModelAddress {
    String Address_id,tv_address_type, tv_full_address, latitude, logitude;

    public ModelAddress(String address_id, String tv_address_type, String tv_full_address, String latitude, String logitude) {
        Address_id = address_id;
        this.tv_address_type = tv_address_type;
        this.tv_full_address = tv_full_address;
        this.latitude = latitude;
        this.logitude = logitude;
    }

    public String getAddress_id() {
        return Address_id;
    }

    public void setAddress_id(String address_id) {
        Address_id = address_id;
    }

    public String getTv_address_type() {
        return tv_address_type;
    }

    public void setTv_address_type(String tv_address_type) {
        this.tv_address_type = tv_address_type;
    }

    public String getTv_full_address() {
        return tv_full_address;
    }

    public void setTv_full_address(String tv_full_address) {
        this.tv_full_address = tv_full_address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }
}