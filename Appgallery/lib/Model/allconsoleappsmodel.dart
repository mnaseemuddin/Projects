// To parse this JSON data, do
//
//     final allAppsModel = allAppsModelFromJson(jsonString);

import 'dart:convert';

AllConsoleAppsModel allAppsModelFromJson(String str) => AllConsoleAppsModel.fromJson(json.decode(str));

String allAppsModelToJson(AllConsoleAppsModel data) => json.encode(data.toJson());

class AllConsoleAppsModel {
  AllConsoleAppsModel({
    required this.status,
    required this.data,
  });

  int status;
  List<Datum> data;

  factory AllConsoleAppsModel.fromJson(Map<String, dynamic> json) => AllConsoleAppsModel(
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
    required this.consoleUserId,
    required this.user,
    required this.appStatus,
    required this.appInstallationCount,
    required this.updateStatus,
    required this.avreviewstar,
    required this.review,
    required this.appName,
    required this.appCategory,
    required this.appType,
    required this.screenShot,
    required this.apkInformation,
    required this.appFullDescription,
    required this.appShortDescription,
    required this.appGraphic,
    required this.appIcon,
    required this.createdDate,
  });

  String id;
  String consoleUserId;
  String user;
  int appStatus;
  int appInstallationCount;
  int updateStatus;
  int avreviewstar;
  List<Review> review;
  String appName;
  String appCategory;
  String appType;
  List<ScreenShot> screenShot;
  List<ApkInformation> apkInformation;
  String appFullDescription;
  String appShortDescription;
  String appGraphic;
  String appIcon;
  String createdDate;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    id: json["id"],
    consoleUserId: json["console_user_id"],
    user: json["user"],
    appStatus: json["app_status"],
    appInstallationCount: json["app_installation_count"],
    updateStatus: json["update_status"],
    avreviewstar: json["Avreviewstar"],
    review: List<Review>.from(json["review"].map((x) => Review.fromJson(x))),
    appName: json["app_name"],
    appCategory: json["app_category"],
    appType: json["app_type"],
    screenShot: List<ScreenShot>.from(json["screen_shot"].map((x) => ScreenShot.fromJson(x))),
    apkInformation: List<ApkInformation>.from(json["apk_information"].map((x) => ApkInformation.fromJson(x))),
    appFullDescription: json["app_full_description"],
    appShortDescription: json["app_short_description"],
    appGraphic: json["app_graphic"],
    appIcon: json["app_icon"],
    createdDate: json["created_date"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "console_user_id": consoleUserId,
    "user": user,
    "app_status": appStatus,
    "app_installation_count": appInstallationCount,
    "update_status": updateStatus,
    "Avreviewstar": avreviewstar,
    "review": List<dynamic>.from(review.map((x) => x.toJson())),
    "app_name": appName,
    "app_category": appCategory,
    "app_type": appType,
    "screen_shot": List<dynamic>.from(screenShot.map((x) => x.toJson())),
    "apk_information": List<dynamic>.from(apkInformation.map((x) => x.toJson())),
    "app_full_description": appFullDescription,
    "app_short_description": appShortDescription,
    "app_graphic": appGraphic,
    "app_icon": appIcon,
    "created_date": createdDate,
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

class Review {
  Review({
    required this.id,
    required this.ratings,
    required this.userName,
    required this.review,
    required this.dateTime,
    required this.userImage,
    required this.replay,
  });

  String id;
  int ratings;
  String userName;
  String review;
  String dateTime;
  String userImage;
  List<Replay> replay;

  factory Review.fromJson(Map<String, dynamic> json) => Review(
    id: json["id"],
    ratings: json["ratings"],
    userName: json["user_name"],
    review: json["review"],
    dateTime: json["date_time"],
    userImage: json["user_image"],
    replay: List<Replay>.from(json["replay"].map((x) => Replay.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "ratings": ratings,
    "user_name": userName,
    "review": review,
    "date_time": dateTime,
    "user_image": userImage,
    "replay": List<dynamic>.from(replay.map((x) => x.toJson())),
  };
}

class Replay {
  Replay({
    required this.id,
    required this.userName,
    required this.replay,
    required this.dateTime,
    required this.userImage,
  });

  String id;
  String userName;
  String replay;
  String dateTime;
  String userImage;

  factory Replay.fromJson(Map<String, dynamic> json) => Replay(
    id: json["id"],
    userName: json["user_name"],
    replay: json["replay"],
    dateTime: json["date_time"],
    userImage: json["user_image"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "user_name": userName,
    "replay": replay,
    "date_time": dateTime,
    "user_image": userImage,
  };
}

enum UserName { SHAHRUKH, SHAHRUKH_KHAN }



class ScreenShot {
  ScreenShot({
    required this.filename,
  });

  String filename;

  factory ScreenShot.fromJson(Map<String, dynamic> json) => ScreenShot(
    filename: json["filename"],
  );

  Map<String, dynamic> toJson() => {
    "filename": filename,
  };
}


