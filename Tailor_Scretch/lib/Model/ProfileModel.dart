


// To parse this JSON data, do
//
//     final profileModel = profileModelFromJson(jsonString);

import 'dart:convert';

ProfileModel profileModelFromJson(String str) => ProfileModel.fromJson(json.decode(str));

String profileModelToJson(ProfileModel data) => json.encode(data.toJson());

class ProfileModel {
  ProfileModel({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<Datum> data;

  factory ProfileModel.fromJson(Map<String, dynamic> json) => ProfileModel(
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
    required this.usersId,
    required this.fName,
    required this.lName,
    required this.email,
    required this.password,
    required this.gender,
    required this.mobile,
    required this.address,
    required this.city,
    required this.state,
    required this.country,
    required this.zipCode,
    required this.userImage,
  });

  int usersId;
  String fName;
  String lName;
  String email;
  String password;
  String gender;
  String mobile;
  String address;
  String city;
  String state;
  String country;
  String zipCode;
  String userImage;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    usersId: json["users_id"],
    fName: json["f_name"],
    lName: json["l_name"],
    email: json["email"],
    password: json["password"],
    gender: json["gender"],
    mobile: json["mobile"],
    address: json["address"],
    city: json["city"],
    state: json["state"],
    country: json["country"],
    zipCode: json["zip_code"],
    userImage: json["user_image"],
  );

  Map<String, dynamic> toJson() => {
    "users_id": usersId,
    "f_name": fName,
    "l_name": lName,
    "email": email,
    "password": password,
    "gender": gender,
    "mobile": mobile,
    "address": address,
    "city": city,
    "state": state,
    "country": country,
    "zip_code": zipCode,
    "user_image": userImage,
  };
}
