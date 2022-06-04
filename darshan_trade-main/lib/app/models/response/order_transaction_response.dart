// To parse this JSON data, do
//
//     final orderTransactionResponse = orderTransactionResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<OrderTransactionResponse> orderTransactionResponseFromJson(String str) => List<OrderTransactionResponse>.from(json.decode(str).map((x) => OrderTransactionResponse.fromJson(x)));

String orderTransactionResponseToJson(List<OrderTransactionResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class OrderTransactionResponse {
  OrderTransactionResponse({
    required this.id,
    required this.userid,
    required this.symbol,
    required this.side,
    required this.trade_amount,
    required this.transactTime,
    required this.qunatity,
    required this.platform,
    required this.price,
    required this.tradeStatus,
    required this.commission,
    required this.commissionAsset
  });

  final int id;
  final int userid;
  final String symbol;
  final String side;
  final double trade_amount;
  final String transactTime;
  final double qunatity;
  final String platform;
  final double price;
  final String tradeStatus;
  final double commission;
  final String commissionAsset;




  factory OrderTransactionResponse.fromJson(Map<String, dynamic> json) => OrderTransactionResponse(
    id: json["id"],
    userid: json["userid"]??1,
    symbol: json["symbol"],
    side: json["side"]??'Buy',
    trade_amount: double.parse('${json["trade_amount"]}') ,//==null?0.0:json["trade_amount"] is double?json["trade_amount"]:double.parse('${json["trade_amount"]}'),
    transactTime: '${json["transactTime"]}'??'',
    qunatity: json["qunatity"]??0.0,
    platform: json["platform"]??'Binance',
    price: json["price"]??0.0,
    tradeStatus: json['tradeStatus'],
    commission: json["commission"],
    commissionAsset: json["commissionAsset"]
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "userid": userid,
    "symbol": symbol,
    "side": side,
    "trade_amount": trade_amount,
    "transactTime": transactTime,
    "qunatity": qunatity,
    "platform": platform,
    "price": price,
    "tradeStatus":tradeStatus,
    "commission":commission,
    "commissionAsset":commissionAsset
  };
}


// // To parse this JSON data, do
// //
// //     final orderTransactionResponse = orderTransactionResponseFromJson(jsonString);
//
// import 'package:meta/meta.dart';
// import 'dart:convert';
//
// List<OrderTransactionResponse> orderTransactionResponseFromJson(String str) => List<OrderTransactionResponse>.from(json.decode(str).map((x) => OrderTransactionResponse.fromJson(x)));
//
// String orderTransactionResponseToJson(List<OrderTransactionResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));
//
// class OrderTransactionResponse {
//   OrderTransactionResponse({
//     required this.id,
//     required this.userid,
//     required this.symbol,
//     required this.side,
//     required this.tradeAmount,
//     required this.transactTime,
//   });
//
//   final int id;
//   final int userid;
//   final String symbol;
//   final String side;
//   final double tradeAmount;
//   final int transactTime;
//
//   factory OrderTransactionResponse.fromJson(Map<String, dynamic> json) => OrderTransactionResponse(
//     id: json["id"],
//     userid: json["userid"],
//     symbol: json["symbol"],
//     side: json["side"],
//     tradeAmount: json["trade_amount"] is double?json["trade_amount"]:double.parse('${json["trade_amount"]}'),
//     transactTime: json["transactTime"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "id": id,
//     "userid": userid,
//     "symbol": symbol,
//     "side": side,
//     "trade_amount": tradeAmount,
//     "transactTime": transactTime,
//   };
// }
