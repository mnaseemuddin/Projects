



class CategoryModel{
   late int categoryId;
  late String categoryName;
  //late List<SubCategory>subCategoryList;
   bool IsSubCategoryVisibility;

  CategoryModel({required this.categoryId, required this.categoryName,required this.IsSubCategoryVisibility});
}

class SubCategoryModel{
  late int subCateoryId;
   late String subCategoryName;
   late int categoryId;
   bool IsSubCategoryVisibility;

   SubCategoryModel({required this.subCateoryId,required this.subCategoryName,required this.categoryId,
     required this.IsSubCategoryVisibility});
}


class Category1{
  late String CategoryName;
  late String ImageUrls;
  // bool IsSubCategoryVisibility;

  Category1({required this.CategoryName,required this.ImageUrls});
}