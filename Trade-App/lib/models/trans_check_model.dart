// To parse this JSON data, do
//
//     final transactionCheckModel = transactionCheckModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

TransactionCheckModel transactionCheckModelFromJson(String str) => TransactionCheckModel.fromJson(json.decode(str));

String transactionCheckModelToJson(TransactionCheckModel data) => json.encode(data.toJson());

class TransactionCheckModel {
  TransactionCheckModel({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Datum> data;

  factory TransactionCheckModel.fromJson(Map<String, dynamic> json) => TransactionCheckModel(
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

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
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
