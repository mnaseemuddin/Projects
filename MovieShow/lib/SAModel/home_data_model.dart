// To parse this JSON data, do
//
//     final homeDataModel = homeDataModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

HomeDataModel homeDataModelFromJson(String str) => HomeDataModel.fromJson(json.decode(str));

String homeDataModelToJson(HomeDataModel data) => json.encode(data.toJson());

class HomeDataModel {
  HomeDataModel({
    required this.message,
    required this.status,
    required this.sliderData,
    required this.topMoviesData,
    required this.allCategoryMovies,
  });

  final String message;
  final String status;
  final List<SliderDatum> sliderData;
  final List<TopMoviesDatum> topMoviesData;
  final List<AllCategoryMovie> allCategoryMovies;

  factory HomeDataModel.fromJson(Map<String, dynamic> json) => HomeDataModel(
    message: json["message"],
    status: json["status"],
    sliderData: List<SliderDatum>.from(json["slider_data"].map((x) => SliderDatum.fromJson(x))),
    topMoviesData: List<TopMoviesDatum>.from(json["top_movies_data"].map((x) => TopMoviesDatum.fromJson(x))),
    allCategoryMovies: List<AllCategoryMovie>.from(json["all_category_movies"].map((x) => AllCategoryMovie.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "message": message,
    "status": status,
    "slider_data": List<dynamic>.from(sliderData.map((x) => x.toJson())),
    "top_movies_data": List<dynamic>.from(topMoviesData.map((x) => x.toJson())),
    "all_category_movies": List<dynamic>.from(allCategoryMovies.map((x) => x.toJson())),
  };
}

class AllCategoryMovie {
  AllCategoryMovie({
    required this.title,
    required this.categoryName,
    required this.subCategoryId,
    required this.allMovies,
  });

  final String? title;
  final String? categoryName;
  final String? subCategoryId;
  final List<AllMovie> allMovies;

  factory AllCategoryMovie.fromJson(Map<String, dynamic> json) => AllCategoryMovie(
    title: json["title"] == null ? null : json["title"],
    categoryName: json["category_name"] == null ? null : json["category_name"],
    subCategoryId: json["sub_category_id"] == null ? null : json["sub_category_id"],
    allMovies: json["all_movies"] == null ? [] : List<AllMovie>.from(json["all_movies"].map((x) => AllMovie.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "title": title == null ? null : title,
    "category_name": categoryName == null ? null : categoryName,
    "sub_category_id": subCategoryId == null ? null : subCategoryId,
    "all_movies": allMovies == null ? null : List<dynamic>.from(allMovies.map((x) => x.toJson())),
  };
}

class AllMovie {
  AllMovie({
    required this.tblMovie,
    required this.categoryName,
    required this.movieName,
    required this.thumbnailImage,
  });

  final String tblMovie;
  final String categoryName;
  final String movieName;
  final String thumbnailImage;

  factory AllMovie.fromJson(Map<String, dynamic> json) => AllMovie(
    tblMovie: json["tbl_movie"],
    categoryName: json["category_name"],
    movieName: json["movie_name"],
    thumbnailImage: json["thumbnail_image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_movie": tblMovie,
    "category_name": categoryName,
    "movie_name": movieName,
    "thumbnail_image": thumbnailImage,
  };
}

class SliderDatum {
  SliderDatum({
    required this.tblSliderId,
    required this.movieId,
    required this.name,
    required this.image,
  });

  final String tblSliderId;
  final String movieId;
  final String name;
  final String image;

  factory SliderDatum.fromJson(Map<String, dynamic> json) => SliderDatum(
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

class TopMoviesDatum {
  TopMoviesDatum({
    required this.title,
    required this.subCategoryId,
    required this.tblMovieId,
    required this.thumbnailImage,
  });

  final String title;
  final String? subCategoryId;
  final String? tblMovieId;
  final String? thumbnailImage;

  factory TopMoviesDatum.fromJson(Map<String, dynamic> json) => TopMoviesDatum(
    title: json["title"],
    subCategoryId: json["sub_category_id"] == null ? null : json["sub_category_id"],
    tblMovieId: json["tbl_movie_id"] == null ? null : json["tbl_movie_id"],
    thumbnailImage: json["thumbnail_image"] == null ? null : json["thumbnail_image"],
  );

  Map<String, dynamic> toJson() => {
    "title": title,
    "sub_category_id": subCategoryId == null ? null : subCategoryId,
    "tbl_movie_id": tblMovieId == null ? null : tblMovieId,
    "thumbnail_image": thumbnailImage == null ? null : thumbnailImage,
  };
}
