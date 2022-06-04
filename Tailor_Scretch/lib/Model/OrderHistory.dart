// To parse this JSON data, do
//
//     final orderHistory = orderHistoryFromJson(jsonString);

import 'dart:convert';

OrderHistory orderHistoryFromJson(String str) => OrderHistory.fromJson(json.decode(str));

String orderHistoryToJson(OrderHistory data) => json.encode(data.toJson());

class OrderHistory {
  OrderHistory({
    required this.status,
    required this.message,
    required this.data,
  });

  String status;
  String message;
  List<Datum> data;

  factory OrderHistory.fromJson(Map<String, dynamic> json) => OrderHistory(
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
    required this.image,
    required this.astimate_date
  });

  int tblOrderId;
  int userId;
  String orderNumber;
  int categoryId;
  String selectedAttribute;
  String userMesurment;
  String orderDate;
  String orderStatus;
  String image;
  String astimate_date;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    tblOrderId: json["tbl_order_id"],
    userId: json["user_id"],
    orderNumber: json["order_number"],
    categoryId: json["category_id"],
    selectedAttribute: json["selected_attribute"],
    userMesurment: json["user_mesurment"],
    orderDate: json["order_date"],
    orderStatus: json["order_status"],
    image: json["image"],
    astimate_date:json["astimate_date"]
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
    "image": image,
    "astimate_date":astimate_date
  };
}
