


// To parse this JSON data, do
//
//     final fCurrencyModel = fCurrencyModelFromJson(jsonString);

import 'dart:convert';

List<FBUSDCurrencyModel> fBUSDCurrencyModelFromJson(String str) => List<FBUSDCurrencyModel>.from(json.decode(str).map((x) => FBUSDCurrencyModel.fromJson(x)));

String fBUSDCurrencyModelToJson(List<FBUSDCurrencyModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class FBUSDCurrencyModel {
  FBUSDCurrencyModel({
    required this.symbol,
    required this.priceChangePercent,
    required this.lastPrice,
    required this.lastQty,
  });

  String symbol;
  String priceChangePercent;
  String lastPrice;
  String lastQty;

  factory FBUSDCurrencyModel.fromJson(Map<String, dynamic> json) => FBUSDCurrencyModel(
    symbol: json["symbol"]??"",
    priceChangePercent: json["priceChangePercent"]?? "0.0",
    lastPrice: json["lastPrice"]?? "0.0",
    lastQty: json["lastQty"]?? "0.0",
  );

  Map<String, dynamic> toJson() => {
    "symbol": symbol,
    "priceChangePercent": priceChangePercent,
    "lastPrice": lastPrice,
    "lastQty": lastQty,
  };
}
