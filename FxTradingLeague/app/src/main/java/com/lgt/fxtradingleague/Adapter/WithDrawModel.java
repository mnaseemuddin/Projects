package com.lgt.fxtradingleague.Adapter;

public class WithDrawModel {

    String tbl_withdrawn_request_id,user_id,mobile,withdrawn_amount,bank_holder_name,account_number,IFSC,status,request_date;

    public WithDrawModel(String tbl_withdrawn_request_id, String user_id, String mobile, String withdrawn_amount, String bank_holder_name, String account_number, String IFSC, String status, String request_date) {
        this.tbl_withdrawn_request_id = tbl_withdrawn_request_id;
        this.user_id = user_id;
        this.mobile = mobile;
        this.withdrawn_amount = withdrawn_amount;
        this.bank_holder_name = bank_holder_name;
        this.account_number = account_number;
        this.IFSC = IFSC;
        this.status = status;
        this.request_date = request_date;
    }

    public String getTbl_withdrawn_request_id() {
        return tbl_withdrawn_request_id;
    }

    public void setTbl_withdrawn_request_id(String tbl_withdrawn_request_id) {
        this.tbl_withdrawn_request_id = tbl_withdrawn_request_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWithdrawn_amount() {
        return withdrawn_amount;
    }

    public void setWithdrawn_amount(String withdrawn_amount) {
        this.withdrawn_amount = withdrawn_amount;
    }

    public String getBank_holder_name() {
        return bank_holder_name;
    }

    public void setBank_holder_name(String bank_holder_name) {
        this.bank_holder_name = bank_holder_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }
}
