// To parse this JSON data, do
//
//     final apiKeyResponse = apiKeyResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

ApiKeyResponse apiKeyResponseFromJson(String str) => ApiKeyResponse.fromJson(json.decode(str));

String apiKeyResponseToJson(ApiKeyResponse data) => json.encode(data.toJson());

class ApiKeyResponse {
  ApiKeyResponse({
    required this.id,
    required this.exchanetype,
    required this.apikey,
    required this.secretkey,
    required this.status,
    required this.message,
    required this.ips,
  });

  final int id;
  final int exchanetype;
  final String apikey;
  final String secretkey;
  final String status;
  final String message;
  final String ips;

  factory ApiKeyResponse.fromJson(Map<String, dynamic> json) => ApiKeyResponse(
    id: json["id"],
    exchanetype: json["exchanetype"],
    apikey: json["apikey"],
    secretkey: json["secretkey"],
    status: json["status"],
    message: json["message"],
    ips: json["ips"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "exchanetype": exchanetype,
    "apikey": apikey,
    "secretkey": secretkey,
    "status": status,
    "message": message,
    "ips": ips,
  };
}



// // To parse this JSON data, do
// //
// //     final apiKeyResponse = apiKeyResponseFromJson(jsonString);
//
// import 'package:meta/meta.dart';
// import 'dart:convert';
//
// ApiKeyResponse apiKeyResponseFromJson(String str) => ApiKeyResponse.fromJson(json.decode(str));
//
// String apiKeyResponseToJson(ApiKeyResponse data) => json.encode(data.toJson());
//
// class ApiKeyResponse {
//   ApiKeyResponse({
//     required this.id,
//     required this.exchanetype,
//     required this.apikey,
//     required this.secretkey,
//     required this.status,
//     required this.message,
//   });
//
//   final int id;
//   final int exchanetype;
//   final String apikey;
//   final String secretkey;
//   final String status;
//   final String message;
//
//   factory ApiKeyResponse.fromJson(Map<String, dynamic> json) => ApiKeyResponse(
//     id: json["id"],
//     exchanetype: json["exchanetype"],
//     apikey: json["apikey"],
//     secretkey: json["secretkey"],
//     status: json["status"],
//     message: json["message"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "id": id,
//     "exchanetype": exchanetype,
//     "apikey": apikey,
//     "secretkey": secretkey,
//     "status": status,
//     "message": message,
//   };
// }
