
import 'dart:convert';

TrendingDealModel trendingDealModelFromJson(String str) => TrendingDealModel.fromJson(json.decode(str));

String trendingDealModelToJson(TrendingDealModel data) => json.encode(data.toJson());

class TrendingDealModel {
  TrendingDealModel({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<TrendingDatum> data;

  factory TrendingDealModel.fromJson(Map<String, dynamic> json) => TrendingDealModel(
    status: json["status"],
    message: json["message"],
    data: List<TrendingDatum>.from(json["trending_data"].map((x) => TrendingDatum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "trending_data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class TrendingDatum {
  TrendingDatum({
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

  factory TrendingDatum.fromJson(Map<String, dynamic> json) => TrendingDatum(
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

