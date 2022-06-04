// To parse this JSON data, do
//
//     final rankTeamResponse = rankTeamResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<RankTeamResponse> rankTeamResponseFromJson(String str) => List<RankTeamResponse>.from(json.decode(str).map((x) => RankTeamResponse.fromJson(x)));

String rankTeamResponseToJson(List<RankTeamResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class RankTeamResponse {
  RankTeamResponse({
    required this.ranks,
    required this.totalteam,
  });

  final String ranks;
  final int totalteam;

  factory RankTeamResponse.fromJson(Map<String, dynamic> json) => RankTeamResponse(
    ranks: json["ranks"],
    totalteam: json["totalteam"],
  );

  Map<String, dynamic> toJson() => {
    "ranks": ranks,
    "totalteam": totalteam,
  };
}
