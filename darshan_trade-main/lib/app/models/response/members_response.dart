// To parse this JSON data, do
//
//     final membersResponse = membersResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<MembersResponse> membersResponseFromJson(String str) => List<MembersResponse>.from(json.decode(str).map((x) => MembersResponse.fromJson(x)));

String membersResponseToJson(List<MembersResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class MembersResponse {
  MembersResponse({
    required this.start1,
    required this.start2,
    required this.cond,
    required this.list,
    required this.color,
  });

  final String start1;
  final String start2;
  final String cond;
  final String color;
  final List<ListElement> list;

  factory MembersResponse.fromJson(Map<String, dynamic> json) => MembersResponse(
    start1: json["start_1"],
    start2: json["start_2"],
    cond: json["cond"],
    color: json["color"],
    list: List<ListElement>.from(json["list"].map((x) => ListElement.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "start_1": start1,
    "start_2": start2,
    "cond": cond,
    "color": color,
    "list": List<dynamic>.from(list.map((x) => x.toJson())),
  };
}

class ListElement {
  ListElement({
    required this.icon,
    required this.heading,
    required this.content,
  });

  final String icon;
  final String heading;
  final String content;

  factory ListElement.fromJson(Map<String, dynamic> json) => ListElement(
    icon: json["icon"],
    heading: json["heading"],
    content: json["content"],
  );

  Map<String, dynamic> toJson() => {
    "icon": icon,
    "heading": heading,
    "content": content,
  };
}
