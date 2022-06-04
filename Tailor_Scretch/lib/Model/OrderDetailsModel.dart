// To parse this JSON data, do
//
//     final orderDeatilsModel = orderDeatilsModelFromJson(jsonString);

import 'dart:convert';

OrderDeatilsModel orderDeatilsModelFromJson(String str) => OrderDeatilsModel.fromJson(json.decode(str));

String orderDeatilsModelToJson(OrderDeatilsModel data) => json.encode(data.toJson());

class OrderDeatilsModel {
  OrderDeatilsModel({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<Datum> data;

  factory OrderDeatilsModel.fromJson(Map<String, dynamic> json) => OrderDeatilsModel(
    status: json["status"],
    message: json["message"],
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Datum {
  Datum({
    required this.tblOrderId,
    required this.userId,
    required this.orderNumber,
    required this.categoryId,
    required this.selectedAttribute,
    required this.userMesurment,
    required this.orderDate,
    required this.orderStatus,
    required this.addressType,
    required this.name,
    required this.mobile,
    required this.address,
    required this.city,
    required this.state,
    required this.zipcode,
    required this.image,
  });

  int tblOrderId;
  int userId;
  String orderNumber;
  int categoryId;
  String selectedAttribute;
  String userMesurment;
  String orderDate;
  String orderStatus;
  String addressType;
  String name;
  String mobile;
  String address;
  String city;
  String state;
  int zipcode;
  String image;


  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblOrderId: json["tbl_order_id"],
    userId: json["user_id"],
    orderNumber: json["order_number"],
    categoryId: json["category_id"],
    selectedAttribute: json["selected_attribute"],
    userMesurment: json["user_mesurment"],
    orderDate: json["order_date"],
    orderStatus: json["order_status"],
    addressType: json["address_type"],
    name: json["name"],
    mobile: json["mobile"],
    address: json["address"],
    city: json["city"],
    state: json["state"],
    zipcode: json["zipcode"],
    image: json["image"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_order_id": tblOrderId,
    "user_id": userId,
    "order_number": orderNumber,
    "category_id": categoryId,
    "selected_attribute": selectedAttribute,
    "user_mesurment": userMesurment,
    "order_date": orderDate,
    "order_status": orderStatus,
    "address_type": addressType,
    "name": name,
    "mobile": mobile,
    "address": address,
    "city": city,
    "state": state,
    "zipcode": zipcode,
    "image": image,
  };
}
