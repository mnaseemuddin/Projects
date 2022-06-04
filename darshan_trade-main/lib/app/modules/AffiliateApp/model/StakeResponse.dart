


// To parse this JSON data, do
//
//     final stakeResponse = stakeResponseFromJson(jsonString);

import 'dart:convert';

StakeResponse stakeResponseFromJson(String str) => StakeResponse.fromJson(json.decode(str));

String stakeResponseToJson(StakeResponse data) => json.encode(data.toJson());

class StakeResponse {
  StakeResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  bool status;
  String message;
  List<StackData> data;

  factory StakeResponse.fromJson(Map<String, dynamic> json) => StakeResponse(
    status: json["status"],
    message: json["message"],
    data: List<StackData>.from(json["data"].map((x) => StackData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class StackData {
  StackData({
    required this.tblStakePeriodId,
    required this.packageName,
    required this.numberOfMonth,
    required this.roiPercentage,
  });

  int tblStakePeriodId;
  String packageName;
  int numberOfMonth;
  double roiPercentage;

  factory StackData.fromJson(Map<String, dynamic> json) => StackData(
    tblStakePeriodId: json["tbl_stake_period_id"],
    packageName: json["package_name"],
    numberOfMonth: json["number_of_month"],
    roiPercentage: json["ROI_percentage"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_stake_period_id": tblStakePeriodId,
    "package_name": packageName,
    "number_of_month": numberOfMonth,
    "ROI_percentage": roiPercentage,
  };
}
