// To parse this JSON data, do
//
//     final userModel = userModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

UserModel userModelFromJson(String str) => UserModel.fromJson(json.decode(str));

String userModelToJson(UserModel data) => json.encode(data.toJson());

class UserModel {
  UserModel({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Datum> data;

  factory UserModel.fromJson(Map<String, dynamic> json) => UserModel(
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
    required this.userId,
    required this.username,
    required this.email,
    required this.emailVerify,
    required this.mobileVerify,
    required this.name,
    required this.mobile,
    required this.createDate,
    required this.userImage,
    required this.tdcWallet, required this.withdrawaddress
  });

  final int userId;
  final String username;
  final String email;
   int emailVerify;
   int mobileVerify;
   String name;
  String mobile;
  final String createDate;
  final String userImage;
  final String tdcWallet;
  String withdrawaddress;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    userId: json["user_id"],
    username: json["username"],
    email: json["email"],
    emailVerify: json["email_verify"],
    mobileVerify: json["mobile_verify"],
    name: json["name"],
    mobile: json["mobile"],
    createDate: json["create_date"],
    userImage: json["user_image"],
    tdcWallet: json['tdc_wallet']==null?"0":json['tdc_wallet'],
    withdrawaddress:json['payment_address']==null?"0":json['payment_address']
  );

  Map<String, dynamic> toJson() => {
    "user_id": userId,
    "username": username,
    "email": email,
    "email_verify": emailVerify,
    "mobile_verify": mobileVerify,
    "name": name,
    "mobile": mobile,
    "create_date": createDate,
    "user_image": userImage,
    "tdc_wallet":tdcWallet
  };
}



// // To parse this JSON data, do
// //
// //     final userModel = userModelFromJson(jsonString);
//
// import 'package:meta/meta.dart';
// import 'dart:convert';
//
// UserModel userModelFromJson(String str) => UserModel.fromJson(json.decode(str));
//
// String userModelToJson(UserModel data) => json.encode(data.toJson());
//
// class UserModel {
//   UserModel({
//     required this.status,
//     required this.message,
//     required this.data,
//   });
//
//   final String status;
//   final String message;
//   final List<Datum> data;
//
//   factory UserModel.fromJson(Map<String, dynamic> json) => UserModel(
//     status: json["status"],
//     message: json["message"],
//     data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
//   );
//
//   Map<String, dynamic> toJson() => {
//     "status": status,
//     "message": message,
//     "data": List<dynamic>.from(data.map((x) => x.toJson())),
//   };
// }
//
// class Datum {
//   Datum({
//     required this.userId,
//     required this.username,
//     required this.email,
//     required this.mobile,
//     required this.createDate,
//     required this.userImage,
//   });
//
//   final int userId;
//   String username;
//   final String email;
//   String mobile;
//   final String createDate;
//   final String userImage;
//
//   factory Datum.fromJson(Map<String, dynamic> json) => Datum(
//     userId: json["user_id"],
//     username: json["username"],
//     email: json["email"],
//     mobile: json["mobile"],
//     createDate: json["create_date"],
//     userImage: json["user_image"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "user_id": userId,
//     "username": username,
//     "email": email,
//     "mobile": mobile,
//     "create_date": createDate,
//     "user_image": userImage,
//   };
// }
//
//
