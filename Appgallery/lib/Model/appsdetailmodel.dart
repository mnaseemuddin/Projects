
// To parse this JSON data, do
//
//     final appsDetailModel = appsDetailModelFromJson(jsonString);

import 'dart:convert';

AppsDetailModel appsDetailModelFromJson(String str) => AppsDetailModel.fromJson(json.decode(str));

String appsDetailModelToJson(AppsDetailModel data) => json.encode(data.toJson());

class AppsDetailModel {
  AppsDetailModel({
    required this.status,
    required this.data,
  });

  int status;
  List<Datum> data;

  factory AppsDetailModel.fromJson(Map<String, dynamic> json) => AppsDetailModel(
    status: json["status"],
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum {
  Datum({
    required this.id,
    required this.appInstallationCount,
    required this.appName,
    required this.appCategory,
    required this.appType,
    required this.createdDate,
    required this.screenShot,
    required this.apkInformation,
    required this.review,
    required this.appFullDescription,
    required this.appGraphic,
    required this.appIcon,
    required this.appShortDescription,
    required this.averageReview
  });

  String id;
  int appInstallationCount;
  String appName;
  String appCategory;
  String appType;
  String createdDate;
  List<ScreenShot> screenShot;
  List<ApkInformation> apkInformation;
  List<dynamic> review;
  String appFullDescription;
  String appGraphic;
  String appIcon;
  String appShortDescription;
  double averageReview;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    id: json["_id"],
    appInstallationCount: json["app_installation_count"],
    appName: json["app_name"],
    appCategory: json["app_category"],
    appType: json["app_type"],
    createdDate: json["created_date"],
    screenShot: List<ScreenShot>.from(json["screen_shot"].map((x) => ScreenShot.fromJson(x))),
    apkInformation: List<ApkInformation>.from(json["apk_information"].map((x) => ApkInformation.fromJson(x))),
    review: List<dynamic>.from(json["review"].map((x) => x)),
    appFullDescription: json["app_full_description"],
    appGraphic: json["app_graphic"],
    appIcon: json["app_icon"],
    appShortDescription: json["app_short_description"],
    averageReview: json["Avreview"]==null?0.0:json["Avreview"].toDouble()
  );

  Map<String, dynamic> toJson() => {
    "_id": id,
    "app_installation_count": appInstallationCount,
    "app_name": appName,
    "app_category": appCategory,
    "app_type": appType,
    "created_date": createdDate,
    "screen_shot": List<dynamic>.from(screenShot.map((x) => x.toJson())),
    "apk_information": List<dynamic>.from(apkInformation.map((x) => x.toJson())),
    "review": List<dynamic>.from(review.map((x) => x)),
    "app_full_description": appFullDescription,
    "app_graphic": appGraphic,
    "app_icon": appIcon,
    "app_short_description": appShortDescription,
    "Avreview":averageReview
  };
}

class ApkInformation {
  ApkInformation({
    required this.apk,
    required this.versionName,
    required this.versionCode,
    required this.package,
    required this.lastUpdate,
    required this.id,
  });

  String apk;
  String versionName;
  String versionCode;
  String package;
  String lastUpdate;
  String id;

  factory ApkInformation.fromJson(Map<String, dynamic> json) => ApkInformation(
    apk: json["apk"],
    versionName: json["versionName"],
    versionCode: json["versionCode"],
    package: json["package"],
    lastUpdate: json["last_update"],
    id: json["_id"],
  );

  Map<String, dynamic> toJson() => {
    "apk": apk,
    "versionName": versionName,
    "versionCode": versionCode,
    "package": package,
    "last_update": lastUpdate,
    "_id": id,
  };
}

class ScreenShot {
  ScreenShot({
    required this.filename,
    required this.id,
  });

  String filename;
  String id;

  factory ScreenShot.fromJson(Map<String, dynamic> json) => ScreenShot(
    filename: json["filename"],
    id: json["_id"],
  );

  Map<String, dynamic> toJson() => {
    "filename": filename,
    "_id": id,
  };
}
