// To parse this JSON data, do
//
//     final revnueListDateResponse = revnueListDateResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<RevnueListDateResponse> revnueListDateResponseFromJson(String str) => List<RevnueListDateResponse>.from(json.decode(str).map((x) => RevnueListDateResponse.fromJson(x)));

String revnueListDateResponseToJson(List<RevnueListDateResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class RevnueListDateResponse {
  RevnueListDateResponse({
    required this.transdate,
    required this.credit,
    required this.side,
    required this.remark,
    required this.orderNo,
  });

  final String transdate;
  final String credit;
  final String side;
  final String remark;
  final String orderNo;

  factory RevnueListDateResponse.fromJson(Map<String, dynamic> json) => RevnueListDateResponse(
    transdate: json["transdate"],
    credit: json["credit"],
    side: json["side"],
    remark: json["remark"],
    orderNo: json["orderNo"],
  );

  Map<String, dynamic> toJson() => {
    "transdate": transdate,
    "credit": credit,
    "side": side,
    "remark": remark,
    "orderNo": orderNo,
  };
}
