// To parse this JSON data, do
//
//     final categoryWiseAttribute = categoryWiseAttributeFromJson(jsonString);

import 'package:flutter/cupertino.dart';
import 'package:meta/meta.dart';
import 'dart:convert';

import 'package:tailor/Model/model.dart';

CategoryWiseAttribute categoryWiseAttributeFromJson(String str) => CategoryWiseAttribute.fromJson(json.decode(str));

String categoryWiseAttributeToJson(CategoryWiseAttribute data) => json.encode(data.toJson());

class CategoryWiseAttribute {
  CategoryWiseAttribute({
    required this.status,
    required this.message,
    required this.canvasWidth,
    required this.canvasHeight,
    required this.data,
    required this.texture,
    required this.attributeColor,
    required this.specsDatum,
    required this.easeDatum
  });

  final String status;
  final String message;
  final double canvasWidth;
  final double canvasHeight;
  final List<Attribute> data;
  final List<SATexture> texture;
  final List<AttributeColor> attributeColor;
  final List<SpecsDatum>specsDatum;
  final List<EaseDatum>easeDatum;
  factory CategoryWiseAttribute.fromJson(Map<String, dynamic> json) => CategoryWiseAttribute(
    status: json["status"],
    message: json["message"],
    canvasWidth: double.parse(json["canvas_width"]),
    canvasHeight: double.parse(json["canvas_height"]),
    data: List<Attribute>.from(json["data"].map((x) => Attribute.fromJson(x))),
    texture: List<SATexture>.from(json["texture"].map((x) => SATexture.fromJson(x))),
    attributeColor: List<AttributeColor>.from(json["attribute_color"].map((x) =>
        AttributeColor.fromJson(x))),
    specsDatum: List<SpecsDatum>.from(json["specs_data"].map((x) =>
        SpecsDatum.fromJson(x))),
    easeDatum: List<EaseDatum>.from(json["ease_data"].map((x) =>
        EaseDatum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "canvas_width": canvasWidth,
    "canvas_height": canvasHeight,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
    "texture": List<dynamic>.from(texture.map((x) => x.toJson())),
    "attribute_color": List<dynamic>.from(attributeColor.map((x) => x.toJson())),
    "specs_data": List<dynamic>.from(specsDatum.map((x) => x.toJson())),
  };
}

class AttributeColor {
  AttributeColor({
    required this.tblAttributeColorId,
    required this.catId,
    required this.colorCode,
    required this.colorName,
    required this.selectedColor,
    required this.selectedColorName

  });

  final int tblAttributeColorId;
  final int catId;
  final String colorCode;
  final String colorName;
  String selectedColor;
  String selectedColorName;

  factory AttributeColor.fromJson(Map<String, dynamic> json) => AttributeColor(
    tblAttributeColorId: json["tbl_attribute_color_id"],
    catId: json["cat_id"],
    colorCode: json["color_code"],
    colorName: json["color_name"],
    selectedColor: "null",
    selectedColorName: "null"
  );

  Map<String, dynamic> toJson() => {
    "tbl_attribute_color_id": tblAttributeColorId,
    "cat_id": catId,
    "color_code": colorCode,
    "color_name":colorName
  };
}

class Attribute {
  Attribute({
    required this.tblAttributeId,
    required this.categoryId,
    required this.attributeName,
    required this.zIndex,
    required this.image,
    required this.subAttributeData,
    required this.selectedImage,
    required this.selectedName
  });

  final int tblAttributeId;
  final int categoryId;
  final String attributeName;
  final int zIndex;
  String image;
  String selectedImage;
  String selectedName;
  final List<SubAttributeData> subAttributeData;

  factory Attribute.fromJson(Map<String, dynamic> json) => Attribute(
    tblAttributeId: json["tbl_attribute_id"],
    categoryId: json["category_id"],
    attributeName: json["attribute_name"],
    zIndex: json["z_index"],
    image: json["image"],
    selectedImage: "null",
    selectedName: 'null',
    subAttributeData: List<SubAttributeData>.from(json["sub_attribute_data"].map((x) => SubAttributeData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "tbl_attribute_id": tblAttributeId,
    "category_id": categoryId,
    "attribute_name": attributeName,
    "z_index": zIndex,
    "image": image,
    "sub_attribute_data": List<dynamic>.from(subAttributeData.map((x) => x.toJson())),
  };
}

class SubAttributeData {
  SubAttributeData({
    required this.tblSubAttributeId,
    required this.attributeId,
    required this.top,
    required this.left,
    required this.width,
    required this.height,
    required this.subAttributeName,
    required this.image,
  });

  final int tblSubAttributeId;
  final int attributeId;
  final double top;
  final double left;
  final double width;
  final double height;
  final String subAttributeName;
  final String image;

  factory SubAttributeData.fromJson(Map<String, dynamic> json) => SubAttributeData(
    tblSubAttributeId: json["tbl_sub_attribute_id"],
    attributeId: json["attribute_id"],
    top:    json["top"]    is double ? json["top"]    : double.parse(json["top"]),
    left:   json["left"]   is double ? json["left"]   : double.parse(json["left"]),
    width:  json["width"]  is double ? json["width"]  : double.parse(json["width"]),
    height: json["height"] is double ? json["height"] : double.parse(json["height"]),
    subAttributeName: json["sub_attribute_name"],
    image: json["image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_sub_attribute_id": tblSubAttributeId,
    "attribute_id": attributeId,
    "top": top,
    "left": left,
    "width": width,
    "height": height,
    "sub_attribute_name": subAttributeName,
    "image": image,
  };
}

class SATexture {
  SATexture({
    required this.tblAttributeTextureId,
    required this.catId,
    required this.textureName,
    required this.textureImage,
    required this.selectedtextureImage,
    required this.selectedtextureName
  });

  final int tblAttributeTextureId;
  final int catId;
  final String textureName;
  final String textureImage;
  String selectedtextureImage;
  String selectedtextureName;

  factory SATexture.fromJson(Map<String, dynamic> json) => SATexture(
    tblAttributeTextureId: json["tbl_attribute_texture_id"],
    catId: json["cat_id"],
    textureName: json["texture_name"],
    textureImage: json["texture_image"],
    selectedtextureName: "null",
    selectedtextureImage: "null"
  );

  Map<String, dynamic> toJson() => {
    "tbl_attribute_texture_id": tblAttributeTextureId,
    "cat_id": catId,
    "texture_name": textureName,
    "texture_image": textureImage,
  };
}


class SpecsDatum {
  SpecsDatum({
    required this.tblSpecsId,
    required this.pointOfMeasure,
    required this.measure,
    required this.categoryId,
  });

  int tblSpecsId;
  late String pointOfMeasure;
  String measure;
  int categoryId;

  factory SpecsDatum.fromJson(Map<String, dynamic> json) => SpecsDatum(
    tblSpecsId: json["tbl_specs_id"]==null?0:json["tbl_specs_id"],
    pointOfMeasure: json["point_of_measure"]==null?"":json["point_of_measure"],
    measure: json["measure"]==null?"":json["measure"],
    categoryId: json["category_id"]==null?0:json["category_id"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_specs_id": tblSpecsId,
    "point_of_measure": pointOfMeasure,
    "measure": measure,
    "category_id": categoryId,
  };
}



class EaseDatum {
  EaseDatum({
    required this.tblEaseId,
    required this.pointOfMeasure,
    required this.ease,
    required this.categoryId,
    required this.tvcontroller
  });

  int tblEaseId;
  String pointOfMeasure;
  String ease;
  int categoryId;
  TextEditingController tvcontroller;

  factory EaseDatum.fromJson(Map<String, dynamic> json) => EaseDatum(
    tblEaseId: json["tbl_ease_id"],
    pointOfMeasure: json["point_of_measure"],
    ease: json["ease"],
    categoryId: json["category_id"],
    tvcontroller: TextEditingController()

  );

  Map<String, dynamic> toJson() => {
    "tbl_ease_id": tblEaseId,
    "point_of_measure": pointOfMeasure,
    "ease": ease,
    "category_id": categoryId,
  };
}


/**
import 'dart:ffi';

import 'package:meta/meta.dart';
import 'dart:convert';

CategoryWiseAttribute categoryWiseAttributeFromJson(String str) => CategoryWiseAttribute.fromJson(json.decode(str));

String categoryWiseAttributeToJson(CategoryWiseAttribute data) => json.encode(data.toJson());

class CategoryWiseAttribute {
  CategoryWiseAttribute({
    required this.status,
    required this.message,
    required this.canvasWidth,
    required this.canvasHeight,
    required this.data,
  });

  final String status;
  final String message;
  final double canvasWidth;
  final double canvasHeight;
  final List<Attribute> data;

  factory CategoryWiseAttribute.fromJson(Map<String, dynamic> json) => CategoryWiseAttribute(
    status: json["status"],
    message: json["message"],
    canvasWidth: double.parse(json["canvas_width"]),
    canvasHeight: double.parse(json["canvas_height"]),
    data: List<Attribute>.from(json["data"].map((x) => Attribute.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "canvas_width": canvasWidth,
    "canvas_height": canvasHeight,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Attribute {
  Attribute({
    required this.tblAttributeId,
    required this.categoryId,
    required this.attributeName,
    required this.zIndex,
    required this.image,
    required this.subAttributeData,
  });

  final int tblAttributeId;
  final int categoryId;
  final String attributeName;
  final int zIndex;
  final String image;
  final List<SubAttributeData> subAttributeData;

  factory Attribute.fromJson(Map<String, dynamic> json) => Attribute(
    tblAttributeId: json["tbl_attribute_id"],
    categoryId: json["category_id"],
    attributeName: json["attribute_name"],
    zIndex: json["z_index"],
    image: json["image"],
    subAttributeData: List<SubAttributeData>.from(json["sub_attribute_data"].map((x) => SubAttributeData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "tbl_attribute_id": tblAttributeId,
    "category_id": categoryId,
    "attribute_name": attributeName,
    "z_index": zIndex,
    "image": image,
    "sub_attribute_data": List<dynamic>.from(subAttributeData.map((x) => x.toJson())),
  };
}

class SubAttributeData {
  SubAttributeData({
    required this.tblSubAttributeId,
    required this.attributeId,
    required this.top,
    required this.left,
    required this.width,
    required this.height,
    required this.subAttributeName,
    required this.image,
  });

  final int tblSubAttributeId;
  final int attributeId;
  final double top;
  final double left;
  final double width;
  final double height;
  final String subAttributeName;
  final String image;

  factory SubAttributeData.fromJson(Map<String, dynamic> json) => SubAttributeData(
    tblSubAttributeId: json["tbl_sub_attribute_id"],
    attributeId: json["attribute_id"],
    top: double.parse(json["top"]),
    left: double.parse(json["left"]),
    width: double.parse(json["width"]),
    height: double.parse(json["height"]),
    subAttributeName: json["sub_attribute_name"],
    image: json["image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_sub_attribute_id": tblSubAttributeId,
    "attribute_id": attributeId,
    "top": top,
    "left": left,
    "width": width,
    "height": height,
    "sub_attribute_name": subAttributeName,
    "image": image,
  };
}

**/

/**
import 'package:meta/meta.dart';
import 'dart:convert';

CategoryWiseAttribute categoryWiseAttributeFromJson(String str) => CategoryWiseAttribute.fromJson(json.decode(str));

String categoryWiseAttributeToJson(CategoryWiseAttribute data) => json.encode(data.toJson());

class CategoryWiseAttribute {
  CategoryWiseAttribute({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Attribute> data;

  factory CategoryWiseAttribute.fromJson(Map<String, dynamic> json) => CategoryWiseAttribute(
    status: json["status"],
    message: json["message"],
    data: List<Attribute>.from(json["data"].map((x) => Attribute.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Attribute {
  Attribute({
    required this.tblAttributeId,
    required this.categoryId,
    required this.attributeName,
    required this.zIndex,
    required this.topMarging,
    required this.leftMargin,
    required this.image,
    required this.subAttributeData,
  });

  final int tblAttributeId;
  final int categoryId;
  final String attributeName;
  final int zIndex;
  final double topMarging;
  final double leftMargin;
  final String image;
  final List<SubAttributeData> subAttributeData;

  factory Attribute.fromJson(Map<String, dynamic> json) => Attribute(
    tblAttributeId: json["tbl_attribute_id"],
    categoryId: json["category_id"],
    attributeName: json["attribute_name"],
    zIndex: json["z_index"],
    topMarging: double.parse(json["top_marging"]),
    leftMargin: double.parse(json["left_margin"]),
    image: json["image"],
    subAttributeData: List<SubAttributeData>.from(json["sub_attribute_data"].map((x) => SubAttributeData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "tbl_attribute_id": tblAttributeId,
    "category_id": categoryId,
    "attribute_name": attributeName,
    "z_index": zIndex,
    "top_marging": topMarging,
    "left_margin": leftMargin,
    "image": image,
    "sub_attribute_data": List<dynamic>.from(subAttributeData.map((x) => x.toJson())),
  };
}

class SubAttributeData {
  SubAttributeData({
    required this.tblSubAttributeId,
    required this.attributeId,
    required this.subAttributeName,
    required this.image,
  });

  final int tblSubAttributeId;
  final int attributeId;
  final String subAttributeName;
  final String image;

  factory SubAttributeData.fromJson(Map<String, dynamic> json) => SubAttributeData(
    tblSubAttributeId: json["tbl_sub_attribute_id"],
    attributeId: json["attribute_id"],
    subAttributeName: json["sub_attribute_name"],
    image: json["image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_sub_attribute_id": tblSubAttributeId,
    "attribute_id": attributeId,
    "sub_attribute_name": subAttributeName,
    "image": image,
  };
}

**/
// To parse this JSON data, do
//
//     final categoryWiseAttribute = categoryWiseAttributeFromJson(jsonString);
/**
import 'package:meta/meta.dart';
import 'dart:convert';

CategoryWiseAttribute categoryWiseAttributeFromJson(String str) => CategoryWiseAttribute.fromJson(json.decode(str));

String categoryWiseAttributeToJson(CategoryWiseAttribute data) => json.encode(data.toJson());

class CategoryWiseAttribute {
  CategoryWiseAttribute({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Attribute> data;

  factory CategoryWiseAttribute.fromJson(Map<String, dynamic> json) => CategoryWiseAttribute(
    status: json["status"],
    message: json["message"],
    data: List<Attribute>.from(json["data"].map((x) => Attribute.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Attribute {
  Attribute({
    required this.tblAttributeId,
    required this.categoryId,
    required this.attributeName,
    required this.image,
    required this.subAttributeData,
  });

  final int tblAttributeId;
  final int categoryId;
  final String attributeName;
  final String image;
  final List<SubAttributeData> subAttributeData;

  factory Attribute.fromJson(Map<String, dynamic> json) => Attribute(
    tblAttributeId: json["tbl_attribute_id"],
    categoryId: json["category_id"],
    attributeName: json["attribute_name"],
    image: json["image"],
    subAttributeData: List<SubAttributeData>.from(json["sub_attribute_data"].map((x) => SubAttributeData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "tbl_attribute_id": tblAttributeId,
    "category_id": categoryId,
    "attribute_name": attributeName,
    "image": image,
    "sub_attribute_data": List<dynamic>.from(subAttributeData.map((x) => x.toJson())),
  };
}

class SubAttributeData {
  SubAttributeData({
    required this.tblSubAttributeId,
    required this.attributeId,
    required this.subAttributeName,
    required this.image,
  });

  final int tblSubAttributeId;
  final int attributeId;
  final String subAttributeName;
  final String image;

  factory SubAttributeData.fromJson(Map<String, dynamic> json) => SubAttributeData(
    tblSubAttributeId: json["tbl_sub_attribute_id"],
    attributeId: json["attribute_id"],
    subAttributeName: json["sub_attribute_name"],
    image: json["image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_sub_attribute_id": tblSubAttributeId,
    "attribute_id": attributeId,
    "sub_attribute_name": subAttributeName,
    "image": image,
  };
}
**/