// To parse this JSON data, do
//
//     final curOneChngListResponse = curOneChngListResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

CurOneChngListResponse curOneChngListResponseFromJson(String str) => CurOneChngListResponse.fromJson(json.decode(str));

String curOneChngListResponseToJson(CurOneChngListResponse data) => json.encode(data.toJson());

class CurOneChngListResponse {
  CurOneChngListResponse({
    required this.pair,
    required this.positionamt,
    required this.avgprice,
    required this.margincall,
    required this.positionquantity,
    required this.currentprice,
    required this.returnrate,
  });

  final String pair;
  final double positionamt;
  final double avgprice;
  final double margincall;
  final double positionquantity;
  final double currentprice;
  final double returnrate;

  factory CurOneChngListResponse.fromJson(Map<String, dynamic> json) => CurOneChngListResponse(
    pair: json["pair"]??'',
    positionamt: json["positionamt"].toDouble()??0.0,
    avgprice: json["avgprice"].toDouble()??0.0,
    margincall: json["margincall"].toDouble()??0.0,
    positionquantity: json["positionquantity"].toDouble()??0.0,
    currentprice: json["currentprice"].toDouble(),
    returnrate: json["returnrate"].toDouble(),
  );

  Map<String, dynamic> toJson() => {
    "pair": pair,
    "positionamt": positionamt,
    "avgprice": avgprice,
    "margincall": margincall,
    "positionquantity": positionquantity,
    "currentprice": currentprice,
    "returnrate": returnrate,
  };
}
