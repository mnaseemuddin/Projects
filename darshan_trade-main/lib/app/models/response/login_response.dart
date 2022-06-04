// To parse this JSON data, do
//
//     final loginResponse = loginResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

LoginResponse loginResponseFromJson(String str) => LoginResponse.fromJson(json.decode(str));

String loginResponseToJson(LoginResponse data) => json.encode(data.toJson());

class LoginResponse {
  LoginResponse({
    required this.id,
    required this.status,
    required this.message,
    required this.username,
    required this.password,
  });

  final int id;
  final String status;
  final String message;
  final String username;
  final String password;

  factory LoginResponse.fromJson(Map<String, dynamic> json) => LoginResponse(
    id: json["id"],
    status: json["status"],
    message: json["message"],
    username: json["username"]??'',
    password: json["password"]??'',
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "status": status,
    "message": message,
    "username": username,
    "password": password,
  };
}
