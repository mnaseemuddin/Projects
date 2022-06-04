
import 'dart:convert';
// TopCategoryModel topCategoryModelDromJson(String str)=>TopCategoryModel.fromJson(json.decode(str));
// String topCategoryModeltoJson(TopCategoryModel model)=>json.encode(model.toJson());

// class TopCategoryModel {
//   String status;
//   String message;
//   List<Datum> data;
//
//   TopCategoryModel({required this.status, required this.message, required this.data});
//
// factory  TopCategoryModel.fromJson(Map<String, dynamic> json)=>TopCategoryModel(status: json['status'],
//     message: json['message'], data:
//     List<Datum>.from(json['data'].map((x)=>Datum.fromJson(json))));
//
//   Map<String, dynamic> toJson()=>
//       {
//         "status": status,
//         "message": message,
//         "data": List<dynamic>.from(data.map((x) => x.toJson())),
//       };
// }

class TopCategoryModel {
  int tblCategoryId;
  String categoryName;
  String categoryImage;

  TopCategoryModel({required this.tblCategoryId, required this.categoryName, required this.categoryImage});

  factory TopCategoryModel.fromJson(Map<String, dynamic> json) =>TopCategoryModel(
      tblCategoryId: json['tbl_category_id'], categoryName:
  json['category_name'], categoryImage: json['category_image']);

  //
  //
  // Map<String, dynamic> toJson() => {
  //   "tbl_category_id": tblCategoryId,
  //   "category_name": categoryName,
  //   "category_image": categoryImage,
  // };

}