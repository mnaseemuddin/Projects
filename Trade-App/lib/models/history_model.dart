// To parse this JSON data, do
//
//     final historyModel = historyModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

HistoryModel historyModelFromJson(String str) => HistoryModel.fromJson(json.decode(str));

String historyModelToJson(HistoryModel data) => json.encode(data.toJson());

class HistoryModel {
  HistoryModel({
    required this.status,
    required this.message,
    required this.totalprice,
    required this.data,
  });

  final String status;
  final String message;
  final int totalprice;
  final List<Histories> data;

  factory HistoryModel.fromJson(Map<String, dynamic> json) => HistoryModel(
    status: json["status"],
    message: json["message"],
    totalprice: json["totalprice"],
    data: List<Histories>.from(json["data"].map((x) => Histories.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "totalprice": totalprice,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Histories {
  Histories({
    required this.tblUserBetId,
    required this.betType,
    required this.betTime,
    required this.betPrice,
    required this.currencyValue,
    required this.currencyRate,
    required this.betResult,
    required this.winningAmount,
  });

  final int tblUserBetId;
  final String betType;
  final String betTime;
  final String betPrice;
  final String currencyValue;
  final String currencyRate;
  final String betResult;
  final String winningAmount;

  factory Histories.fromJson(Map<String, dynamic> json) => Histories(
    tblUserBetId: json["tbl_user_bet_id"],
    betType: json["bet_type"],
    betTime: json["bet_time"],
    betPrice: json["bet_price"],
    currencyValue: json["currency_value"],
    currencyRate: json["currency_rate"],
    betResult: json["bet_result"],
    winningAmount: json["winning_amount"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_user_bet_id": tblUserBetId,
    "bet_type": betType,
    "bet_time": betTime,
    "bet_price": betPrice,
    "currency_value": currencyValue,
    "currency_rate": currencyRate,
    "bet_result": betResult,
    "winning_amount": winningAmount,
  };
}
