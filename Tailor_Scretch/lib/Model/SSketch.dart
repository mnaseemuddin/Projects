// To parse this JSON data, do
//
//     final sSketch = sSketchFromJson(jsonString);

import 'dart:convert';

SSketch sSketchFromJson(String str) => SSketch.fromJson(json.decode(str));

String sSketchToJson(SSketch data) => json.encode(data.toJson());

class SSketch {
  SSketch({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<SDatum> data;

  factory SSketch.fromJson(Map<String, dynamic> json) => SSketch(
    status: json["status"],
    message: json["message"],
    data: List<SDatum>.from(json["data"].map((x) => SDatum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class SDatum {
  SDatum({
    required this.tblUserDesignSaveId,
    required this.categoryId,
    required this.selectedAttribute,
    required this.userMeasurement,
    required this.userId,
    required this.image,
    // ignore: non_constant_identifier_names
    required this.pro_image,
    required this.categoryName,
    // ignore: non_constant_identifier_names
    required this.design_name,
    required this.yardage

  });

  int tblUserDesignSaveId;
  int categoryId;
  String selectedAttribute;
  String userMeasurement;
  int userId;
  String image;
  // ignore: non_constant_identifier_names
  String pro_image;
  String categoryName;
  // ignore: non_constant_identifier_names
  String design_name;
  String yardage;

  factory SDatum.fromJson(Map<String, dynamic> json) => SDatum(
    tblUserDesignSaveId: json["tbl_user_design_save_id"],
    categoryId: json["category_id"],
    selectedAttribute: json["selected_attribute"],
    userMeasurement: json["user_measurement"],
    userId: json["user_id"],
    image: json["image"],
    pro_image: json['pro_image'],
    categoryName: json["category_name"],
    design_name:json["design_name"],
    yardage: json['yardage']
  );

  Map<String, dynamic> toJson() => {
    "tbl_user_design_save_id": tblUserDesignSaveId,
    "category_id": categoryId,
    "selected_attribute": selectedAttribute,
    "user_measurement": userMeasurement,
    "user_id": userId,
    "image": image,
    "pro_image":pro_image,
    "category_name":categoryName,
    "design_name":design_name,
    'yardage':yardage
  };
}
