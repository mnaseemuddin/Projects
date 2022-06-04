// To parse this JSON data, do
//
//     final tdcCurrencyModel = tdcCurrencyModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

TdcCurrencyModel tdcCurrencyModelFromJson(String str) => TdcCurrencyModel.fromJson(json.decode(str));

String tdcCurrencyModelToJson(TdcCurrencyModel data) => json.encode(data.toJson());

class TdcCurrencyModel {
  TdcCurrencyModel({
    required this.status,
    required this.data,
    required this.message,
  });

  final String status;
  final List<Data> data;
  final String message;

  factory TdcCurrencyModel.fromJson(Map<String, dynamic> json) => TdcCurrencyModel(
    status: json["status"],
    data: List<Data>.from(json["Data"].map((x) => Data.fromJson(x))),
    message: json["Message"],
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "Data": List<dynamic>.from(data.map((x) => x.toJson())),
    "Message": message,
  };
}

class Data {
  Data({
    required this.name,
    required this.symbol,
    required this.rateDollar,
    required this.rateBnb,
  });

  final String name;
  final String symbol;
  final String rateDollar;
  final String rateBnb;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
    name: json["Name"],
    symbol: json["Symbol"],
    rateDollar: json["RateDollar"],
    rateBnb: json["RateBnb"],
  );

  Map<String, dynamic> toJson() => {
    "Name": name,
    "Symbol": symbol,
    "RateDollar": rateDollar,
    "RateBnb": rateBnb,
  };
}
