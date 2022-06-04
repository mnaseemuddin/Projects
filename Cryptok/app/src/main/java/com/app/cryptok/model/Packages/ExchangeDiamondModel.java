package com.app.cryptok.model.Packages;

public class ExchangeDiamondModel {
    private String tbl_exchange_diamond_id;
    private String beans;
    private String diamonds;

    public ExchangeDiamondModel() {
    }

    public ExchangeDiamondModel(String tbl_exchange_diamond_id, String beans, String diamonds) {
        this.tbl_exchange_diamond_id = tbl_exchange_diamond_id;
        this.beans = beans;
        this.diamonds = diamonds;
    }

    public String getTbl_exchange_diamond_id() {
        return tbl_exchange_diamond_id;
    }

    public void setTbl_exchange_diamond_id(String tbl_exchange_diamond_id) {
        this.tbl_exchange_diamond_id = tbl_exchange_diamond_id;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public String getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(String diamonds) {
        this.diamonds = diamonds;
    }
}
