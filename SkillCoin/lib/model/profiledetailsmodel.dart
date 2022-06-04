// To parse this JSON data, do
//
//     final profileDetailsModel = profileDetailsModelFromJson(jsonString);

import 'dart:convert';

ProfileDetailsModel profileDetailsModelFromJson(String str) => ProfileDetailsModel.fromJson(json.decode(str));

String profileDetailsModelToJson(ProfileDetailsModel data) => json.encode(data.toJson());

class ProfileDetailsModel {
  ProfileDetailsModel({
    required this.status,
    required this.message,
    required this.data,
  });

  int status;
  String message;
  List<Datum> data;

  factory ProfileDetailsModel.fromJson(Map<String, dynamic> json) => ProfileDetailsModel(
    status: json["status"],
    message: json["message"],
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum {
  Datum({
    required this.name,
    required this.email,
    required this.walletAmount,
    required this.resultsImage,
    required this.skillCoinAddress,
    required this.tronAddress
  });

  String name;
  String email;
  double walletAmount;
  String resultsImage;
  String skillCoinAddress;
  String tronAddress;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    name: json["name"],
    email: json["email"],
    walletAmount: json["wallet_amount"].toDouble(),
    resultsImage: json["results_image"],
    skillCoinAddress: json["SkillCoinAddress"],
    tronAddress: json["TronAddress"]
  );

  Map<String, dynamic> toJson() => {
    "name": name,
    "email": email,
    "wallet_amount": walletAmount,
    "results_image": resultsImage,
    "SkillCoinAddress":skillCoinAddress,
    "TronAddress":tronAddress
  };
}
