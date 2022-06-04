// To parse this JSON data, do
//
//     final otherWalletModel = otherWalletModelFromJson(jsonString);

import 'dart:convert';

OtherWalletModel otherWalletModelFromJson(String str) => OtherWalletModel.fromJson(json.decode(str));

String otherWalletModelToJson(OtherWalletModel data) => json.encode(data.toJson());

class OtherWalletModel {
  OtherWalletModel({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<Datum> data;

  factory OtherWalletModel.fromJson(Map<String, dynamic> json) => OtherWalletModel(
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
    required this.tblOrderId,
    required this.orderNumber,
    required this.totalAmount,
    required this.orderDateTime,
    required this.otherWallet,
  });

  int tblOrderId;
  String orderNumber;
  String totalAmount;
  String orderDateTime;
  String otherWallet;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblOrderId: json["tbl_order_id"],
    orderNumber: json["order_number"],
    totalAmount: json["total_amount"],
    orderDateTime: json["order_date_time"],
    otherWallet: json["other_wallet"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_order_id": tblOrderId,
    "order_number": orderNumber,
    "total_amount": totalAmount,
    "order_date_time": orderDateTime,
    "other_wallet": otherWallet,
  };
}
