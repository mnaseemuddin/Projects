// To parse this JSON data, do
//
//     final memberResponse = memberResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

MemberResponse memberResponseFromJson(String str) => MemberResponse.fromJson(json.decode(str));

String memberResponseToJson(MemberResponse data) => json.encode(data.toJson());

class MemberResponse {
  MemberResponse({
    required this.status,
    required this.username,
    required this.activationtime,
    required this.remaindays,
    required this.ranks,
    required this.totalteam,
    required this.rewardratio,
    required this.activationamount,
    required this.ispaid,
  });

  final String status;
  final String username;
  final String activationtime;
  final int remaindays;
  final String ranks;
  final int totalteam;
  final double rewardratio;
  final double activationamount;
  final int ispaid;

  factory MemberResponse.fromJson(Map<String, dynamic> json) => MemberResponse(
    status: json["status"],
    username: json["username"],
    activationtime: json["activationtime"],
    remaindays: json["remaindays"] is int?json["remaindays"]:int.parse(json["remaindays"]),
    ranks: json["ranks"],
    totalteam: json["totalteam"],
    rewardratio: json["rewardratio"],
    activationamount: json["activationamount"],
    ispaid: json["ispaid"],
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "username": username,
    "activationtime": activationtime,
    "remaindays": remaindays,
    "ranks": ranks,
    "totalteam": totalteam,
    "rewardratio": rewardratio,
    "activationamount": activationamount,
    "ispaid": ispaid,
  };
}
