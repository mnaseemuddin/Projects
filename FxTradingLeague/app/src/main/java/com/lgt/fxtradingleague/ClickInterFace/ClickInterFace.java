package com.lgt.fxtradingleague.ClickInterFace;

import com.lgt.fxtradingleague.TradingAdapter.TradeListAdapter;

public interface ClickInterFace {

    void itemCountList(int itemsSelect, String check, TradeListAdapter.TradeHolder holder);

    void savedSelectTradingDetails(String CurrencyName,String ActualPrice,String PurchasedTokenPrice);

    void displayCoinDetails(String name,String symbol,String currentRate,String closingRate,String OneDay,String sevenDays,String FourteenDays,String month);

}
