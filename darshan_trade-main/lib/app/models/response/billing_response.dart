// To parse this JSON data, do
//
//     final billingResponse = billingResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<BillingResponse> billingResponseFromJson(String str) => List<BillingResponse>.from(json.decode(str).map((x) => BillingResponse.fromJson(x)));

String billingResponseToJson(List<BillingResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class BillingResponse {
  BillingResponse({
    required this.profit,
    required this.platform,
    required this.symbol,
    required this.transDate
  });

  final double profit;
  final String platform;
  final String symbol;
  final String transDate;

  factory BillingResponse.fromJson(Map<String, dynamic> json) => BillingResponse(

    profit: json["profit"] is double?json["profit"]:double.parse(json["profit"]),
    platform: json["platform"],
    symbol: json["symbol"],
    transDate: json["transdate"]

  );

  Map<String, dynamic> toJson() => {
    "profit": profit,
    "platform": platform,
    "symbol": symbol,
    "transdate":transDate
  };
}
