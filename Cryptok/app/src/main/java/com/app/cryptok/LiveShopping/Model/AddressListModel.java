package com.app.cryptok.LiveShopping.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddressListModel implements Serializable {
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
    public static class Datum implements Serializable
    {

        @SerializedName("address_id")
        @Expose
        private String addressId;
        @SerializedName("address_title")
        @Expose
        private String addressTitle;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("houseNo")
        @Expose
        private String houseNo;
        @SerializedName("mobileNo")
        @Expose
        private String mobileNo;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("nearBy")
        @Expose
        private String nearBy;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("city")
        @Expose
        private String city;

        @SerializedName("pin_code")
        @Expose
        private String pinCode;
        @SerializedName("state")
        @Expose
        private String state;


        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("address")
        @Expose
        private String address;
        private final static long serialVersionUID = 3460515770806025779L;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getAddressTitle() {
            return addressTitle;
        }

        public void setAddressTitle(String addressTitle) {
            this.addressTitle = addressTitle;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNearBy() {
            return nearBy;
        }

        public void setNearBy(String nearBy) {
            this.nearBy = nearBy;
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



        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }



        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }

}
