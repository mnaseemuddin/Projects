// To parse this JSON data, do
//
//     final getSellModeResponse = getSellModeResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

GetSellModeResponse getSellModeResponseFromJson(String str) => GetSellModeResponse.fromJson(json.decode(str));

String getSellModeResponseToJson(GetSellModeResponse data) => json.encode(data.toJson());

class GetSellModeResponse {
  GetSellModeResponse({
    required this.id,
    required this.positionamt,
    required this.avgprice,
    required this.positionquantity,
    required this.currentprice,
    required this.positionprofitloss,
    required this.remainpositionamt,
    required this.status,
  });

  final int id;
  final double positionamt;
  final double avgprice;
  final double positionquantity;
  final double currentprice;
  final double positionprofitloss;
  final double remainpositionamt;
  final String status;

  factory GetSellModeResponse.fromJson(Map<String, dynamic> json) => GetSellModeResponse(
    id: json["id"],
    positionamt: json["positionamt"].toDouble()??0.0,
    avgprice: json["avgprice"].toDouble()??0.0,
    positionquantity: json["positionquantity"].toDouble()??0.0,
    currentprice: json["currentprice"].toDouble()??0.0,
    positionprofitloss: json["positionprofitloss"].toDouble()??0.0,
    remainpositionamt: json["remainpositionamt"].toDouble()??0.0,
    status: json["status"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "positionamt": positionamt,
    "avgprice": avgprice,
    "positionquantity": positionquantity,
    "currentprice": currentprice,
    "positionprofitloss": positionprofitloss,
    "remainpositionamt": remainpositionamt,
    "status": status,
  };
}
