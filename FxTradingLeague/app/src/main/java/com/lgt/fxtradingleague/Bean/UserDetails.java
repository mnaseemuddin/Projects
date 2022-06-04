package com.lgt.fxtradingleague.Bean;

/**
 * Created by telasol1 on 23-Oct-18.
 */

public class UserDetails {

    private String user_id;
    private String name;
    private String dob;
    private String mobile;
    private String email;
    private String type;
    private String verify;
    private String referral_code;
    private String BITCOIN_ADDRESS;
    private String BITCOIN_LABEL;
    private String BITCOIN_USERID;

    public String getBITCOIN_ADDRESS() {
        return BITCOIN_ADDRESS;
    }

    public void setBITCOIN_ADDRESS(String BITCOIN_ADDRESS) {
        this.BITCOIN_ADDRESS = BITCOIN_ADDRESS;
    }

    public String getBITCOIN_LABEL() {
        return BITCOIN_LABEL;
    }

    public void setBITCOIN_LABEL(String BITCOIN_LABEL) {
        this.BITCOIN_LABEL = BITCOIN_LABEL;
    }

    public String getBITCOIN_USERID() {
        return BITCOIN_USERID;
    }

    public void setBITCOIN_USERID(String BITCOIN_USERID) {
        this.BITCOIN_USERID = BITCOIN_USERID;
    }

    private String city;
    private String country;
    private String pincode;
    private String state;
    private String address;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
