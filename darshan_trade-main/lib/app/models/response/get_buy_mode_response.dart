// To parse this JSON data, do
//
//     final getBuyModeResponse = getBuyModeResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

GetBuyModeResponse getBuyModeResponseFromJson(String str) => GetBuyModeResponse.fromJson(json.decode(str));

String getBuyModeResponseToJson(GetBuyModeResponse data) => json.encode(data.toJson());

class GetBuyModeResponse {
  GetBuyModeResponse({
    required this.id,
    required this.positionamt,
    required this.avgprice,
    required this.positionquantity,
    required this.currentprice,
    required this.positionprofitloss,
    required this.estimatedavgprice,
    required this.estimatedholdprofitloss,
    required this.status,
  });

  final int id;
  final double positionamt;
  final double avgprice;
  final double positionquantity;
  final double currentprice;
  final double positionprofitloss;
  final double estimatedavgprice;
  final double estimatedholdprofitloss;
  final String status;

  factory GetBuyModeResponse.fromJson(Map<String, dynamic> json) => GetBuyModeResponse(
    id: json["id"],
    positionamt: json["positionamt"].toDouble()??0.0,
    avgprice: json["avgprice"].toDouble()??0.0,
    positionquantity: json["positionquantity"].toDouble()??0.0,
    currentprice: json["currentprice"].toDouble()??0.0,
    positionprofitloss: json["positionprofitloss"].toDouble()??0.0,
    estimatedavgprice: json["estimatedavgprice"].toDouble()??0.0,
    estimatedholdprofitloss: json["estimatedholdprofitloss"].toDouble()??0.0,
    status: json["status"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "positionamt": positionamt,
    "avgprice": avgprice,
    "positionquantity": positionquantity,
    "currentprice": currentprice,
    "positionprofitloss": positionprofitloss,
    "estimatedavgprice": estimatedavgprice,
    "estimatedholdprofitloss": estimatedholdprofitloss,
    "status": status,
  };
}
