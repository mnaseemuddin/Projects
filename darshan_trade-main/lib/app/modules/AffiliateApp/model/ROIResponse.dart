

// To parse this JSON data, do
//
//     final roiResposne = roiResposneFromJson(jsonString);

import 'dart:convert';

RoiResposne roiResposneFromJson(String str) => RoiResposne.fromJson(json.decode(str));

String roiResposneToJson(RoiResposne data) => json.encode(data.toJson());

class RoiResposne {
  RoiResposne({
    required this.status,
    required this.message,
    required this.totalRoiIncome,
    required this.todayRoiIncome,
    required this.data,
  });

  bool status;
  String message;
  var totalRoiIncome;
  var todayRoiIncome;
  List<Datum> data;

  factory RoiResposne.fromJson(Map<String, dynamic> json) => RoiResposne(
    status: json["status"],
    message: json["message"],
    totalRoiIncome: json["total_roi_income"],
    todayRoiIncome: json["today_roi_income"],
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "total_roi_income": totalRoiIncome,
    "today_roi_income": todayRoiIncome,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum {
  Datum({
    required this.tblRoiTransferId,
    required this.packageName,
    required this.packageAmount,
    required this.roiAmount,
    required this.roiPercentage,
    required this.createdDate,
  });

  int tblRoiTransferId;
  String packageName;
  var packageAmount;
  var roiAmount;
  var roiPercentage;
  String createdDate;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblRoiTransferId: json["tbl_roi_transfer_id"],
    packageName: json["package_name"],
    packageAmount: json["package_amount"],
    roiAmount: json["roi_amount"],
    roiPercentage: json["roi_percentage"],
    createdDate: json["created_date"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_roi_transfer_id": tblRoiTransferId,
    "package_name": packageName,
    "package_amount": packageAmount,
    "roi_amount": roiAmount,
    "roi_percentage": roiPercentage,
    "created_date": createdDate,
  };
}
