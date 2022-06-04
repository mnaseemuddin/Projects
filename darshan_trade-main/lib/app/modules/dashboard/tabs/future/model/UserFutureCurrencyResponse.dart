// To parse this JSON data, do
//
//     final userFutureCurrencyResponse = userFutureCurrencyResponseFromJson(jsonString);

import 'dart:convert';

List<UserFutureCurrencyResponse> userFutureCurrencyResponseFromJson(String str) => List<UserFutureCurrencyResponse>.from(json.decode(str).map((x) => UserFutureCurrencyResponse.fromJson(x)));

String userFutureCurrencyResponseToJson(List<UserFutureCurrencyResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class UserFutureCurrencyResponse {
  UserFutureCurrencyResponse({
    required this.symbol,
    required this.priceChange,
    required this.quantity,
    required this.price,
    required this.profit,
    required this.returnprofit,
    required this.cyclemode,
  });

  String symbol;
  var priceChange;
  var quantity;
  var price;
  var profit;
  var returnprofit;
  int cyclemode;

  factory UserFutureCurrencyResponse.fromJson(Map<String, dynamic> json) => UserFutureCurrencyResponse(
    symbol: json["symbol"],
    priceChange: json["priceChange"],
    quantity: json["quantity"],
    price: json["price"],
    profit: json["profit"],
    returnprofit: json["returnprofit"],
    cyclemode: json["cyclemode"],
  );

  Map<String, dynamic> toJson() => {
    "symbol": symbol,
    "priceChange": priceChange,
    "quantity": quantity,
    "price": price,
    "profit": profit,
    "returnprofit": returnprofit,
    "cyclemode": cyclemode,
  };
}
