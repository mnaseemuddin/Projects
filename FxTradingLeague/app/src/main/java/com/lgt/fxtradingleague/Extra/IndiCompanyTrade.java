package com.lgt.fxtradingleague.Extra;

import com.lgt.fxtradingleague.IndiModel;

public interface IndiCompanyTrade {

    void selectedItemsCount(int size);

    void setDataOfSelectedList(String symbol,String price);

    void showDetailsAboutCompany(IndiModel indiMod);
}
