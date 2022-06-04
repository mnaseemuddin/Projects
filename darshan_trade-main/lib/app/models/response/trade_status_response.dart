// To parse this JSON data, do
//
//     final tradeStatusResponse = tradeStatusResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

TradeStatusResponse tradeStatusResponseFromJson(String str) => TradeStatusResponse.fromJson(json.decode(str));

String tradeStatusResponseToJson(TradeStatusResponse data) => json.encode(data.toJson());

class TradeStatusResponse {
  TradeStatusResponse({
    required this.sell,
    required this.buy,
    required this.cycleShotMode,
    required this.strategyMode,
    required this.marginLimit,
    required this.trademode,
    required this.status,
  });

  final int sell;
  final int buy;
  final int cycleShotMode;
  final int strategyMode;
  final int marginLimit;
  final int trademode;
  final String status;

  factory TradeStatusResponse.fromJson(Map<String, dynamic> json) => TradeStatusResponse(
    sell: json["sell"],
    buy: json["buy"],
    cycleShotMode: json["cycleShotMode"],
    strategyMode: json["strategyMode"],
    marginLimit: json["marginLimit"],
    trademode: json["trademode"]??0,
    status: json["status"],
  );

  Map<String, dynamic> toJson() => {
    "sell": sell,
    "buy": buy,
    "cycleShotMode": cycleShotMode,
    "strategyMode": strategyMode,
    "marginLimit": marginLimit,
    "trademode": trademode,
    "status": status,
  };
}
