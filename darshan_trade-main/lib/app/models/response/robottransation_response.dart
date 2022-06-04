// To parse this JSON data, do
//
//     final robotTransationModel = robotTransationModelFromJson(jsonString);

import 'dart:convert';

List<RobotTransationModel> robotTransationModelFromJson(String str) => List<RobotTransationModel>.from(json.decode(str).map((x) => RobotTransationModel.fromJson(x)));

String robotTransationModelToJson(List<RobotTransationModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class RobotTransationModel {
  RobotTransationModel({
    required this.id,
    required this.userid,
    required this.symbol,
    required this.side,
    required this.tradeAmount,
    required this.qunatity,
    required this.platform,
    required this.orderdate,
  });

  int id;
  int userid;
  String symbol;
  String side;
  double tradeAmount;
  double qunatity;
  String platform;
  String orderdate;

  factory RobotTransationModel.fromJson(Map<String, dynamic> json) => RobotTransationModel(
    id: json["id"],
    userid: json["userid"],
    symbol: json["symbol"],
    side: json["side"],
    tradeAmount: json["trade_amount"].toDouble(),
    qunatity: json["qunatity"].toDouble(),
    platform: json["platform"],
    orderdate: json["orderdate"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "userid": userid,
    "symbol": symbol,
    "side": side,
    "trade_amount": tradeAmount,
    "qunatity": qunatity,
    "platform": platform,
    "orderdate": orderdate,
  };
}
