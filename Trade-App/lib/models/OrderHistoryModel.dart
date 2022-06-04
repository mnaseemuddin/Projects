


class OrderHistoryModel{
  int tblOrderId;
  int userId;
  String orderNumber;
  String totalAmount;
  int orderQuantity;
  String orderType;
  String orderDateTime;
  int orderStatus;
  String addressType;
  String name;
  String email;
  String mobile;
  String address;
  String city;
  String country;

  OrderHistoryModel(
      {required this.tblOrderId,
        required this.userId, required this.orderNumber,
        required this.totalAmount, required this.orderQuantity,
        required this.orderType, required this.orderDateTime,
        required this.orderStatus, required this.addressType,
        required this.name, required this.email,
        required this.mobile, required this.address,
        required this.city, required this.country});

  factory OrderHistoryModel.fromJson(Map<String,dynamic>json){
    return OrderHistoryModel(tblOrderId: json["tbl_order_id"], userId: json["user_id"],
        orderNumber: json["order_number"], totalAmount: json["total_amount"], orderQuantity: json["order_quantity"],
        orderType: json["order_type"], orderDateTime: json["order_date_time"], orderStatus: json["order_status"],
        addressType: json["address_type"], name: json["name"], email: json["email"], mobile: json["mobile"], address:
        json["address"], city: json["city"], country: json["country"]);
  }

}