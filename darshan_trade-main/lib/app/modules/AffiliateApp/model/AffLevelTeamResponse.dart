// To parse this JSON data, do
//
//     final affLevelTeamResponse = affLevelTeamResponseFromJson(jsonString);

import 'dart:convert';

AffLevelTeamResponse affLevelTeamResponseFromJson(String str) => AffLevelTeamResponse.fromJson(json.decode(str));

String affLevelTeamResponseToJson(AffLevelTeamResponse data) => json.encode(data.toJson());

class AffLevelTeamResponse {
  AffLevelTeamResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  bool status;
  String message;
  List<Datum> data;

  factory AffLevelTeamResponse.fromJson(Map<String, dynamic> json) => AffLevelTeamResponse(
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
    required this.tblRoiLevelPercentageId,
    required this.levelName,
    required this.level,
    required this.total,
  });

  int tblRoiLevelPercentageId;
  String levelName;
  int level;
  String total;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblRoiLevelPercentageId: json["tbl_roi_level_percentage_id"],
    levelName: json["level_name"],
    level: json["level"],
    total: json["total"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_roi_level_percentage_id": tblRoiLevelPercentageId,
    "level_name": levelName,
    "level": level,
    "total": total,
  };
}
