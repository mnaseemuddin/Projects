// To parse this JSON data, do
//
//     final totalBillingResponse = totalBillingResponseFromJson(jsonString);

import 'dart:convert';

List<TotalBillingResponse> totalBillingResponseFromJson(String str) => List<TotalBillingResponse>.from(json.decode(str).map((x) => TotalBillingResponse.fromJson(x)));

String totalBillingResponseToJson(List<TotalBillingResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class TotalBillingResponse {
  TotalBillingResponse({
    required this.profit,
    required this.platform,
    required this.symbol,
    required this.transdate,
  });

  String profit;
  String platform;
  String symbol;
  String transdate;

  factory TotalBillingResponse.fromJson(Map<String, dynamic> json) => TotalBillingResponse(
    profit: json["profit"],
    platform: json["platform"],
    symbol: json["symbol"],
    transdate: json["transdate"],
  );

  Map<String, dynamic> toJson() => {
    "profit": profit,
    "platform": platform,
    "symbol": symbol,
    "transdate": transdate,
  };
}
