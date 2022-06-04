// To parse this JSON data, do
//
//     final getcurrencyResponse = getcurrencyResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<GetcurrencyResponse> getcurrencyResponseFromJson(String str) => List<GetcurrencyResponse>.from(json.decode(str).map((x) => GetcurrencyResponse.fromJson(x)));

String getcurrencyResponseToJson(List<GetcurrencyResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class GetcurrencyResponse {
  GetcurrencyResponse({
    required this.id,
    required this.currency,
    required this.price,
    required this.address,
    required this.minimumdeposit,
  });

  final String id;
  final String currency;
  final double price;
  final String address;
  final double minimumdeposit;

  factory GetcurrencyResponse.fromJson(Map<String, dynamic> json) => GetcurrencyResponse(
    id: json["id"],
    currency: json["currency"],
    price: json["price"] ==null? 1.0:json["price"] is double?json["price"]:double.parse(json["price"],),
    address: json["address"]??'',
    minimumdeposit: json["minimumdeposit"] == null?0.0:json["minimumdeposit"] is double?json["minimumdeposit"]:double.parse(json["minimumdeposit"]),
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "currency": currency,
    "price": price,
    "address": address,
    "minimumdeposit": minimumdeposit,
  };
}
