// To parse this JSON data, do
//
//     final tradingProfitResponse = tradingProfitResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<TradingProfitResponse> tradingProfitResponseFromJson(String str) => List<TradingProfitResponse>.from(json.decode(str).map((x) => TradingProfitResponse.fromJson(x)));

String tradingProfitResponseToJson(List<TradingProfitResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class TradingProfitResponse {
  TradingProfitResponse({
    required this.id,
    required this.username,
    required this.name,
    required this.countryname,
    required this.invitaionCode,
  });

  final int id;
  final String username;
  final String name;
  final String countryname;
  final String invitaionCode;

  factory TradingProfitResponse.fromJson(Map<String, dynamic> json) => TradingProfitResponse(
    id: json["id"],
    username: json["username"]??'',
    name: json["name"]??'',
    countryname: json["countryname"]??'',
    invitaionCode: json["invitaionCode"]??'',
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "username": username,
    "name": name,
    "countryname": countryname,
    "invitaionCode": invitaionCode,
  };
}
