// To parse this JSON data, do
//
//     final revenueResponse = revenueResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<RevenueResponse> revenueResponseFromJson(String str) => List<RevenueResponse>.from(json.decode(str).map((x) => RevenueResponse.fromJson(x)));

String revenueResponseToJson(List<RevenueResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class RevenueResponse {
  RevenueResponse({
    required this.transdate,
    required this.credit,
    required this.remark,
  });

  final String transdate;
  final double credit;
  final String remark;

  factory RevenueResponse.fromJson(Map<String, dynamic> json) => RevenueResponse(
    transdate: json["transdate"]??'',
    credit:    json["credit"] is double?json["credit"]??0.0:double.parse(json["credit"]??'0.0'),
    remark:    json["remark"]??'',
  );

  Map<String, dynamic> toJson() => {
    "transdate": transdate,
    "credit": credit,
    "remark": remark,
  };
}
