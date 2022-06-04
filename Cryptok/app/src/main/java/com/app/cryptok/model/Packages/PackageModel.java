package com.app.cryptok.model.Packages;

import java.io.Serializable;

public class PackageModel implements Serializable {
    private String tbl_package_id;
    private String package_name;
    private String coin;
    private String total_coins;
    private String amount;

    public PackageModel() {
    }

    public PackageModel(String tbl_package_id, String package_name, String coin, String total_coins, String amount) {
        this.tbl_package_id = tbl_package_id;
        this.package_name = package_name;
        this.coin = coin;
        this.total_coins = total_coins;
        this.amount = amount;
    }

    public String getTbl_package_id() {
        return tbl_package_id;
    }

    public void setTbl_package_id(String tbl_package_id) {
        this.tbl_package_id = tbl_package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getTotal_coins() {
        return total_coins;
    }

    public void setTotal_coins(String total_coins) {
        this.total_coins = total_coins;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
