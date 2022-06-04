// To parse this JSON data, do
//
//     final affiliateLevelIncomeResponse = affiliateLevelIncomeResponseFromJson(jsonString);

import 'dart:convert';

AffiliateLevelIncomeResponse affiliateLevelIncomeResponseFromJson(String str) => AffiliateLevelIncomeResponse.fromJson(json.decode(str));

String affiliateLevelIncomeResponseToJson(AffiliateLevelIncomeResponse data) => json.encode(data.toJson());

class AffiliateLevelIncomeResponse {
  AffiliateLevelIncomeResponse({
    required this.status,
    required this.message,
    required this.totalLevelIncome,
    required this.todayLevelIncome,
    required this.data,
  });

  bool status;
  String message;
  double totalLevelIncome;
  double todayLevelIncome;
  List<Datum1> data;

  factory AffiliateLevelIncomeResponse.fromJson(Map<String, dynamic> json) => AffiliateLevelIncomeResponse(
    status: json["status"],
    message: json["message"],
    totalLevelIncome: json["total_level_income"].toDouble(),
    todayLevelIncome: json["today_level_income"].toDouble(),
    data: List<Datum1>.from(json["data"].map((x) => Datum1.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "total_level_income": totalLevelIncome,
    "today_level_income": todayLevelIncome,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum1 {
  Datum1({
    required this.tblLevelIncomeId,
    required this.packageName,
    required this.packageId,
    required this.levelAmount,
    required this.roiAmount,
    required this.createdDate,
  });

  int tblLevelIncomeId;
  String packageName;
  int packageId;
  double levelAmount;
  double roiAmount;
  String createdDate;

  factory Datum1.fromJson(Map<String, dynamic> json) => Datum1(
    tblLevelIncomeId: json["tbl_level_income_id"],
    packageName: json["package_name"],
    packageId: json["package_id"],
    levelAmount: json["level_amount"].toDouble(),
    roiAmount: json["roi_amount"].toDouble(),
    createdDate: json["created_date"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_level_income_id": tblLevelIncomeId,
    "package_name": packageName,
    "package_id": packageId,
    "level_amount": levelAmount,
    "roi_amount": roiAmount,
    "created_date": createdDate,
  };
}
