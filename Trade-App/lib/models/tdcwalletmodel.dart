


// To parse this JSON data, do
//
//     final tdcWalletModel = tdcWalletModelFromJson(jsonString);

import 'dart:convert';

TdcWalletModel tdcWalletModelFromJson(String str) => TdcWalletModel.fromJson(json.decode(str));

String tdcWalletModelToJson(TdcWalletModel data) => json.encode(data.toJson());

class TdcWalletModel {
  TdcWalletModel({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<TDatum> data;

  factory TdcWalletModel.fromJson(Map<String, dynamic> json) => TdcWalletModel(
    status: json["status"],
    message: json["message"],
    data: List<TDatum>.from(json["data"].map((x) => TDatum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class TDatum {
  TDatum({
    required this.tblOrderId,
    required this.orderNumber,
    required this.totalAmount,
    required this.orderDateTime,
    required this.allTdcDetect,
  });

  int tblOrderId;
  String orderNumber;
  String totalAmount;
  String orderDateTime;
  String allTdcDetect;

  factory TDatum.fromJson(Map<String, dynamic> json) => TDatum(
    tblOrderId: json["tbl_order_id"],
    orderNumber: json["order_number"],
    totalAmount: json["total_amount"],
    orderDateTime: json["order_date_time"],
    allTdcDetect: json["all_tdc_detect"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_order_id": tblOrderId,
    "order_number": orderNumber,
    "total_amount": totalAmount,
    "order_date_time": orderDateTime,
    "all_tdc_detect": allTdcDetect,
  };
}
