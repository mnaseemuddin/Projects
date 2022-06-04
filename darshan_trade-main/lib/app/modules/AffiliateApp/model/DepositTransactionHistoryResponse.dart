// To parse this JSON data, do
//
//     final depositTransationResponse = depositTransationResponseFromJson(jsonString);

import 'dart:convert';

DepositTransationResponse depositTransationResponseFromJson(String str) => DepositTransationResponse.fromJson(json.decode(str));

String depositTransationResponseToJson(DepositTransationResponse data) => json.encode(data.toJson());

class DepositTransationResponse {
  DepositTransationResponse({
    required this.status,
    required this.message,
    required this.totalRoiIncome,
    required this.data,
  });

  bool status;
  String message;
  String totalRoiIncome;
  List<Datum> data;

  factory DepositTransationResponse.fromJson(Map<String, dynamic> json) => DepositTransationResponse(
    status: json["status"],
    message: json["message"],
    totalRoiIncome: json["total_roi_income"].toString(),
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "total_roi_income": totalRoiIncome,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum {
  Datum({
    required this.tblDepositeAmountId,
    required this.userId,
    required this.amount,
    required this.packageName,
    required this.numberOfMonth,
    required this.trasectionType,
    required this.trasectionId,
    required this.createdDate,
    required this.toaddres
  });

  int tblDepositeAmountId;
  int userId;
  int amount;
  String packageName;
  int numberOfMonth;
  String trasectionType;
  String trasectionId;
  String createdDate;
  String toaddres;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblDepositeAmountId: json["tbl_deposite_amount_id"],
    userId: json["user_id"],
    amount: json["amount"],
    packageName: json["package_name"],
    numberOfMonth: json["number_of_month"],
    trasectionType: json["trasection_type"],
    trasectionId: json["trasection_id"],
    createdDate: json["created_date"],
    toaddres: json["to_addres"]
  );

  Map<String, dynamic> toJson() => {
    "tbl_deposite_amount_id": tblDepositeAmountId,
    "user_id": userId,
    "amount": amount,
    "package_name": packageName,
    "number_of_month": numberOfMonth,
    "trasection_type": trasectionType,
    "trasection_id": trasectionId,
    "created_date": createdDate,
    "to_addres":toaddres
  };
}
