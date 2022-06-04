// To parse this JSON data, do
//
//     final affLevelWiseTeamResponse = affLevelWiseTeamResponseFromJson(jsonString);

import 'dart:convert';

AffLevelWiseTeamResponse affLevelWiseTeamResponseFromJson(String str) => AffLevelWiseTeamResponse.fromJson(json.decode(str));

String affLevelWiseTeamResponseToJson(AffLevelWiseTeamResponse data) => json.encode(data.toJson());

class AffLevelWiseTeamResponse {
  AffLevelWiseTeamResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  bool status;
  String message;
  List<Datum> data;

  factory AffLevelWiseTeamResponse.fromJson(Map<String, dynamic> json) => AffLevelWiseTeamResponse(
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
    required this.tblUserId,
    required this.fullName,
    required this.mobile,
    required this.referalCode,
    required this.amountStake,
  });

  int tblUserId;
  String fullName;
  String mobile;
  String referalCode;
  int amountStake;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblUserId: json["tbl_user_id"],
    fullName: json["full_name"],
    mobile: json["mobile"],
    referalCode: json["referal_code"],
    amountStake: json["amount_stake"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_user_id": tblUserId,
    "full_name": fullName,
    "mobile": mobile,
    "referal_code": referalCode,
    "amount_stake": amountStake,
  };
}
