// To parse this JSON data, do
//
//     final registerRequest = registerRequestFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

RegisterRequest registerRequestFromJson(String str) => RegisterRequest.fromJson(json.decode(str));

String registerRequestToJson(RegisterRequest data) => json.encode(data.toJson());

class RegisterRequest {
  RegisterRequest({
    // required this.name,
    required this.referralcode,
    required this.countryname,
    required this.email,
    required this.username,
    required this.password,
    required this.MobileNo,
  });

  // final String name;
  final String referralcode;
  final String countryname;
  final String email;
  final String username;
  final String password;
  final String MobileNo;

  factory RegisterRequest.fromJson(Map<String, dynamic> json) => RegisterRequest(
    // name: json["name"],
    referralcode: json["referralcode"],
    countryname: json["countryname"],
    email: json["Email"],
    username: json["username"],
    password: json["Password"],
    MobileNo: json["MobileNo"],
  );

  Map<String, dynamic> toJson() => {
    // "name": name,
    "referralcode": referralcode,
    "countryname": countryname,
    "Email": email,
    "username": username,
    "Password": password,
    "MobileNo": MobileNo,
  };
}
