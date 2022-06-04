package com.app.cryptok.model;

import java.io.Serializable;

public class ProfilePOJO implements Serializable {
    private String email;
    private String name;
    private String userId;
    private String super_live_id;
    private String gender;
    private String dob;
    private String hometown;
    private String bio;
    private String image;
    private String mobile;
    private String loginType;
    private String country_name;
    private String country_code;
    private String auth_status;
    private String current_level;
    private int point;
    private int total_recieved_beans;
    private int total_sent_diamonds;


    public ProfilePOJO() {

    }

    public ProfilePOJO(String email, String name, String userId, String super_live_id, String gender, String dob, String hometown, String bio, String image, String mobile, String loginType, String country_name, String country_code, String auth_status, String current_level, int point, int total_recieved_beans, int total_sent_diamonds) {
        this.email = email;
        this.name = name;
        this.userId = userId;
        this.super_live_id = super_live_id;
        this.gender = gender;
        this.dob = dob;
        this.hometown = hometown;
        this.bio = bio;
        this.image = image;
        this.mobile = mobile;
        this.loginType = loginType;
        this.country_name = country_name;
        this.country_code = country_code;
        this.auth_status = auth_status;
        this.current_level = current_level;
        this.point = point;
        this.total_recieved_beans = total_recieved_beans;
        this.total_sent_diamonds = total_sent_diamonds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSuper_live_id() {
        return super_live_id;
    }

    public void setSuper_live_id(String super_live_id) {
        this.super_live_id = super_live_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(String auth_status) {
        this.auth_status = auth_status;
    }

    public String getCurrent_level() {
        return current_level;
    }

    public void setCurrent_level(String current_level) {
        this.current_level = current_level;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getTotal_recieved_beans() {
        return total_recieved_beans;
    }

    public void setTotal_recieved_beans(int total_recieved_beans) {
        this.total_recieved_beans = total_recieved_beans;
    }

    public int getTotal_sent_diamonds() {
        return total_sent_diamonds;
    }

    public void setTotal_sent_diamonds(int total_sent_diamonds) {
        this.total_sent_diamonds = total_sent_diamonds;
    }
}
