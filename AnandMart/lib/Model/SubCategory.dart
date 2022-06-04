



class SubCategory{
  String id;
  String name;
  String noOfProducts;
  String description;
  String image;
  String catStatus;
  String catId;

  SubCategory(
      {required this.id,
        required this.name,
        required this.noOfProducts,
        required this.description,
        required this.image,
        required this.catStatus,
        required this.catId});

  factory SubCategory.formJson(Map<String,dynamic>json){
    return SubCategory(id: json["id"], name: json["name"], noOfProducts: json["no_of_products"],
        description: json["description"], image: json["image"], catStatus: json["cat_status"],
        catId: json["cat_id"]);
  }
}