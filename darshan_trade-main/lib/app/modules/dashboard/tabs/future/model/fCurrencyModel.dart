


// To parse this JSON data, do
//
//     final fCurrencyModel = fCurrencyModelFromJson(jsonString);

import 'dart:convert';

List<FUSDTCurrencyModel> fCurrencyModelFromJson(String str) => List<FUSDTCurrencyModel>.from(json.decode(str).map((x) => FUSDTCurrencyModel.fromJson(x)));

String fCurrencyModelToJson(List<FUSDTCurrencyModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class FUSDTCurrencyModel {
  FUSDTCurrencyModel({
    required this.symbol,
    required this.priceChangePercent,
    required this.lastPrice,
    required this.lastQty,
  });

  String symbol;
  String priceChangePercent;
  String lastPrice;
  String lastQty;

  factory FUSDTCurrencyModel.fromJson(Map<String, dynamic> json) => FUSDTCurrencyModel(
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
