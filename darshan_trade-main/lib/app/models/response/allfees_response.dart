// To parse this JSON data, do
//
//     final allFeesResponse = allFeesResponseFromJson(jsonString);

import 'dart:convert';

List<AllFeesResponse> allFeesResponseFromJson(String str) => List<AllFeesResponse>.from(json.decode(str).map((x) => AllFeesResponse.fromJson(x)));

String allFeesResponseToJson(List<AllFeesResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class AllFeesResponse {
  AllFeesResponse({
    required this.commission,
    required this.symbol,
    required this.platform,
    required this.commissionAsset

  });

  double commission;
  String symbol;
  String platform;
  String commissionAsset;

  factory AllFeesResponse.fromJson(Map<String, dynamic> json) => AllFeesResponse(
    commission: json["commission"].toDouble(),
    symbol: json["symbol"],
    platform: json["platform"],
    commissionAsset: json["commissionAsset"]
  );

  Map<String, dynamic> toJson() => {
    "commission": commission,
    "symbol": symbol,
    "platform": platform,
    "commissionAsset":commissionAsset
  };
}
