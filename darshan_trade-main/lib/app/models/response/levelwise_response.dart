// To parse this JSON data, do
//
//     final levelWiseResponse = levelWiseResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<LevelWiseResponse> levelWiseResponseFromJson(String str) => List<LevelWiseResponse>.from(json.decode(str).map((x) => LevelWiseResponse.fromJson(x)));

String levelWiseResponseToJson(List<LevelWiseResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class LevelWiseResponse {
  LevelWiseResponse({
    required this.username,
    required this.name,
    required this.status,
    required this.rank,
    required this.uuid,
  });

  final String username;
  final String name;
  final String status;
  final String rank;
  final String uuid;

  factory LevelWiseResponse.fromJson(Map<String, dynamic> json) => LevelWiseResponse(
    username: json["username"],
    name: json["name"],
    status: json["status"],
    rank: json["rank"],
    uuid: json["uuid"],
  );

  Map<String, dynamic> toJson() => {
    "username": username,
    "name": name,
    "status": status,
    "rank": rank,
    "uuid": uuid,
  };
}

