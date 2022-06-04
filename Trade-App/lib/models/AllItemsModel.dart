


class AllItemsModel{

  late int ProductsId;
  late String ProductName;
  late String ProductPrice;
  late String Image;
  late String ProductDescription;
  bool IsVisibilityAddItem;
  bool IsVisibilityAddMinus;
  int finalQty;
  int vendorId;
  String Tdcdetect;

  AllItemsModel({required this.ProductsId, required this.ProductName,
    required this.ProductPrice, required this.Image,required this.ProductDescription,required this.IsVisibilityAddItem
  ,required this.IsVisibilityAddMinus,required this.finalQty,required this.vendorId,required this.Tdcdetect
  });

  factory AllItemsModel.fromJson(Map<String,dynamic>json)=>
      AllItemsModel(ProductsId: json["tbl_products_id"],
          ProductName: json["product_name"], ProductPrice:
          json["product_price"], Image: json["image"],ProductDescription: json["product_description"],
      IsVisibilityAddItem: true,IsVisibilityAddMinus: false,finalQty: json["quantity"],
          vendorId: json['vendor_id'],Tdcdetect: json['tdc_detect']);
}

