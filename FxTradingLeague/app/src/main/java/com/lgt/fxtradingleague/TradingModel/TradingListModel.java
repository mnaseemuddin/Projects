package com.lgt.fxtradingleague.TradingModel;

public class TradingListModel {
    String tbl_crypto_currency_list_id,currency_id,symbol,name,currency_image,currency_rate,closing_rate,currency_value,
            price_change_percentage_24h,price_change_percentage_7d,price_change_percentage_14d,price_change_percentage_30d;

    public String getCurrency_value() {
        return currency_value;
    }

    public void setCurrency_value(String currency_value) {
        this.currency_value = currency_value;
    }

    public String getClosing_rate() {
        return closing_rate;
    }

    public void setClosing_rate(String closing_rate) {
        this.closing_rate = closing_rate;
    }

    public String getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }

    public void setPrice_change_percentage_24h(String price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    public String getPrice_change_percentage_7d() {
        return price_change_percentage_7d;
    }

    public void setPrice_change_percentage_7d(String price_change_percentage_7d) {
        this.price_change_percentage_7d = price_change_percentage_7d;
    }

    public String getPrice_change_percentage_14d() {
        return price_change_percentage_14d;
    }

    public void setPrice_change_percentage_14d(String price_change_percentage_14d) {
        this.price_change_percentage_14d = price_change_percentage_14d;
    }

    public String getPrice_change_percentage_30d() {
        return price_change_percentage_30d;
    }

    public void setPrice_change_percentage_30d(String price_change_percentage_30d) {
        this.price_change_percentage_30d = price_change_percentage_30d;
    }

    private boolean isSelected;
    public TradingListModel(String tbl_crypto_currency_list_id, String currency_id, String symbol, String name, String currency_image, String currency_rate,Boolean setCheck) {
        this.tbl_crypto_currency_list_id = tbl_crypto_currency_list_id;
        this.currency_id = currency_id;
        this.symbol = symbol;
        this.name = name;
        this.currency_image = currency_image;
        this.currency_rate = currency_rate;
        this.isSelected = setCheck;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTbl_crypto_currency_list_id() {
        return tbl_crypto_currency_list_id;
    }

    public void setTbl_crypto_currency_list_id(String tbl_crypto_currency_list_id) {
        this.tbl_crypto_currency_list_id = tbl_crypto_currency_list_id;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency_image() {
        return currency_image;
    }

    public void setCurrency_image(String currency_image) {
        this.currency_image = currency_image;
    }

    public String getCurrency_rate() {
        return currency_rate;
    }

    public void setCurrency_rate(String currency_rate) {
        this.currency_rate = currency_rate;
    }
}
