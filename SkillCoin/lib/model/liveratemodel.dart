// To parse this JSON data, do
//
//     final liveRateModel = liveRateModelFromJson(jsonString);

import 'dart:convert';

LiveRateModel liveRateModelFromJson(String str) => LiveRateModel.fromJson(json.decode(str));

String liveRateModelToJson(LiveRateModel data) => json.encode(data.toJson());

class LiveRateModel {
  LiveRateModel({
    required this.status,
    required this.data,
  });

  int status;
  Data data;

  factory LiveRateModel.fromJson(Map<String, dynamic> json) => LiveRateModel(
    status: json["status"],
    data: Data.fromJson(json["data"]),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "data": data.toJson(),
  };
}

class Data {
  Data({
    required this.id,
    required this.liveRate,
    required this.createdAt,
    required this.updatedAt,
    required this.v,
  });

  String id;
  int liveRate;
  DateTime createdAt;
  DateTime updatedAt;
  int v;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
    id: json["_id"],
    liveRate: json["live_rate"],
    createdAt: DateTime.parse(json["created_at"]),
    updatedAt: DateTime.parse(json["updated_at"]),
    v: json["__v"],
  );

  Map<String, dynamic> toJson() => {
    "_id": id,
    "live_rate": liveRate,
    "created_at": createdAt.toIso8601String(),
    "updated_at": updatedAt.toIso8601String(),
    "__v": v,
  };
}
