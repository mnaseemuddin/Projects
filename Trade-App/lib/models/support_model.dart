// To parse this JSON data, do
//
//     final supportModel = supportModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

SupportModel supportModelFromJson(String str) => SupportModel.fromJson(json.decode(str));

String supportModelToJson(SupportModel data) => json.encode(data.toJson());

class SupportModel {
  SupportModel({
    required this.status,
    required this.message,
    required this.email,
    required this.youtube,
    required this.facebook,
    required this.instagram,
    required this.twitter,
    required this.telegram,
    required this.contactNo,
  });

  final String status;
  final String message;
  final String email;
  final String youtube;
  final String facebook;
  final String instagram;
  final String telegram;
  final String twitter;
  final List<ContactNo> contactNo;

  factory SupportModel.fromJson(Map<String, dynamic> json) => SupportModel(
    status: json["status"],
    message: json["message"],
    email: json["Email"],
    youtube: json["youtube"],
    facebook: json["facebook"],
    instagram: json['Instagram'],
    twitter: json["Twitter"],
    telegram: json["telegram"],
    contactNo: List<ContactNo>.from(json["contact_no"].map((x) => ContactNo.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "Email": email,
    "youtube": youtube,
    "facebook": facebook,
    "Instagram":instagram,
    "telegram": telegram,
    "Twitter":twitter,
    "contact_no": List<dynamic>.from(contactNo.map((x) => x.toJson())),
  };
}

class ContactNo {
  ContactNo({
    required this.tblInformationId,
    required this.locationName,
    required this.contactNumber,
  });

  final int tblInformationId;
  final String locationName;
  final String contactNumber;

  factory ContactNo.fromJson(Map<String, dynamic> json) => ContactNo(
    tblInformationId: json["tbl_information_id"],
    locationName: json["location_name"],
    contactNumber: json["contact_number"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_information_id": tblInformationId,
    "location_name": locationName,
    "contact_number": contactNumber,
  };
}
