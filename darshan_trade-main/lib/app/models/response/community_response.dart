// To parse this JSON data, do
//
//     final communityResponse = communityResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

CommunityResponse communityResponseFromJson(String str) => CommunityResponse.fromJson(json.decode(str));

String communityResponseToJson(CommunityResponse data) => json.encode(data.toJson());

class CommunityResponse {
  CommunityResponse({
    required this.regitrationBonus,
    required this.totalinviting,
    required this.firstlevelmem,
    required this.secondlevelmem,
    required this.activeuser,
    required this.teamactivetradevol,
    required this.inactiveusers,
    required this.agentidentity,
    required this.ratio,
    required this.id,
    required this.status,
  });

  final double regitrationBonus;
  final int totalinviting;
  final int firstlevelmem;
  final int secondlevelmem;
  final int activeuser;
  final double teamactivetradevol;
  final int inactiveusers;
  final String agentidentity;
  final double ratio;
  final int id;
  final String status;

  factory CommunityResponse.fromJson(Map<String, dynamic> json) => CommunityResponse(
    regitrationBonus: json["regitrationBonus"],
    totalinviting: json["totalinviting"],
    firstlevelmem: json["firstlevelmem"],
    secondlevelmem: json["secondlevelmem"],
    activeuser: json["activeuser"],
    teamactivetradevol: json["teamactivetradevol"],
    inactiveusers: json["inactiveusers"],
    agentidentity: json["agentidentity"],
    ratio: json["ratio"],
    id: json["id"],
    status: json["status"],
  );

  Map<String, dynamic> toJson() => {
    "regitrationBonus": regitrationBonus,
    "totalinviting": totalinviting,
    "firstlevelmem": firstlevelmem,
    "secondlevelmem": secondlevelmem,
    "activeuser": activeuser,
    "teamactivetradevol": teamactivetradevol,
    "inactiveusers": inactiveusers,
    "agentidentity": agentidentity,
    "ratio": ratio,
    "id": id,
    "status": status,
  };
}
