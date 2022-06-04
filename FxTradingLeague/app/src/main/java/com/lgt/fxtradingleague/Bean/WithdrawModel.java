package com.lgt.fxtradingleague.Bean;

public class WithdrawModel {

    String payTypeName;
    int payTypeIcon;

    public WithdrawModel(int payTypeIcon, String payTypeName) {
        this.payTypeIcon = payTypeIcon;
        this.payTypeName = payTypeName;
    }

    public int getPayTypeIcon() {
        return payTypeIcon;
    }

    public void setPayTypeIcon(int payTypeIcon) {
        this.payTypeIcon = payTypeIcon;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
}
