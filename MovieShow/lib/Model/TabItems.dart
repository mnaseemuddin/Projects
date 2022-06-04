



class TabItems{
  late String CategoryID;
  late String CategoryName;

  TabItems({required this.CategoryID,required this.CategoryName});


  factory TabItems.fromJson(Map<String,dynamic>json){
    return TabItems(CategoryID: json["Category_id"], CategoryName: json["Category_name"]);
  }
}