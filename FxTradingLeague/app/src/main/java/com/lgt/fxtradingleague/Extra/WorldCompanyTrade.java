package com.lgt.fxtradingleague.Extra;

public interface WorldCompanyTrade {

    void selectedItemsCount(int selectedCount);

    void setDataOfSelectedList(String companySymbol,String shareRate);

    // company_name,open_price,daily_high,daily_low,exchange_price,previous_open,previous_close;
    void showDetailsAboutCompany(String company_name,String Symbol,String open_price,String daily_high,String daily_low,String exchange_price,String previous_open,String previous_close,String changesPercentage,String daily_changes);
}
