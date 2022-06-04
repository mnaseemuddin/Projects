// To parse this JSON data, do
//
//     final countryModel = countryModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<CountryModel> countryModelFromJson(String str) => List<CountryModel>.from(json.decode(str).map((x) => CountryModel.fromJson(x)));

String countryModelToJson(List<CountryModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class CountryModel {
  CountryModel({
    required this.phonecode,
    required this.name,
  });

  final int phonecode;
  final String name;

  factory CountryModel.fromJson(Map<String, dynamic> json) => CountryModel(
    phonecode: json["phonecode"],
    name: json["name"],
  );

  Map<String, dynamic> toJson() => {
    "phonecode": phonecode,
    "name": name,
  };
}



// // To parse this JSON data, do
// //
// //     final countryModel = countryModelFromJson(jsonString);
//
// import 'package:meta/meta.dart';
// import 'dart:convert';
//
// CountryModel countryModelFromJson(String str) => CountryModel.fromJson(json.decode(str));
//
// String countryModelToJson(CountryModel data) => json.encode(data.toJson());
//
// class CountryModel {
//   CountryModel({
//     required this.message,
//     required this.status,
//     required this.data,
//   });
//
//   final String message;
//   final String status;
//   final List<Country> data;
//
//   factory CountryModel.fromJson(Map<String, dynamic> json) => CountryModel(
//     message: json["message"],
//     status: json["status"],
//     data: List<Country>.from(json["data"].map((x) => Country.fromJson(x))),
//   );
//
//   Map<String, dynamic> toJson() => {
//     "message": message,
//     "status": status,
//     "data": List<dynamic>.from(data.map((x) => x.toJson())),
//   };
// }
//
// class Country {
//   Country({
//     required this.countriesIsdCode,
//     required this.name,
//     required this.countriesIsoCode,
//     required this.flag,
//   });
//
//   final String countriesIsdCode;
//   final String name;
//   final String countriesIsoCode;
//   final String flag;
//
//   factory Country.fromJson(Map<String, dynamic> json) => Country(
//     countriesIsdCode: json["countries_isd_code"],
//     name: json["name"],
//     countriesIsoCode: json["countries_iso_code"],
//     flag: json["flag"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "countries_isd_code": countriesIsdCode,
//     "name": name,
//     "countries_iso_code": countriesIsoCode,
//     "flag": flag,
//   };
// }
