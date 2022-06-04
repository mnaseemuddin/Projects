// To parse this JSON data, do
//
//     final categoryModel = categoryModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

import 'package:movieshow/SAModel/home_data_model.dart';


CategoryModel categoryModelFromJson(String str) => CategoryModel.fromJson(json.decode(str));

String categoryModelToJson(CategoryModel data) => json.encode(data.toJson());

class CategoryModel {
  CategoryModel({
    required this.message,
    required this.status,
    required this.sliderData,
    required this.data,
  });

  final String message;
  final String status;
  final List<SliderData> sliderData;
  final List<CategoryData> data;

  factory CategoryModel.fromJson(Map<String, dynamic> json) => CategoryModel(
    message: json["message"],
    status: json["status"],
    sliderData: List<SliderData>.from(json["slider_data"].map((x) => SliderData.fromJson(x))),
    data: List<CategoryData>.from(json["data"].map((x) => CategoryData.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "message": message,
    "status": status,
    "slider_data": List<dynamic>.from(sliderData.map((x) => x.toJson())),
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class CategoryData {
  CategoryData({
    required this.categoryName,
    required this.categoryId,
    required this.movieData,
  });

  final String categoryName;
  final String categoryId;
  final List<AllMovie> movieData;



  factory CategoryData.fromJson(Map<String, dynamic> json) => CategoryData(
    categoryName: json["Category_name"],
    categoryId: json["Category_id"],
    movieData: List<AllMovie>.from(json["Movie_data"].map((x) => AllMovie.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "Category_name": categoryName,
    "Category_id": categoryId,
    "Movie_data": List<dynamic>.from(movieData.map((x) => x.toJson())),
  };
}

// class AllMovie {
//   AllMovie({
//     required this.tblMovie,
//     required this.categoryName,
//     required this.movieName,
//     required this.thumbnailImage,
//   });
//
//
//   final String tblMovie;
//   final String categoryName;
//   final String movieName;
//   final String thumbnailImage;
//
//   factory AllMovie.fromJson(Map<String, dynamic> json) => AllMovie(
//     tblMovie: json["tbl_movie"],
//     categoryName: json["category_name"],
//     movieName: json["movie_name"],
//     thumbnailImage: json["thumbnail_image"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "tbl_movie": tblMovie,
//     "category_name": categoryName,
//     "movie_name": movieName,
//     "thumbnail_image": thumbnailImage,
//   };
// }

class SliderData {
  SliderData({
    required this.tblSliderId,
    required this.movieId,
    required this.name,
    required this.image,
  });

  final String tblSliderId;
  final String movieId;
  final String name;
  final String image;

  factory SliderData.fromJson(Map<String, dynamic> json) => SliderData(
    tblSliderId: json["tbl_slider_id"],
    movieId: json["movie_id"],
    name: json["name"],
    image: json["image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_slider_id": tblSliderId,
    "movie_id": movieId,
    "name": name,
    "image": image,
  };
}
