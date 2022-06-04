// To parse this JSON data, do
//
//     final userCurrencyResponse = userCurrencyResponseFromJson(jsonString);

// [{"symbol":"XRPUSDT","priceChange":0.03510000,"quantity":10.00000000,
// "price":1.00760000,"profit":0.08,"returnprofit":1.1951,"cyclemode":1}]

import 'package:meta/meta.dart';
import 'dart:convert';

List<UserCurrencyResponse> userCurrencyResponseFromJson(String str) => List<UserCurrencyResponse>.from(json.decode(str).map((x) => UserCurrencyResponse.fromJson(x)));

String userCurrencyResponseToJson(List<UserCurrencyResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class UserCurrencyResponse {
  UserCurrencyResponse({
    required this.symbol,
    required this.priceChange,
    required this.quantity,
    required this.price,
    required this.profit,
    required this.returnprofit,
    required this.cyclemode,
    required this.marginlimit,
    required this.strategyMode,
  });

  final String symbol;
  final double priceChange;
  final double quantity;
  final double price;
  final double profit;
  final double returnprofit;
  final int cyclemode;
  final int marginlimit;
  final int strategyMode;


  factory UserCurrencyResponse.fromJson(Map<String, dynamic> json) => UserCurrencyResponse(
    symbol: json["symbol"]??'',
    priceChange: json["priceChange"].toDouble(),
    quantity: json["quantity"].toDouble(),
    price: json["price"].toDouble(),
    profit: json["profit"].toDouble(),
    returnprofit: json["returnprofit"].toDouble(),
    cyclemode: json["cyclemode"]??1,
    marginlimit: json["marginlimit"]??1,
    strategyMode: json["strategyMode"]??0,
  );

  Map<String, dynamic> toJson() => {
    "symbol": symbol,
    "priceChange": priceChange,
    "quantity": quantity,
    "price": price,
    "profit": profit,
    "returnprofit": returnprofit,
    "cyclemode": cyclemode,
    "marginlimit": marginlimit,
    "strategyMode": strategyMode,
  };
}
