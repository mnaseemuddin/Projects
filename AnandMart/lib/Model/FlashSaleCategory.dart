




class FlashSaleCategory{

  late String pid;
  late  String categoryId;
  late String catname;
  late String catParent;
  late  String pimage;
  late String productName;
  late String productSDesc;
  late String productType;
  late String brandId;
  late String sortWeightId;
  late String sortWeight;
  late String sortSPrice;
  late  String sortPColor;
  late String sortPPrice;
  late String pricequantity;
  late String pWeight;
  late String sPrice;
  late String pPrice;
  late String redemPrice;
  late String weightId;
  late String discount;
  late String discountType;
  late String stockType;
  late String stockValue;
  late String qnt;
  late String vendorName;
  late String vendorId;

  FlashSaleCategory(
      {required this.pid,
        required this.categoryId,
        required this.catname,
        required this.catParent,
        required this.pimage,
        required this.productName,
        required this.productSDesc,
        required this.productType,
        required this.brandId,
        required this.sortWeightId,
        required this.sortWeight,
        required  this.sortSPrice,
        required  this.sortPColor,
        required this.sortPPrice,
        required  this.pricequantity,
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
        required this.vendorId});

  factory FlashSaleCategory.fromJson(Map<String, dynamic> json) {
   return FlashSaleCategory(
       pid : json['pid'],
       categoryId : json['category_id'],
       catname : json['catname'],
       catParent : json['cat_parent'],
       pimage : json['pimage'],
       productName : json['product_name'],
       productSDesc : json['product_s_desc'],
       productType : json['product_type'],
       brandId : json['brand_id'],
       sortWeightId : json['sort_weight_id'],
       sortWeight : json['sort_weight'],
       sortSPrice : json['sort_s_price'],
       sortPColor : json['sort_p_color'],
       sortPPrice : json['sort_p_price'],
       pricequantity : json['pricequantity'],
       pWeight : json['p_weight'],
       sPrice : json['s_price'],
       pPrice : json['p_price'],
       redemPrice : json['redem_price'],
       weightId : json['weight_id'],
       discount : json['discount'],
       discountType : json['discount_type'],
       stockType : json['stock_type'],
       stockValue : json['stock_value'],
       qnt : json['qnt'],
       vendorName : json['vendor_name'],
       vendorId : json['vendor_id']
   );
  }
}