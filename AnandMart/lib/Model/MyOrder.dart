

import 'package:anandmart/Model/MyAddress.dart';

class MyOrder{
String orderno;
  String totalitems;
  String orderDate;
  String orderTime;
  String totalamount;
  String shippingCharges;
  String totaldiscount;
  String moneypay;
  String orderStatus;
  bool IsVisiblityCancelOrder;
  double OrderUIMinHeight;

MyOrder({required this.orderno,
      required this.totalitems,
      required this.orderDate,
      required this.orderTime,
      required this.totalamount,
      required this.shippingCharges,
      required this.totaldiscount,
      required this.moneypay,
      required this.orderStatus,
required this.IsVisiblityCancelOrder,required this.OrderUIMinHeight});


  factory MyOrder.formJson(Map<String,dynamic>json){
    return MyOrder(orderno: json["orderno"], totalitems: json["totalitems"], orderDate: json["order_date"],
     orderTime: json["order_time"], totalamount: json["totalamount"], shippingCharges: json["shipping_charges"], 
     totaldiscount: json["totaldiscount"], moneypay: json["moneypay"], orderStatus: json["order_status"],
    IsVisiblityCancelOrder: false,OrderUIMinHeight: 140);
  }


}