package com.lgt.Ace11.TrakNPayPackage;

public class PackModel {

    String tbl_coin_package_id, package_name, package_value, package_coins;

    public PackModel(String tbl_coin_package_id, String package_name, String package_value, String package_coins) {
        this.tbl_coin_package_id = tbl_coin_package_id;
        this.package_name = package_name;
        this.package_value = package_value;
        this.package_coins = package_coins;
    }

    public String getTbl_coin_package_id() {
        return tbl_coin_package_id;
    }

    public void setTbl_coin_package_id(String tbl_coin_package_id) {
        this.tbl_coin_package_id = tbl_coin_package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_value() {
        return package_value;
    }

    public void setPackage_value(String package_value) {
        this.package_value = package_value;
    }

    public String getPackage_coins() {
        return package_coins;
    }

    public void setPackage_coins(String package_coins) {
        this.package_coins = package_coins;
    }
}
