// To parse this JSON data, do
//
//     final boosterCurResponse = boosterCurResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

BoosterCurResponse boosterCurResponseFromJson(String str) => BoosterCurResponse.fromJson(json.decode(str));

String boosterCurResponseToJson(BoosterCurResponse data) => json.encode(data.toJson());

class BoosterCurResponse {
  BoosterCurResponse({
    required this.symbol,
    required this.price,
    required this.boosterMode,
    required this.qunatity,
  });

  final String symbol;
  final String price;
  final int boosterMode;
  final String qunatity;

  factory BoosterCurResponse.fromJson(Map<String, dynamic> json) => BoosterCurResponse(
    symbol: json["symbol"],
    price: json["price"],
    boosterMode: json["boosterMode"],
    qunatity: json["qunatity"],
  );

  Map<String, dynamic> toJson() => {
    "symbol": symbol,
    "price": price,
    "boosterMode": boosterMode,
    "qunatity": qunatity,
  };
}
