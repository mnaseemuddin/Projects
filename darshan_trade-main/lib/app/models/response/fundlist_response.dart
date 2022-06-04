// To parse this JSON data, do
//
//     final fundListResponse = fundListResponseFromJson(jsonString);

import 'package:intl/intl.dart';
import 'package:meta/meta.dart';
import 'dart:convert';

List<FundListResponse> fundListResponseFromJson(String str) => List<FundListResponse>.from(json.decode(str).map((x) => FundListResponse.fromJson(x)));

String fundListResponseToJson(List<FundListResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class FundListResponse {
  FundListResponse({
    required this.createDate,
    required this.credit,
    required this.debit,
    required this.ttype,
    this.dateTime,
  });

  final String createDate;
  final double credit;
  final double debit;
  final String ttype;
  final DateTime? dateTime;

  factory FundListResponse.fromJson(Map<String, dynamic> json) => FundListResponse(
    createDate: json["createDate"],
    credit: json["credit"],
    debit: json["debit"],
    ttype: json["ttype"],
      dateTime: DateFormat("MM/dd/yyyy hh:mm:ss").parse(json["createDate"])
      // 11/01/2021 13:49:48
      // dd/MM/yyyy HH:mm:ss
  );

  Map<String, dynamic> toJson() => {
    "createDate": createDate,
    "credit": credit,
    "debit": debit,
    "ttype": ttype,
  };
}
