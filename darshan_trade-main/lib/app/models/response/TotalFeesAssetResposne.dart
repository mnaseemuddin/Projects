// To parse this JSON data, do
//
//     final totoalFeesResponse = totoalFeesResponseFromJson(jsonString);

import 'dart:convert';

List<TotoalFeesResponse> totoalFeesResponseFromJson(String str) => List<TotoalFeesResponse>.from(json.decode(str).map((x) => TotoalFeesResponse.fromJson(x)));

String totoalFeesResponseToJson(List<TotoalFeesResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class TotoalFeesResponse {
  TotoalFeesResponse({
    required this.commission,
    required this.commissionAsset,
    required this.platform,
  });

  double commission;
  String commissionAsset;
  String platform;

  factory TotoalFeesResponse.fromJson(Map<String, dynamic> json) => TotoalFeesResponse(
    commission: json["commission"].toDouble(),
    commissionAsset: json["commissionAsset"],
    platform: json["platform"],
  );

  Map<String, dynamic> toJson() => {
    "commission": commission,
    "commissionAsset": commissionAsset,
    "platform": platform,
  };
}
