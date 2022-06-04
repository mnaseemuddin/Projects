import 'dart:convert';

GraphModel graphModelFromJson(String str) => GraphModel.fromJson(json.decode(str));

String graphModelToJson(GraphModel data) => json.encode(data.toJson());

class GraphModel {
  GraphModel({
    required this.at,
    required this.ticker,
  });

  int at;
  final Ticker ticker;

  factory GraphModel.fromJson(Map<String, dynamic> json) => GraphModel(
    at: json["at"],
    ticker: Ticker.fromJson(json["ticker"]),
  );

  Map<String, dynamic> toJson() => {
    "at": at,
    "ticker": ticker.toJson(),
  };
}

class Ticker {
  Ticker({
    required this.buy,
    required this.sell,
    required this.low,
    required this.high,
    required this.last,
    required this.vol,
  });

  final double buy;
  final double sell;
  final double low;
  final double high;
   double last;
  final double vol;

  factory Ticker.fromJson(Map<String, dynamic> json) => Ticker(
    buy: double.parse('${json["buy"]}'),
    sell: double.parse('${json["sell"]}'),
    low: double.parse('${json["low"]}'),
    high: double.parse('${json["high"]}'),
    last: double.parse('${json["last"]}'),
    vol: double.parse('${json["vol"]}'),
  );

  Map<String, dynamic> toJson() => {
    "buy": buy,
    "sell": sell,
    "low": low,
    "high": high,
    "last": last,
    "vol": vol,
  };
}
