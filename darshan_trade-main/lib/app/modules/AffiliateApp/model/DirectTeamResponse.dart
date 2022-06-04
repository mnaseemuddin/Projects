// To parse this JSON data, do
//
//     final directTeamResponse = directTeamResponseFromJson(jsonString);

import 'dart:convert';

AffDirectTeamResponse affdirectTeamResponseFromJson(String str) => AffDirectTeamResponse.fromJson(json.decode(str));

String directTeamResponseToJson(AffDirectTeamResponse data) => json.encode(data.toJson());

class AffDirectTeamResponse {
  AffDirectTeamResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  bool status;
  String message;
  List<Datum> data;

  factory AffDirectTeamResponse.fromJson(Map<String, dynamic> json) => AffDirectTeamResponse(
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
    required this.referalCode,
    required this.mobile,
    required this.amountStake,
  });

  int tblUserId;
  String fullName;
  String referalCode;
  String mobile;
  int amountStake;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblUserId: json["tbl_user_id"],
    fullName: json["full_name"],
    referalCode: json["referal_code"],
    mobile: json["mobile"],
    amountStake: json["amount_stake"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_user_id": tblUserId,
    "full_name": fullName,
    "referal_code": referalCode,
    "mobile": mobile,
    "amount_stake": amountStake,
  };
}
