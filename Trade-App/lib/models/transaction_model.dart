// To parse this JSON data, do
//
//     final transactionModel = transactionModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

TransactionModel transactionModelFromJson(String str) => TransactionModel.fromJson(json.decode(str));

String transactionModelToJson(TransactionModel data) => json.encode(data.toJson());

class TransactionModel {
  TransactionModel({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Trans> data;

  factory TransactionModel.fromJson(Map<String, dynamic> json) => TransactionModel(
    status: json["status"],
    message: json["message"],
    data: List<Trans>.from(json["data"].map((x) => Trans.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Trans {
  Trans({
    required this.tblAddAmountId,
    required this.amount,
    required this.fiatAmount,
    required this.txnId,
    required this.coin,
    required this.confirms,
    required this.address,
    required this.addedTime,
  });

  final int tblAddAmountId;
  final String amount;
  final String fiatAmount;
  final String txnId;
  final String coin;
  final String confirms;
  final String address;
  final String addedTime;

  factory Trans.fromJson(Map<String, dynamic> json) => Trans(
    tblAddAmountId: json["tbl_add_amount_id"],
    amount: json["amount"],
    fiatAmount: json["fiat_amount"],
    txnId: json["txn_id"],
    coin: json["coin"],
    confirms: json["confirms"],
    address: json["address"],
    addedTime: json["added_time"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_add_amount_id": tblAddAmountId,
    "amount": amount,
    "fiat_amount": fiatAmount,
    "txn_id": txnId,
    "coin": coin,
    "confirms": confirms,
    "address": address,
    "added_time": addedTime,
  };
}
