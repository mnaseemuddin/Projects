import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/rendering.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/models/OrderHistoryModel.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/screens/Shopping/OrderDetailsActivity.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

import '../dashboard.dart';

class OrderHiostoryActivity extends StatefulWidget {
  const OrderHiostoryActivity({Key? key}) : super(key: key);

  @override
  _OrderHiostoryActivityState createState() => _OrderHiostoryActivityState();
}

class _OrderHiostoryActivityState extends State<OrderHiostoryActivity> {
  List<OrderHistoryModel> orderHistoryModelList = [];
  bool IsLoading = true;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context, listen: false).startMonitoring();
    myOrderHistoryAPI();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx, data, child) {
      if (data.isOnline != null) {
        return data.isOnline
            ? Scaffold(
                backgroundColor: Color(0xffF3F3F7),
                appBar: AppBar(
                  backgroundColor: Colors.black,
                  title: Text(
                    "My Order",
                    style: GoogleFonts.aBeeZee(
                        color: Colors.white, fontSize: 15.5),
                  ),
                ),
                body: IsLoading == false
                    ? orderHistoryModelList.isEmpty
                        ? NoRecordsFound()
                        : Container(
                            height: double.infinity,
                            width: double.infinity,
                            child: Padding(
                              padding: const EdgeInsets.all(15.0),
                              child: ListView.builder(
                                  itemCount:
                                      orderHistoryModelList.length == null
                                          ? 0
                                          : orderHistoryModelList.length,
                                  itemBuilder: (context, i) {
                                  /*  // return   Padding(
                                    //   padding: const EdgeInsets.all(8.0),
                                    //   child: GestureDetector(
                                    //     child: Container(
                                    //       decoration: BoxDecoration(
                                    //         color: Colors.white,
                                    //         borderRadius: BorderRadius.circular(10),
                                    //         boxShadow: [
                                    //           BoxShadow(
                                    //             color:  Colors.grey,
                                    //             offset: Offset(4, 3),
                                    //             blurRadius: 9,
                                    //           ),
                                    //         ],
                                    //       ),
                                    //       height: 120,
                                    //       width: double.infinity,
                                    //       child: Padding(
                                    //         padding: const EdgeInsets.only(left:8.0,top:15),
                                    //         child: Column(
                                    //           children: [
                                    //             Row(
                                    //               children: [
                                    //                 Align(
                                    //                   alignment: Alignment.centerLeft,
                                    //                   child: Text("Order #"+orderHistoryModelList[i].orderNumber,
                                    //                     style: GoogleFonts.aBeeZee(
                                    //                         color: Colors.black,
                                    //                         fontSize: 15,
                                    //                         fontWeight: FontWeight.w600),),
                                    //                 ),
                                    //                 Spacer(),
                                    //                 Align(
                                    //                   alignment: Alignment.centerRight,
                                    //                   child: Text("Date & Time : "+orderHistoryModelList[i].orderDateTime,
                                    //                     textAlign: TextAlign.center,overflow: TextOverflow.ellipsis,style:
                                    //                     GoogleFonts.aBeeZee(
                                    //                         color: Colors.black,
                                    //                         fontSize: 13.2,
                                    //                         fontWeight: FontWeight.w600),
                                    //                     ),
                                    //
                                    //                 )
                                    //               ],
                                    //             ),
                                    //             Padding(
                                    //               padding: const EdgeInsets.only(top:8.0),
                                    //               child: Row(
                                    //                 children: [
                                    //                   Align(
                                    //                     alignment: Alignment.centerLeft,
                                    //                     child: Text("Total Cart Item's: "+orderHistoryModelList[i].orderQuantity.toString(),style: GoogleFonts.aBeeZee(
                                    //                         color: Colors.black,
                                    //                         fontSize: 15,
                                    //                         fontWeight: FontWeight.w600),),
                                    //                   ),
                                    //                   Spacer(),
                                    //                   Padding(
                                    //                     padding: const EdgeInsets.only(right:10.0),
                                    //                     child: Text("Status: Order Confirm",style: GoogleFonts.aBeeZee(
                                    //                         color: Colors.black,
                                    //                         fontSize: 15,
                                    //                         fontWeight: FontWeight.w600),),
                                    //                   )
                                    //                 ],
                                    //               ),
                                    //             ),
                                    //
                                    //             Padding(
                                    //               padding: const EdgeInsets.only(top:8.0),
                                    //               child: Row(
                                    //                 children: [
                                    //                   Text("Total: \$ "+orderHistoryModelList[i].totalAmount+".00",style: GoogleFonts.aBeeZee(
                                    //                       color: Colors.black,
                                    //                       fontSize: 15,
                                    //                       fontWeight: FontWeight.w600),),
                                    //                   Spacer(),
                                    //                   Padding(
                                    //                     padding: const EdgeInsets.only(right:8.0),
                                    //                     child: GestureDetector(
                                    //                       onTap: (){
                                    //                         navPush(context, OrderDetailsActivity(OrderNo:orderHistoryModelList[i].orderNumber,DateAndTime:
                                    //                         orderHistoryModelList[i].orderDateTime,));
                                    //                       },
                                    //                       child: Container(
                                    //                         width: 110,
                                    //                         height: 35,
                                    //                         decoration: BoxDecoration(
                                    //                           borderRadius: BorderRadius.circular(10),
                                    //                           color: Colors.black,
                                    //                         ),
                                    //                         child: Center(child: Text("View",style:
                                    //                         GoogleFonts.aBeeZee(color: Colors.white),)),
                                    //                       ),
                                    //                     ),
                                    //                   )
                                    //                 ],
                                    //               ),
                                    //             ),
                                    //
                                    //           ],
                                    //         ),
                                    //       ),
                                    //     ),
                                    //   ),
                                    // );*/
                                    return orderHistoryUI(
                                        orderHistoryModelList[i]);
                                  }),
                            ))
                    : Center(
                        child: Container(
                          padding: EdgeInsets.all(16),
                          decoration: BoxDecoration(
                              color: APP_SECONDARY_COLOR,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(16))),
                          child: CircularProgressIndicator(
                            strokeWidth: 5.5,
                          ),
                        ),
                      ),
              )
            : NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

  myOrderHistoryAPI() async {
    try {
      final response = await http.get(
          Uri.parse(OrderHistoryAPI + userModel!.data.first.userId.toString()),
          headers: headers);
      print(userModel!.data.first.userId.toString());
      Map data = json.decode(response.body);
      print(data.toString());
      if (data["status"] != ERROR) {
        if (!mounted) return;
        setState(() {
          IsLoading = false;
          var res = data["data"] as List;
          orderHistoryModelList =
              res.map((e) => OrderHistoryModel.fromJson(e)).toList();
          // orderHistoryModelList.clear();
        });
      } else {
        Fluttertoast.showToast(msg: data["message"]);
      }
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  Widget orderHistoryUI(OrderHistoryModel orderHistoryModelList) {
    Size size = MediaQuery.of(context).size;
    return Padding(
      padding: const EdgeInsets.all(7.0),
      child: GestureDetector(
        onTap: () {
          navPush(
              context,
              OrderDetailsActivity(
                OrderNo: orderHistoryModelList.orderNumber,
                DateAndTime: orderHistoryModelList.orderDateTime,
              ));
        },
        child: Container(
          height: 110,
          width: double.infinity,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(12),
            color: Colors.white,
          ),
          child: Row(
            children: [
              Align(
                alignment: Alignment.centerLeft,
                child: Container(
                  margin: EdgeInsets.only(left: 10),
                  height: size.height / 18,
                  width: size.width / 8,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(6),
                    color: Colors.black,
                  ),
                  child: Icon(
                    Icons.shopping_cart,
                    color: Colors.white,
                  ),
                ),
              ),
              Container(
                margin: EdgeInsets.only(left: 9),
                height: size.height / 1,
                color: Colors.white,
                width: size.width / 1.5,
                child: Row(
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Padding(
                          padding: const EdgeInsets.only(top: 5.0),
                          child: Text(
                            'Order #' + orderHistoryModelList.orderNumber,
                            style: GoogleFonts.roboto(
                                fontSize: 15,
                                color: Colors.black,
                                fontWeight: FontWeight.w600),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top: 3.0),
                          child: Text('Order Confirmed',
                              style: GoogleFonts.roboto(
                                  fontSize: 15,
                                  color: Colors.green,
                                  fontWeight: FontWeight.w600)),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top: 3.0),
                          child: Text(orderHistoryModelList.orderDateTime,
                              style: GoogleFonts.roboto(
                                  fontSize: 15,
                                  color: Colors.grey[500],
                                  fontWeight: FontWeight.w400)),
                        )
                      ],
                    ),
                    Spacer(),
                    Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Padding(
                          padding: const EdgeInsets.only(right: 8.0),
                          child: Text('\$' + orderHistoryModelList.totalAmount+'.00',
                              style: GoogleFonts.roboto(
                                  fontSize: 15,
                                  color: Colors.black,
                                  fontWeight: FontWeight.w600)),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top: 8.0),
                          child: Container(
                            height: 25,
                            width: 25,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(15),
                              color: Colors.green,
                            ),
                            child: Icon(
                              Icons.check,
                              color: Colors.white,
                              size: 14,
                            ),
                          ),
                        )
                      ],
                    ),
                  ],
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}

