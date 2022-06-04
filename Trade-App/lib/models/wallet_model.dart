// To parse this JSON data, do
//
//     final walletModel = walletModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

WalletModel walletModelFromJson(String str) => WalletModel.fromJson(json.decode(str));

String walletModelToJson(WalletModel data) => json.encode(data.toJson());

class WalletModel {
  WalletModel({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Datum> data;

  factory WalletModel.fromJson(Map<String, dynamic> json) => WalletModel(
    status: json["status"],
    message: json["message"],
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum {
  Datum({
    required this.userId,
    required this.username,
    required this.walletAmount,
    required this.tdcWallet
  });

  final int userId;
  final String username;
  final String walletAmount;
  final String tdcWallet;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    userId: json["user_id"],
    username: json["username"],
    walletAmount: json["wallet_amount"],
    tdcWallet: json['tdc_wallet']
  );

  Map<String, dynamic> toJson() => {
    "user_id": userId,
    "username": username,
    "wallet_amount": walletAmount,
    'tdc_wallet':tdcWallet
  };
}
