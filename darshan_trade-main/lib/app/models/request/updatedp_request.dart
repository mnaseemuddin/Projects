// To parse this JSON data, do
//
//     final updateDpRequest = updateDpRequestFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

UpdateDpRequest updateDpRequestFromJson(String str) => UpdateDpRequest.fromJson(json.decode(str));

String updateDpRequestToJson(UpdateDpRequest data) => json.encode(data.toJson());

class UpdateDpRequest {
  UpdateDpRequest({
    required this.id,
    required this.docBase64,
  });

  final int id;
  final String docBase64;

  factory UpdateDpRequest.fromJson(Map<String, dynamic> json) => UpdateDpRequest(
    id: json["id"],
    docBase64: json["DocBase64"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "DocBase64": docBase64,
  };
}
