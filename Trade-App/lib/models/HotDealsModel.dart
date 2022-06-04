//
//
//
// class HotDealsModel{
//   late int tblProductsId;
//   late String ProductName;
//   late String CartAdded;
//   late int Quantity;
//   late String ProductPrice;
//   late String Productdescription;
//   late String ImageUrl;
//   late int vendorId;
//   late String Tdcdetect;
//
//   HotDealsModel({required this.tblProductsId,required this.ProductName,required this.
//   CartAdded,required this.Quantity,required this.ProductPrice,
//     required this.Productdescription,required this.ImageUrl,required this.vendorId,required this.Tdcdetect});
//
//   factory HotDealsModel.fromJson(Map<String,dynamic>json){
//     return HotDealsModel(tblProductsId: json["tbl_products_id"], ProductName: json["product_name"],
//         CartAdded: json["cart_added"], Quantity: json["quantity"], ProductPrice: json["product_price"],
//         Productdescription: json["product_description"], ImageUrl: json["image"],vendorId: json['vendor_id'],
//         Tdcdetect: json['tdc_detect']);
//   }
// }
//
//
//
// class TradingModel{
//   late int tblProductsId;
//   late String ProductName;
//   late String CartAdded;
//   late int Quantity;
//   late String ProductPrice;
//   late String Productdescription;
//   late String ImageUrl;
//   late int vendorId;
//   late String Tdcdetect;
//
//   TradingModel({required this.tblProductsId,required this.ProductName,required this.
//   CartAdded,required this.Quantity,required this.ProductPrice,
//   required this.Productdescription,required this.ImageUrl,required this.vendorId,required this.Tdcdetect});
//
//   factory TradingModel.fromJson(Map<String,dynamic>json){
//     return TradingModel(tblProductsId: json["tbl_products_id"], ProductName: json["product_name"],
//         CartAdded: json["cart_added"], Quantity: json["quantity"], ProductPrice: json["product_price"],
//         Productdescription: json["product_description"], ImageUrl: json["image"],vendorId: json['vendor_id'],
//     Tdcdetect: json['tdc_detect']);
//   }
// }


// To parse this JSON data, do
//
//     final hotDealModel = hotDealModelFromJson(jsonString);

import 'dart:convert';

HotDealModel hotDealModelFromJson(String str) => HotDealModel.fromJson(json.decode(str));

String hotDealModelToJson(HotDealModel data) => json.encode(data.toJson());

class HotDealModel {
  HotDealModel({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<HotDealDatum> data;

  factory HotDealModel.fromJson(Map<String, dynamic> json) => HotDealModel(
    status: json["status"],
    message: json["message"],
    data: List<HotDealDatum>.from(json["hot_deal_data"].map((x) => HotDealDatum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "hot_deal_data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class HotDealDatum {
  HotDealDatum({
    required this.tblProductsId,
    required this.productName,
    required this.cartAdded,
    required this.quantity,
    required this.productPrice,
    required this.productDescription,
    required this.vendorId,
    required this.tdcDetect,
    required this.image,
  });

  int tblProductsId;
  String productName;
  String cartAdded;
  int quantity;
  String productPrice;
  String productDescription;
  int vendorId;
  String tdcDetect;
  String image;

  factory HotDealDatum.fromJson(Map<String, dynamic> json) => HotDealDatum(
    tblProductsId: json["tbl_products_id"],
    productName: json["product_name"],
    cartAdded: json["cart_added"],
    quantity: json["quantity"],
    productPrice: json["product_price"],
    productDescription: json["product_description"],
    vendorId: json["vendor_id"],
    tdcDetect: json["tdc_detect"],
    image: json["image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_products_id": tblProductsId,
    "product_name": productName,
    "cart_added": cartAdded,
    "quantity": quantity,
    "product_price": productPrice,
    "product_description": productDescription,
    "vendor_id": vendorId,
    "tdc_detect": tdcDetect,
    "image": image,
  };
}



// To parse this JSON data, do
//
//     final trendingDealModel = trendingDealModelFromJson(jsonString);

