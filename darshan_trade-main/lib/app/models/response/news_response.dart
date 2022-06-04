// To parse this JSON data, do
//
//     final newsResponse = newsResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<NewsResponse> newsResponseFromJson(String str) => List<NewsResponse>.from(json.decode(str).map((x) => NewsResponse.fromJson(x)));

String newsResponseToJson(List<NewsResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class NewsResponse {
  NewsResponse({
    required this.image,
    required this.heading,
    required this.news,
  });

  final String image;
  final String heading;
  final String news;

  factory NewsResponse.fromJson(Map<String, dynamic> json) => NewsResponse(
    image: json["image"],
    heading: json["heading"],
    news: json["news"],
  );

  Map<String, dynamic> toJson() => {
    "image": image,
    "heading": heading,
    "news": news,
  };
}
