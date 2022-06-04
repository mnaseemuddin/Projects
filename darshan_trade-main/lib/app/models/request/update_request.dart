// To parse this JSON data, do
//
//     final updateRequest = updateRequestFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

UpdateRequest updateRequestFromJson(String str) => UpdateRequest.fromJson(json.decode(str));

String updateRequestToJson(UpdateRequest data) => json.encode(data.toJson());

class UpdateRequest {
  UpdateRequest({
    required this.id,
    required this.name,
    required this.referralcode,
    required this.countryname,
    required this.email,
  });

  final int id;
  late String name;
  final String referralcode;
  late final String countryname;
  late final String email;

  factory UpdateRequest.fromJson(Map<String, dynamic> json) => UpdateRequest(
    id: json["id"],
    name: json["name"],
    referralcode: json["referralcode"],
    countryname: json["countryname"],
    email: json["Email"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "name": name,
    "referralcode": referralcode,
    "countryname": countryname,
    "Email": email,
  };
}
