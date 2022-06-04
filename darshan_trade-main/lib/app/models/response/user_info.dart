// To parse this JSON data, do
//
//     final userInfo = userInfoFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

UserInfo userInfoFromJson(String str) => UserInfo.fromJson(json.decode(str));

String userInfoToJson(UserInfo data) => json.encode(data.toJson());

class UserInfo {
  UserInfo({
    required this.status,
    required this.message,
    required this.id,
    required this.username,
    required this.password,
    required this.name,
    required this.referralcode,
    required this.invitaionCode,
    required this.countryname,
    required this.email,
    required this.ispaid,
    required this.pic,
    required this.ranks,
  });

  final String status;
  final String message;
  final int id;
  final String username;
  final String password;
  final String name;
  final String referralcode;
  final String invitaionCode;
  final String countryname;
  final String email;
   int ispaid;
  final String pic;
  final String ranks;

  factory UserInfo.fromJson(Map<String, dynamic> json) => UserInfo(
    status: json["status"],
    message: json["message"]??'',
    id: json["id"],
    username: json["username"],
    password: json["password"],
    name: json["name"]??'',
    referralcode: json["referralcode"]??'',
    invitaionCode: json["invitaionCode"]??'',
    countryname: json["countryname"],
    email: json["email"],
    ispaid: json["ispaid"],
    pic: json["pic"]??'',
    ranks: json["ranks"]??'',
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "id": id,
    "username": username,
    "password": password,
    "name": name,
    "referralcode": referralcode,
    "invitaionCode": invitaionCode,
    "countryname": countryname,
    "email": email,
    "ispaid": ispaid,
    "pic": pic,
    "ranks": ranks,
  };
}



/**

import 'package:meta/meta.dart';
import 'dart:convert';

UserInfo userInfoFromJson(String str) => UserInfo.fromJson(json.decode(str));

String userInfoToJson(UserInfo data) => json.encode(data.toJson());

class UserInfo{
  UserInfo({
    required this.status,
    required this.message,
    required this.id,
    required this.username,
    required this.password,
    required this.name,
    required this.referralcode,
    required this.countryname,
    required this.email,
    required this.pic,
  });

// Rizvan

  final String status;
  final String message;
  final int id;
  final String username;
  final String password;
  final String name;
  final String referralcode;
  final String countryname;
  final String email;
  final String pic;

  factory UserInfo.fromJson(Map<String, dynamic> json) => UserInfo(
    status: json["status"],
    message: json["message"]??'',
    id: json["id"],
    username: json["username"]??'',
    password: json["password"]??'',
    name: json["name"]??'',
    referralcode: json["referralcode"]??'',
    countryname: json["countryname"]??'',
    email: json["email"]??'',
    pic: json["pic"]??'',
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "id": id,
    "username": username,
    "password": password,
    "name": name,
    "referralcode": referralcode,
    "countryname": countryname,
    "email": email,
    "pic": pic,
  };
}

    **/