// To parse this JSON data, do
//
//     final tabItemModel = tabItemModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

TabItemModel tabItemModelFromJson(String str) => TabItemModel.fromJson(json.decode(str));

String tabItemModelToJson(TabItemModel data) => json.encode(data.toJson());

class TabItemModel {
  TabItemModel({
    required this.message,
    required this.status,
    required this.data,
  });

  final String message;
  final String status;
  final List<TabData> data;

  factory TabItemModel.fromJson(Map<String, dynamic> json) => TabItemModel(
    message: json["message"],
    status: json["status"],
    data: List<TabData>.from(json["data"].map((x) => TabData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "message": message,
    "status": status,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class TabData {
  TabData({
    required this.categoryName,
    required this.categoryId,
  });

  final String categoryName;
  final String categoryId;

  factory TabData.fromJson(Map<String, dynamic> json) => TabData(
    categoryName: json["Category_name"],
    categoryId: json["Category_id"],
  );

  Map<String, dynamic> toJson() => {
    "Category_name": categoryName,
    "Category_id": categoryId,
  };
}
