// To parse this JSON data, do
//
//     final chooseSketchModel = chooseSketchModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

ChooseSketchModel chooseSketchModelFromJson(String str) => ChooseSketchModel.fromJson(json.decode(str));

String chooseSketchModelToJson(ChooseSketchModel data) => json.encode(data.toJson());

class ChooseSketchModel {
  ChooseSketchModel({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Category> data;

  factory ChooseSketchModel.fromJson(Map<String, dynamic> json) => ChooseSketchModel(
    status: json["status"],
    message: json["message"],
    data: List<Category>.from(json["data"].map((x) => Category.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Category {
  Category({
    required this.tblCategoryId,
    required this.categoryName,
    required this.categoryImage,
  });

  final int tblCategoryId;
  final String categoryName;
  final String categoryImage;

  factory Category.fromJson(Map<String, dynamic> json) => Category(
    tblCategoryId: json["tbl_category_id"],
    categoryName: json["category_name"],
    categoryImage: json["category_image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_category_id": tblCategoryId,
    "category_name": categoryName,
    "category_image": categoryImage,
  };
}


//
// import 'dart:convert';
// ChooseSketchModel chooseSketchModel(String str)=>ChooseSketchModel.fromJson(json.decode(str));
// String chooseSketchtoJson(ChooseSketchModel data)=>json.encode(data.toJson());
//
//
//
// class ChooseSketchModel {
//   String status;
//   String message;
//   List<Datum> data;
//
//   ChooseSketchModel({required this.status, required this.message, required this.data});
//
//  factory ChooseSketchModel.fromJson(Map<String, dynamic> json) =>ChooseSketchModel(status: json['status'],
//      message: json['message'], data: List<Datum>.from(json["data"].map((x)=>Datum.fromJson(x))));
//
//   Map<String, dynamic> toJson() {
//     final Map<String, dynamic> data = new Map<String, dynamic>();
//     data['status'] = this.status;
//     data['message'] = this.message;
//     if (this.data != null) {
//       data['data'] = this.data.map((v) => v.toJson()).toList();
//     }
//     return data;
//   }
// }
//
// class Datum {
//   int tblCategoryId;
//   String categoryName;
//   String categoryImage;
//
//   Datum({required this.tblCategoryId, required this.categoryName, required this.categoryImage});
//
//   factory Datum.fromJson(Map<String, dynamic> json) =>Datum(tblCategoryId: json['tbl_category_id'],
//       categoryName: json['category_name'], categoryImage: json['category_image']);
//
//   Map<String, dynamic> toJson() {
//     final Map<String, dynamic> data = new Map<String, dynamic>();
//     data['tbl_category_id'] = this.tblCategoryId;
//     data['category_name'] = this.categoryName;
//     data['category_image'] = this.categoryImage;
//     return data;
//   }
// }