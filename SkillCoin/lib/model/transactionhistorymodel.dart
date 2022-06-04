// To parse this JSON data, do
//
//     final transactionModel = transactionModelFromJson(jsonString);

import 'dart:convert';

TransactionHistoryModel transactionModelFromJson(String str) => TransactionHistoryModel.fromJson(json.decode(str));

String transactionModelToJson(TransactionHistoryModel data) => json.encode(data.toJson());

class TransactionHistoryModel {
  TransactionHistoryModel({
    required this.status,
    required this.data,
  });

  int status;
  List<Datum> data;

  factory TransactionHistoryModel.fromJson(Map<String, dynamic> json) => TransactionHistoryModel(
    status: json["status"],
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum {
  Datum({
    required this.id,
    required this.userId,
    required this.transactionDate,
    required this.amount,
    required this.type,
    required this.address,
    required this.createdAt,
    required this.v,
    required this.hash
  });

  String id;
  String userId;
  String transactionDate;
  int amount;
  String type;
  String address;
  DateTime createdAt;
  int v;
  String hash;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    id: json["_id"],
    userId: json["user_id"],
    transactionDate: json["transaction_date"],
    amount: json["amount"],
    type: json["type"],
    address: json["address"],
    createdAt: DateTime.parse(json["created_at"]),
    v: json["__v"],
    hash: json["payment_hash"]

  );

  Map<String, dynamic> toJson() => {
    "_id": id,
    "user_id": userId,
    "transaction_date": transactionDate,
    "amount": amount,
    "type": type,
    "address": address,
    "created_at": createdAt.toIso8601String(),
    "__v": v,
    "payment_hash":hash
  };
}
