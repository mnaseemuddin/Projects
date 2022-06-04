

// To parse this JSON data, do
//
//     final billingDetailsByCoinModel = billingDetailsByCoinModelFromJson(jsonString);

import 'dart:convert';

List<BillingDetailsByCoinModel> billingDetailsByCoinModelFromJson(String str) => List<BillingDetailsByCoinModel>.from(json.decode(str).map((x) => BillingDetailsByCoinModel.fromJson(x)));

String billingDetailsByCoinModelToJson(List<BillingDetailsByCoinModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class BillingDetailsByCoinModel {
  BillingDetailsByCoinModel({
    required this.transdate,
    required this.orderNo,
    required this.credit,
    required this.side,
    required this.remark,
  });

  String transdate;
  String orderNo;
  String credit;
  String side;
  String remark;

  factory BillingDetailsByCoinModel.fromJson(Map<String, dynamic> json) => BillingDetailsByCoinModel(
    transdate: json["transdate"],
    orderNo: json["orderNo"],
    credit: json["credit"],
    side: json["side"],
    remark: json["remark"],
  );

  Map<String, dynamic> toJson() => {
    "transdate": transdate,
    "orderNo": orderNo,
    "credit": credit,
    "side": side,
    "remark": remark,
  };
}