class NoRecordsFound extends StatelessWidget {
  const NoRecordsFound({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          SizedBox(
            height: 100,
          ),
          Image.asset(
            "assets/ordernotfound.jpg",
            height: 250,
            width: double.infinity,
            fit: BoxFit.fitHeight,
          ),
          SizedBox(
            height: 30,
          ),
          Text(
            'No orders yet',
            style:
                GoogleFonts.roboto(fontSize: 20, fontWeight: FontWeight.w700),
          ),
          SizedBox(
            height: 15,
          ),
          Padding(
            padding: const EdgeInsets.only(left: 40.0, right: 40.0),
            child: Text(
              "Hit the black button down below to create an order",
              textAlign: TextAlign.center,
              style: GoogleFonts.roboto(
                  fontSize: 16,
                  color: Colors.grey,
                  fontWeight: FontWeight.w500),
            ),
          ),
          SizedBox(
            height: 30,
          ),
          Spacer(),
          SizedBox(
            width: 200,
            child: ElevatedButton(
                style: ButtonStyle(
                    backgroundColor:
                        MaterialStateProperty.all<Color>(Colors.black),
                    foregroundColor:
                        MaterialStateProperty.all<Color>(Colors.black),
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(10.0),
                            side: BorderSide(color: Colors.black)))),
                onPressed: () {
                  Get.offAll(Dashboard(
                    from: "Shopping",
                  ));
                },
                child: Text(
                  'Start ordering',
                  style: GoogleFonts.aBeeZee(fontSize: 16, color: Colors.white),
                )),
          )
        ],
      ),
    );
  }
}
