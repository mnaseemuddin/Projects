// To parse this JSON data, do
//
//     final totalprofitResponse = totalprofitResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<TotalprofitResponse> totalprofitResponseFromJson(String str) => List<TotalprofitResponse>.from(json.decode(str).map((x) => TotalprofitResponse.fromJson(x)));

String totalprofitResponseToJson(List<TotalprofitResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class TotalprofitResponse {
  TotalprofitResponse({
    required this.transdate,
    required this.profit,
  });

  final String transdate;
  final double profit;

  factory TotalprofitResponse.fromJson(Map<String, dynamic> json) => TotalprofitResponse(
    transdate: json["transdate"],
    profit: json["profit"] is double?json["profit"]:double.parse(json["profit"]),
  );

  Map<String, dynamic> toJson() => {
    "transdate": transdate,
    "profit": profit,
  };
}
