package com.lgt.Ace11.Bean;

public class PayHistroyModel {

    String status,payment_amt;

    public PayHistroyModel(String status, String payment_amt) {
        this.status = status;
        this.payment_amt = payment_amt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_amt() {
        return payment_amt;
    }

    public void setPayment_amt(String payment_amt) {
        this.payment_amt = payment_amt;
    }
}
