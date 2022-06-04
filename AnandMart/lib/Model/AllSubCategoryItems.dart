


class AllSubCategoryItems{
  String pid;
  String categoryId;
  String catname;
  String catParent;
  String pimage;
  String productName;
  String productSDesc;
  String productType;
  String brandId;
  String sortWeightId;
  String sortWeight;
  String sortSPrice;
  String sortPColor;
  String sortPPrice;
  String pricequantity;
  String pWeight;
  String sPrice;
  String pPrice;
  String redemPrice;
  String weightId;
  String discount;
  String discountType;
  String stockType;
  String stockValue;
  String qnt;
  String vendorName;
  Null vendorId;
  bool IsVisibilityAddItem;
  bool IsVisibilityAddMinus;
  int finalQty;

  AllSubCategoryItems(
      {required this.pid,
        required   this.categoryId,
        required  this.catname,
        required this.catParent,
        required   this.pimage,
        required   this.productName,
        required this.productSDesc,
        required  this.productType,
        required  this.brandId,
        required  this.sortWeightId,
        required  this.sortWeight,
        required this.sortSPrice,
        required this.sortPColor,
        required this.sortPPrice,
        required this.pricequantity,
        required this.pWeight,
        required this.sPrice,
        required this.pPrice,
        required this.redemPrice,
        required this.weightId,
        required this.discount,
        required this.discountType,
        required this.stockType,
        required this.stockValue,
        required this.qnt,
        required this.vendorName,
        required this.vendorId,required this.IsVisibilityAddItem,required this.IsVisibilityAddMinus,
      required this.finalQty});

  factory AllSubCategoryItems.formJson(Map<String,dynamic>json){
    return AllSubCategoryItems(pid: json["pid"], categoryId: json["category_id"], catname: json["catname"],
        catParent: json["cat_parent"], pimage: json["pimage"], productName: json["product_name"],
        productSDesc: json["product_s_desc"], productType: json["product_type"],
        brandId: json["brand_id"], sortWeightId: json["sort_weight_id"],
        sortWeight: json["sort_weight"], sortSPrice: json["sort_s_price"], sortPColor: json["sort_p_color"], sortPPrice:
        json["sort_p_price"], pricequantity: json["pricequantity"],
        pWeight: json["p_weight"], sPrice: json["s_price"], pPrice:
        json["p_price"], redemPrice: json["redem_price"], weightId: json["weight_id"],
        discount: json["discount"], discountType:
        json["discount_type"], stockType: json["stock_type"], stockValue: json["stock_value"],
        qnt: json["qnt"], vendorName: json["vendor_name"],
        vendorId: json["vendor_id"],IsVisibilityAddItem: true,IsVisibilityAddMinus: false,
        finalQty: json["qnt1"]);
  }
}