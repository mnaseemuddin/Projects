// To parse this JSON data, do
//
//     final rewardResponse = rewardResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<RewardResponse> rewardResponseFromJson(String str) => List<RewardResponse>.from(json.decode(str).map((x) => RewardResponse.fromJson(x)));

String rewardResponseToJson(List<RewardResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class RewardResponse {
  RewardResponse({
    required this.transdate,
    required this.credit,
    required this.remark,
    required this.fromUser,
  });

  final String transdate;
  final double credit;
  final String remark;
  final String fromUser;

  factory RewardResponse.fromJson(Map<String, dynamic> json) => RewardResponse(
    transdate: json["transdate"]??'',
    credit: json["credit"] is double ?json["credit"]:double.parse(json["credit"]??'0.0'),
    remark: json["remark"]??'',
    fromUser: json["from_User"]??'',
  );

  Map<String, dynamic> toJson() => {
    "transdate": transdate,
    "credit": credit,
    "remark": remark,
    "from_User": fromUser,
  };
}
