// To parse this JSON data, do
//
//     final rankWiseResponse = rankWiseResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<RankWiseResponse> rankWiseResponseFromJson(String str) => List<RankWiseResponse>.from(json.decode(str).map((x) => RankWiseResponse.fromJson(x)));

String rankWiseResponseToJson(List<RankWiseResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class RankWiseResponse {
  RankWiseResponse({
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

  factory RankWiseResponse.fromJson(Map<String, dynamic> json) => RankWiseResponse(
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
