// To parse this JSON data, do
//
//     final bannerResponse = bannerResponseFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

List<BannerResponse> bannerResponseFromJson(String str) => List<BannerResponse>.from(json.decode(str).map((x) => BannerResponse.fromJson(x)));

String bannerResponseToJson(List<BannerResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class BannerResponse {
  BannerResponse({
    required this.banner,
    required this.url,
  });

  final String banner;
  final String url;

  factory BannerResponse.fromJson(Map<String, dynamic> json) => BannerResponse(
    banner: json["banner"],
    url: json["url"],
  );

  Map<String, dynamic> toJson() => {
    "banner": banner,
    "url": url,
  };
}
