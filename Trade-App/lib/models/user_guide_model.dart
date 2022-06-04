// To parse this JSON data, do
//
//     final userGuideModel = userGuideModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

UserGuideModel userGuideModelFromJson(String str) => UserGuideModel.fromJson(json.decode(str));

String userGuideModelToJson(UserGuideModel data) => json.encode(data.toJson());

class UserGuideModel {
  UserGuideModel({
    required this.status,
    required this.message,
    required this.userGuidedata,
  });

  final String status;
  final String message;
  final List<UserGuidedatum> userGuidedata;

  factory UserGuideModel.fromJson(Map<String, dynamic> json) => UserGuideModel(
    status: json["status"],
    message: json["message"],
    userGuidedata: List<UserGuidedatum>.from(json["UserGuidedata"].map((x) => UserGuidedatum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "UserGuidedata": List<dynamic>.from(userGuidedata.map((x) => x.toJson())),
  };
}

class UserGuidedatum {
  UserGuidedatum({
    required this.catname,
    required this.data,
  });

  final String catname;
  final List<CategoryData> data;

  factory UserGuidedatum.fromJson(Map<String, dynamic> json) => UserGuidedatum(
    catname: json["catname"],
    data: List<CategoryData>.from(json["data"].map((x) => CategoryData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "catname": catname,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class CategoryData {
  CategoryData({
    required this.tblTutorialCategoryId,
    required this.name,
    required this.icon,
  });

  final int tblTutorialCategoryId;
  final String name;
  final String icon;

  factory CategoryData.fromJson(Map<String, dynamic> json) => CategoryData(
    tblTutorialCategoryId: json["tbl_tutorial_category_id"],
    name: json["name"],
    icon: json["icon"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_tutorial_category_id": tblTutorialCategoryId,
    "name": name,
    "icon": icon,
  };
}
