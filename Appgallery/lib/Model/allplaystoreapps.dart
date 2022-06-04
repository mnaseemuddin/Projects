// To parse this JSON data, do
//
//     final allPlayStoreApps = allPlayStoreAppsFromJson(jsonString);

import 'dart:convert';

AllPlayStoreApps allPlayStoreAppsFromJson(String str) => AllPlayStoreApps.fromJson(json.decode(str));

String allPlayStoreAppsToJson(AllPlayStoreApps data) => json.encode(data.toJson());

class AllPlayStoreApps {
  AllPlayStoreApps({
    required this.status,
    required this.totaldata,
    required this.category,
  });

  int status;
  int totaldata;
  List<AllApps> category;

  factory AllPlayStoreApps.fromJson(Map<String, dynamic> json) => AllPlayStoreApps(
    status: json["status"],
    totaldata: json["totaldata"],
    category: List<AllApps>.from(json["Category"].map((x) => AllApps.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "totaldata": totaldata,
    "Category": List<dynamic>.from(category.map((x) => x.toJson())),
  };
}

class AllApps {
  AllApps({
    required this.id,
    required this.appName,
    required this.appCategory,
    required this.appIcon,
    required this.appShortDescription,
  });

  String id;
  String appName;
  String appCategory;
  String appIcon;
  String appShortDescription;

  factory AllApps.fromJson(Map<String, dynamic> json) => AllApps(
    id: json["_id"],
    appName: json["app_name"],
    appCategory: json["app_category"],
    appIcon: json["app_icon"],
    appShortDescription: json["app_short_description"],
  );

  Map<String, dynamic> toJson() => {
    "_id": id,
    "app_name": appName,
    "app_category": appCategory,
    "app_icon": appIcon,
    "app_short_description": appShortDescription,
  };
}
