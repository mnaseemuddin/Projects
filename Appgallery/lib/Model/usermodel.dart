// To parse this JSON data, do
//
//     final userModel = userModelFromJson(jsonString);

import 'dart:convert';

ConsoleLoginModel consoleModelFromJson(String str) => ConsoleLoginModel.fromJson(json.decode(str));

String consoleModelToJson(ConsoleLoginModel data) => json.encode(data.toJson());

class ConsoleLoginModel {
  ConsoleLoginModel({
    required this.status,
    required this.message,
    required this.data,
    required this.email,
    required this.name,
    required this.userImage,
    required this.mobile,
    required this.token
  });

  int status;
  String message;
  String data;
  String email;
  String name;
  String userImage;
  int mobile;
  String token;

  factory ConsoleLoginModel.fromJson(Map<String, dynamic> json) => ConsoleLoginModel(
    status: json["status"],
    message: json["message"],
    data: json["data"],
    email: json["email"],
    name: json["name"],
    userImage: json["user_image"],
    mobile: json["mobile"],
    token: json["token"]
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": data,
    "email": email,
    "name": name,
    "user_image": userImage,
    "mobile": mobile,
    "token":token
  };
}


// To parse this JSON data, do
//
//     final userModel = userModelFromJson(jsonString);


UserLoginModel userModelFromJson(String str) => UserLoginModel.fromJson(json.decode(str));

String userModelToJson1(UserLoginModel data) => json.encode(data.toJson());

class UserLoginModel {
  UserLoginModel({
    required this.status,
    required this.message,
    required this.data,
    required this.email,
    required this.name,
    required this.userImage,
    required this.mobile,
    required this.token
  });

  int status;
  String message;
  String data;
  String email;
  String name;
  String userImage;
  int mobile;
  String token;

  factory UserLoginModel.fromJson(Map<String, dynamic> json) => UserLoginModel(
      status: json["status"],
      message: json["message"],
      data: json["data"],
      email: json["email"],
      name: json["name"],
      userImage: json["user_image"],
      mobile: json["mobile"],
      token: json["token"]
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": data,
    "email": email,
    "name": name,
    "user_image": userImage,
    "mobile": mobile,
    "token":token
  };
}


