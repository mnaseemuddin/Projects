

// To parse this JSON data, do
//
//     final affiliateLoginResponse = affiliateLoginResponseFromJson(jsonString);

import 'dart:convert';

AffiliateLoginResponse affiliateLoginResponseFromJson(String str) => AffiliateLoginResponse.fromJson(json.decode(str));

String affiliateLoginResponseToJson(AffiliateLoginResponse data) => json.encode(data.toJson());

class AffiliateLoginResponse {
  AffiliateLoginResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  bool status;
  String message;
  List<Datum> data;

  factory AffiliateLoginResponse.fromJson(Map<String, dynamic> json) => AffiliateLoginResponse(
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
    required this.email,
    required this.mobile,
    required this.referalCode,
    required this.usedReferalCode,
    required this.jwtToken,
    required this.amountStack
  });

  int tblUserId;
  String fullName;
  String email;
  String mobile;
  String referalCode;
  String usedReferalCode;
  String jwtToken;
  int amountStack;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblUserId: json["tbl_user_id"],
    fullName: json["full_name"],
    email: json["email"],
    mobile: json["mobile"],
    referalCode: json["referal_code"],
    usedReferalCode: json["used_referal_code"],
    jwtToken: json["jwt_token"],
    amountStack:json["amount_stake"]
  );

  Map<String, dynamic> toJson() => {
    "tbl_user_id": tblUserId,
    "full_name": fullName,
    "email": email,
    "mobile": mobile,
    "referal_code": referalCode,
    "used_referal_code": usedReferalCode,
    "jwt_token": jwtToken,
    "amount_stake":amountStack
  };
}
