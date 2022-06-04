package com.app.cryptok.model;

public class CountryModel {
    private String country_name,country_code,country_id;

    public CountryModel() {

    }

    public CountryModel(String country_name, String country_code, String country_id) {
        this.country_name = country_name;
        this.country_code = country_code;
        this.country_id = country_id;
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

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
