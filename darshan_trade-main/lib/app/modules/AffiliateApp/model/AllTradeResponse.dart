// To parse this JSON data, do
//
//     final affiliateAllOrderTransacionResponse = affiliateAllOrderTransacionResponseFromJson(jsonString);

import 'dart:convert';

List<AffiliateAllOrderTransacionResponse> affiliateAllOrderTransacionResponseFromJson(String str) => List<AffiliateAllOrderTransacionResponse>.from(json.decode(str).map((x) => AffiliateAllOrderTransacionResponse.fromJson(x)));

String affiliateAllOrderTransacionResponseToJson(List<AffiliateAllOrderTransacionResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class AffiliateAllOrderTransacionResponse {
  AffiliateAllOrderTransacionResponse({
    required this.userid,
    required this.symbol,
    required this.side,
    required this.tradeAmount,
    required this.transactTime,
    required this.platform,
    required this.qunatity,
    required this.price,
    required this.commission,
    required this.commissionAsset,
    required this.tradeStatus,
  });

  int userid;
  String symbol;
  String side;
  double tradeAmount;
  String transactTime;
  String platform;
  double qunatity;
  double price;
  double commission;
  String commissionAsset;
  String tradeStatus;

  factory AffiliateAllOrderTransacionResponse.fromJson(Map<String, dynamic> json) => AffiliateAllOrderTransacionResponse(
    userid: json["userid"],
    symbol: json["symbol"],
    side: json["side"],
    tradeAmount: json["trade_amount"].toDouble(),
    transactTime: json["transactTime"],
    platform: json["platform"],
    qunatity: json["qunatity"].toDouble(),
    price: json["price"].toDouble(),
    commission: json["commission"].toDouble(),
    commissionAsset: json["commissionAsset"],
    tradeStatus: json["tradeStatus"],
  );

  Map<String, dynamic> toJson() => {
    "userid": userid,
    "symbol": symbol,
    "side": side,
    "trade_amount": tradeAmount,
    "transactTime": transactTime,
    "platform": platform,
    "qunatity": qunatity,
    "price": price,
    "commission": commission,
    "commissionAsset": commissionAsset,
    "tradeStatus": tradeStatus,
  };
}
