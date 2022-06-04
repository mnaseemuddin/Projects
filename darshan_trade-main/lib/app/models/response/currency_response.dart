// To parse this JSON data, do
//
//     final currencyModel = currencyModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<CurrencyModel> currencyModelFromJson(String str) => List<CurrencyModel>.from(json.decode(str).map((x) => CurrencyModel.fromJson(x)));

String currencyModelToJson(List<CurrencyModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class CurrencyModel {
  CurrencyModel({
    required this.symbol,
    required this.priceChangePercent,
    required this.lastPrice,
    required this.lastQty,
    required this.strategyMode,
  });

  final String symbol;
  final String priceChangePercent;
  final String lastPrice;
  final String lastQty;
  final int strategyMode;

  factory CurrencyModel.fromJson(Map<String, dynamic> json) => CurrencyModel(
    symbol: json["symbol"]??'',
    priceChangePercent: json["priceChangePercent"]??'0.0',
    lastPrice: json["lastPrice"]??'0.0',
    lastQty: json["lastQty"]??'0.0',
    strategyMode: json["strategyMode"]??0,
  );

  Map<String, dynamic> toJson() => {
    "symbol": symbol,
    "priceChangePercent": priceChangePercent,
    "lastPrice": lastPrice,
    "lastQty": lastQty,
    "strategyMode": strategyMode,
  };
}
