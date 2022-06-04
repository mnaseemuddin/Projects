// To parse this JSON data, do
//
//     final fOrderTransationResposne = fOrderTransationResposneFromJson(jsonString);

import 'dart:convert';

List<FOrderTransationResposne> fOrderTransationResposneFromJson(String str) => List<FOrderTransationResposne>.from(json.decode(str).map((x) => FOrderTransationResposne.fromJson(x)));

String fOrderTransationResposneToJson(List<FOrderTransationResposne> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class FOrderTransationResposne {
  FOrderTransationResposne({
    required this.buyer,
    required this.symbol,
    required this.positionSide,
    required this.side,
    required this.quoteQty,
    required  this.qty,
    required this.time,
    required this.price,
    required this.commission,
    required this.commissionAsset,
    required this.realizedPnl,
  });

  String buyer;
  String symbol;
  String positionSide;
  String side;
  double quoteQty;
  int qty;
  String time;
  double price;
  double commission;
  String commissionAsset;
  int realizedPnl;

  factory FOrderTransationResposne.fromJson(Map<String, dynamic> json) => FOrderTransationResposne(
    buyer: json["buyer"],
    symbol: json["symbol"],
    positionSide: json["positionSide"],
    side: json["side"],
    quoteQty: json["quoteQty"].toDouble(),
    qty: json["qty"],
    time: json["time"],
    price: json["price"].toDouble(),
    commission: json["commission"].toDouble(),
    commissionAsset: json["commissionAsset"],
    realizedPnl: json["realizedPnl"],
  );

  Map<String, dynamic> toJson() => {
    "buyer": buyer,
    "symbol": symbol,
    "positionSide": positionSide,
    "side": side,
    "quoteQty": quoteQty,
    "qty": qty,
    "time": time,
    "price": price,
    "commission": commission,
    "commissionAsset": commissionAsset,
    "realizedPnl": realizedPnl,
  };
}
