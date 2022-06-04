
import 'dart:convert';

class FeaturedNewAppsModel{

  late String name;
  late String category;
  late String description;
  late String image;

  FeaturedNewAppsModel({required this.name,required this.category,required this.description,
  required this.image});

}



// To parse this JSON data, do
//
//     final newAppsModel = newAppsModelFromJson(jsonString);

// To parse this JSON data, do
//
//     final newAppsModel = newAppsModelFromJson(jsonString);


NewAppsModel newAppsModelFromJson(String str) => NewAppsModel.fromJson(json.decode(str));

String newAppsModelToJson(NewAppsModel data) => json.encode(data.toJson());

class NewAppsModel {
  NewAppsModel({
    required this.category,
  });

  List<Category> category;

  factory NewAppsModel.fromJson(Map<String, dynamic> json) => NewAppsModel(
    category: List<Category>.from(json["Category"].map((x) => Category.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "Category": List<dynamic>.from(category.map((x) => x.toJson())),
  };
}

class Category {
  Category({
    required this.id,
    required this.name,
  });

  String id;
  String name;

  factory Category.fromJson(Map<String, dynamic> json) => Category(
    id: json["id"],
    name: json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "name": name,
  };
}
