// To parse this JSON data, do
//
//     final userModel = userModelFromJson(jsonString);

import 'dart:convert';

UserModel userModelFromJson(String str) => UserModel.fromJson(json.decode(str));

String userModelToJson(UserModel data) => json.encode(data.toJson());

class UserModel {
  UserModel({
    required this.status,
    required this.message,
    required this.data,
  });

  int status;
  String message;
  List<Datum> data;

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
    required this.token,
    required this.userId,
    required this.email,
    required this.name,
    required this.userImage,
  });

  String token;
  String userId;
  String email;
  String name;
  String userImage;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    token: json["token"],
    userId: json["user_id"],
    email: json["email"],
    name: json["name"],
    userImage: json["user_image"],
  );

  Map<String, dynamic> toJson() => {
    "token": token,
    "user_id": userId,
    "email": email,
    "name": name,
    "user_image": userImage,
  };
}
