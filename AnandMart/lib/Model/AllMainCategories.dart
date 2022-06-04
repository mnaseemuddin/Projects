




class AllMainCategories{
late String MainCategoryID;
late String MainParentID;
late String MainCategoryName;
late String MainCategoryDescription;
late String MainCategoryImageUrl;
late String MainCategoryStatus;
late String MainCategoryType;

AllMainCategories({required this.MainCategoryID,required this.MainParentID,
     required this.MainCategoryName,required this.MainCategoryDescription,
required this.MainCategoryImageUrl,required this.MainCategoryStatus,required this.MainCategoryType});

 factory AllMainCategories.formJson(Map<String,dynamic>json){
  return AllMainCategories(MainCategoryID: json["id"], MainParentID: json["parent_id"],
      MainCategoryName: json["name"], MainCategoryDescription: json["category_description"], MainCategoryImageUrl:
      json["base_url"]+json["image"], MainCategoryStatus: json["status"],
      MainCategoryType: json["cat_type"]);
 }
}