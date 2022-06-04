// To parse this JSON data, do
//
//     final communityListResponse = communityListResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<CommunityListResponse> communityListResponseFromJson(String str) => List<CommunityListResponse>.from(json.decode(str).map((x) => CommunityListResponse.fromJson(x)));

String communityListResponseToJson(List<CommunityListResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class CommunityListResponse {
  CommunityListResponse({
    required this.userid,
    required this.mobileNo,
    required this.level,
    required this.doj,
    required this.tradeProfit,
    required this.teamearning,
  });

  final String userid;
  final String mobileNo;
  final String level;
  final String doj;
  final double tradeProfit;
  final double teamearning;

  factory CommunityListResponse.fromJson(Map<String, dynamic> json) => CommunityListResponse(
    userid: json["userid"],
    mobileNo: json["mobileNo"],
    level: json["level"],
    doj: json["doj"],
    tradeProfit: json["trade_profit"],
    teamearning: json["teamearning"],
  );

  Map<String, dynamic> toJson() => {
    "userid": userid,
    "mobileNo": mobileNo,
    "level": level,
    "doj": doj,
    "trade_profit": tradeProfit,
    "teamearning": teamearning,
  };
}
