// To parse this JSON data, do
//
//     final boosterUserListResponse = boosterUserListResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<BoosterUserListResponse> boosterUserListResponseFromJson(String str) => List<BoosterUserListResponse>.from(json.decode(str).map((x) => BoosterUserListResponse.fromJson(x)));

String boosterUserListResponseToJson(List<BoosterUserListResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class BoosterUserListResponse {
  BoosterUserListResponse({
    required this.symbol,
    required this.priceChange,
    required this.quantity,
    required this.price,
    required this.profit,
    required this.returnprofit,
    required this.boosterMode,
  });

  final String symbol;
  final double priceChange;
  final double quantity;
  final double price;
  final double profit;
  final double returnprofit;
  final int boosterMode;

  factory BoosterUserListResponse.fromJson(Map<String, dynamic> json) => BoosterUserListResponse(
    symbol: json["symbol"],
    priceChange: json["priceChange"].toDouble(),
    quantity: json["quantity"].toDouble(),
    price: json["price"].toDouble(),
    profit: json["profit"].toDouble(),
    returnprofit: json["returnprofit"].toDouble(),
    boosterMode: json["boosterMode"],
  );

  Map<String, dynamic> toJson() => {
    "symbol": symbol,
    "priceChange": priceChange,
    "quantity": quantity,
    "price": price,
    "profit": profit,
    "returnprofit": returnprofit,
    "boosterMode": boosterMode,
  };
}
