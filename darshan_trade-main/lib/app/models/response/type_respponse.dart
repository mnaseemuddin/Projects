// To parse this JSON data, do
//
//     final typeResponse = typeResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<TypeResponse> typeResponseFromJson(String str) => List<TypeResponse>.from(json.decode(str).map((x) => TypeResponse.fromJson(x)));

String typeResponseToJson(List<TypeResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class TypeResponse {
  TypeResponse({
    required this.type,
  });

  final String type;

  factory TypeResponse.fromJson(Map<String, dynamic> json) => TypeResponse(
    type: json["type"],
  );

  Map<String, dynamic> toJson() => {
    "type": type,
  };
}
