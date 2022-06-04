// To parse this JSON data, do
//
//     final tradeResponse = tradeResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

import 'package:royal_q/app/data/user_data.dart';

TradeResponse tradeResponseFromJson(String str) => TradeResponse.fromJson(json.decode(str));

String tradeResponseToJson(TradeResponse data) => json.encode(data.toJson());

class TradeResponse {
  TradeResponse({
    required this.id,
    required this.firstBuy,
    required this.openPosition,
    required this.maginCallLimit,
    required this.wholePRatio,
    required this.wholePCallback,
    required this.buyCallback,
   // required this.subPCallback,
    required this.presetBuyprice,
    required this.stopCloseprice,
    required this.selllosspositin,
    required this.lossCallBackTrigger,
    required this.status,
    required this.marginList,
    required this.symbol,
    required this.exchangeType,
    required this.excessUpTrigger

  });

   int id;
   double firstBuy;
   int openPosition;
   int maginCallLimit;
   double wholePRatio;
   double wholePCallback;
   double buyCallback;
  // double subPCallback;
   double presetBuyprice;
   double stopCloseprice;
   double selllosspositin;
   double lossCallBackTrigger;
   String status;
   String symbol;
   int exchangeType;
   double excessUpTrigger;
   List<MarginListItem> marginList;

  factory TradeResponse.fromJson(Map<String, dynamic> json) => TradeResponse(
    id: json["id"],
    firstBuy: json["firstBuy"],
    openPosition: json["openPosition"],
    maginCallLimit: json["maginCallLimit"],
    wholePRatio: json["wholePRatio"].toDouble(),
    wholePCallback: json["wholePCallback"].toDouble(),
    buyCallback: json["buyCallback"].toDouble(),
   // subPCallback: json["subPCallback"].toDouble(),
    presetBuyprice: json["presetBuyprice"].toDouble(),
    stopCloseprice: json["stopCloseprice"].toDouble(),
    selllosspositin: json["selllosspositin"].toDouble(),
    lossCallBackTrigger: json["losscallback"].toDouble(),
    status: json["status"],
    symbol: json["symbol"]??'',
    exchangeType: json["exchangeType"]??exchangeValue,
    excessUpTrigger: json['excessuptrigger'].toDouble(),
    marginList: List<MarginListItem>.from(json["marginList"].map((x) => MarginListItem.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "firstBuy": firstBuy,
    "openPosition": openPosition,
    "maginCallLimit": maginCallLimit,
    "wholePRatio": wholePRatio,
    "wholePCallback": wholePCallback,
    "buyCallback": buyCallback,
   // "subPCallback": subPCallback,
    "presetBuyprice": presetBuyprice,
    "stopCloseprice": stopCloseprice,
    "selllosspositin":selllosspositin,
    "losscallback":lossCallBackTrigger,
    "status": status,
    "symbol": symbol,
    "ExchangeType": exchangeValue,
    "excessuptrigger":excessUpTrigger,
    "marginList": List<dynamic>.from(marginList.map((x) => x.toJson())),
  };
}

class MarginListItem {
  MarginListItem({
    required this.id,
    required this.calls,
    required this.times,
    required this.iscall,
  });

  final int id;
        double calls;
        int times;
        int iscall;

  factory MarginListItem.fromJson(Map<String, dynamic> json) => MarginListItem(
    id: json["id"],
    calls: json["calls"],
    times: json["times"],
    iscall: json["iscall"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "calls": calls,
    "times": times,
    "iscall": iscall,
  };
}




// import 'dart:convert';
//
// TradeResponse tradeResponseFromJson(String str) => TradeResponse.fromJson(json.decode(str));
//
// String tradeResponseToJson(TradeResponse data) => json.encode(data.toJson());
//
// class TradeResponse {
//   TradeResponse({
//     required this.marginList,
//     required this.id,
//     required this.firstBuy,
//     required this.openPosition,
//     required this.maginCallLimit,
//     required this.wholePRatio,
//     required this.wholePCallback,
//     required this.buyCallback,
//     required this.subPCallback,
//     required this.status,
//     required this.message,
//   });
//
//   final List<MarginList> marginList;
//    int id;
//    double firstBuy;
//    int openPosition;
//    int maginCallLimit;
//    double wholePRatio;
//    double wholePCallback;
//    double buyCallback;
//    double subPCallback;
//   final String status;
//   final String message;
//
//   factory TradeResponse.fromJson(Map<String, dynamic> json) => TradeResponse(
//     marginList: List<MarginList>.from(json["marginList"].map((x) => MarginList.fromJson(x))),
//     id: json["id"],
//     firstBuy: json["firstBuy"],
//     openPosition: json["openPosition"],
//     maginCallLimit: json["maginCallLimit"],
//     wholePRatio: json["wholePRatio"].toDouble(),
//     wholePCallback: json["wholePCallback"].toDouble(),
//     buyCallback: json["buyCallback"].toDouble(),
//     subPCallback: json["subPCallback"].toDouble(),
//     status: json["status"],
//     message: json["message"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "marginList": List<dynamic>.from(marginList.map((x) => x.toJson())),
//     "id": id,
//     "firstBuy": firstBuy,
//     "openPosition": openPosition,
//     "maginCallLimit": maginCallLimit,
//     "wholePRatio": wholePRatio,
//     "wholePCallback": wholePCallback,
//     "buyCallback": buyCallback,
//     "subPCallback": subPCallback,
//     "status": status,
//     "message": message,
//   };
// }
//
// class MarginList {
//   MarginList({
//     required this.id,
//     required this.calls,
//     required this.times,
//   });
//
//   final int id;
//   final double calls;
//   final int times;
//
//   factory MarginList.fromJson(Map<String, dynamic> json) => MarginList(
//     id: json["id"],
//     calls: json["calls"],
//     times: json["times"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "id": id,
//     "calls": calls,
//     "times": times,
//   };
// }
