// To parse this JSON data, do
//
//     final levelTeamResponse = levelTeamResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<LevelTeamResponse> levelTeamResponseFromJson(String str) => List<LevelTeamResponse>.from(json.decode(str).map((x) => LevelTeamResponse.fromJson(x)));

String levelTeamResponseToJson(List<LevelTeamResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class LevelTeamResponse {
  LevelTeamResponse({
    required this.id,
    required this.level,
    required this.total,
  });

  final int id;
  final int level;
  final int total;

  factory LevelTeamResponse.fromJson(Map<String, dynamic> json) => LevelTeamResponse(
    id: json["id"],
    level: json["level"],
    total: json["total"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "level": level,
    "total": total,
  };
}
