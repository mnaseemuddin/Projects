import 'dart:convert';

import 'package:anandmart/Activity/MyOrderDetailsActivity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/MyOrder.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:awesome_dialog/awesome_dialog.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart' as http;

class MyOrderActivity extends StatefulWidget {
  const MyOrderActivity({Key? key}) : super(key: key);

  @override
  _MyOrderActivityState createState() => _MyOrderActivityState();
}

class _MyOrderActivityState extends State<MyOrderActivity> {
  List<MyOrder> myOrderList = [];
  String UserId = "";
  late Map data;
  bool IsVisibilityCancelOrder=false;

  double myOrderMinHeight=140;

  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value) {
      setState(() {
        UserId = value;
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),
        title: Text(
          "My Orders",
          style: GoogleFonts.aBeeZee(fontSize: 16, color: Colors.white),
        ),
      ),
      body: FutureBuilder(
        future: getOrders(),
        builder: (BuildContext context, AsyncSnapshot snapshot) {
          if (snapshot.hasError) {
            return Center(
                child: CircularProgressIndicator(
              strokeWidth: 6.0,color: Color(0xfff4811e)
            ));
          } else if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(
                child: CircularProgressIndicator(
              strokeWidth: 6.0,color: Color(0xfff4811e),
            ));
          } else if (snapshot.connectionState == ConnectionState.done) {
            return MyOrderHistoryUI(snapshot.data);
          }
          return Center(
              child: CircularProgressIndicator(
            strokeWidth: 6.0,color: Color(0xfff4811e)
          ));
        },
      ),
    );
  }

  Future<List<MyOrder>> getOrders() async {
    Map<String, dynamic> hashmap = {"uid": UserId};
    String reqUrls = Uri(queryParameters: hashmap).query;
    final response =
        await http.get(Uri.parse(ApiUrls.MyOrderHistoryAPI + "?" + reqUrls));
    data = json.decode(response.body);
    if (data["status"] == "success") {
      var res = data["orders"] as List;
      myOrderList = res.map((e) => MyOrder.formJson(e)).toList();
    }
    return myOrderList;
  }

  Widget MyOrderHistoryUI(List<MyOrder> myOrderList1) {
    return ListView.builder(
        scrollDirection: Axis.vertical,
        shrinkWrap: true,
        itemCount: myOrderList1.length == null ? 0 : myOrderList1.length,
        itemBuilder: (BuildContext ctx, int i) {
          if(myOrderList[i].orderStatus!="Cancel"){
            myOrderList[i].OrderUIMinHeight=180;
            myOrderList[i].IsVisiblityCancelOrder=true;
          }else{
          // myOrderList[i].OrderUIMinHeight=180;
            myOrderList[i].IsVisiblityCancelOrder=false;
          }
          return Padding(
            padding: const EdgeInsets.only(top: 15.0, left: 6, right: 6),
            child: GestureDetector(
              onTap: () {
               Get.to(MyOrderDetailsActivity(OrderId: myOrderList[i].orderno,));
              },
              child: Container(
                decoration: BoxDecoration(
                  border:
                      Border.all(width: 1.0, color: const Color(0xffece7e7)),
                  borderRadius: BorderRadius.circular(5),
                  color: const Color(0xffffffff),
                ),
                height: myOrderList[i].OrderUIMinHeight,
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Expanded(
                          child: Align(
                              alignment: Alignment.topLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 6, bottom: 8),
                                child: Text(
                                  "Order Id",
                                  textAlign: TextAlign.left,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                        Align(
                            alignment: Alignment.topCenter,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 8.0, top: 6, bottom: 8),
                              child: Text(
                                ":",
                                style: GoogleFonts.aBeeZee(
                                    fontSize: 12.6, color: Colors.black),
                              ),
                            )),
                        Expanded(
                          child: Align(
                              alignment: Alignment.topRight,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 6, bottom: 8, right: 8),
                                child: Text(
                                  myOrderList1[i].orderno,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                      ],
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Expanded(
                          child: Align(
                              alignment: Alignment.topLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8),
                                child: Text(
                                  "Items",
                                  textAlign: TextAlign.left,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                        Align(
                            alignment: Alignment.topCenter,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 8.0, top: 4, bottom: 8),
                              child: Text(
                                ":",
                                style: GoogleFonts.aBeeZee(
                                    fontSize: 12.6, color: Colors.black),
                              ),
                            )),
                        Expanded(
                          child: Align(
                              alignment: Alignment.topRight,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8, right: 8),
                                child: Text(
                                  myOrderList1[i].totalitems,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                      ],
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Expanded(
                          child: Align(
                              alignment: Alignment.topLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8),
                                child: Text(
                                  "Order Status",
                                  textAlign: TextAlign.left,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                        Align(
                            alignment: Alignment.topCenter,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 8.0, top: 4, bottom: 8),
                              child: Text(
                                ":",
                                style: GoogleFonts.aBeeZee(
                                    fontSize: 12.6, color: Colors.black),
                              ),
                            )),
                        Expanded(
                          child: Align(
                              alignment: Alignment.topRight,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8, right: 8),
                                child: Text(
                                  myOrderList1[i].orderStatus,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6,
                                      color: Color(0xfff8780a)),
                                ),
                              )),
                        ),
                      ],
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Expanded(
                          child: Align(
                              alignment: Alignment.topLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8),
                                child: Text(
                                  "Date",
                                  textAlign: TextAlign.left,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                        Align(
                            alignment: Alignment.topCenter,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 8.0, top: 4, bottom: 8),
                              child: Text(
                                ":",
                                style: GoogleFonts.aBeeZee(
                                    fontSize: 12.6, color: Colors.black),
                              ),
                            )),
                        Expanded(
                          child: Align(
                              alignment: Alignment.topRight,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8, right: 8),
                                child: Text(
                                  myOrderList1[i].orderDate,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                      ],
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Expanded(
                          child: Align(
                              alignment: Alignment.topLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8),
                                child: Text(
                                  "Amount",
                                  textAlign: TextAlign.left,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                        Align(
                            alignment: Alignment.topCenter,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 8.0, top: 4, bottom: 8),
                              child: Text(
                                ":",
                                style: GoogleFonts.aBeeZee(
                                    fontSize: 12.6, color: Colors.black),
                              ),
                            )),
                        Expanded(
                          child: Align(
                              alignment: Alignment.topRight,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 4, bottom: 8, right: 8),
                                child: Text(
                                  myOrderList1[i].totalamount,
                                  style: GoogleFonts.aBeeZee(
                                      fontSize: 12.6, color: Colors.black),
                                ),
                              )),
                        ),
                      ],
                    ),
                    Visibility(
                      visible: myOrderList[i].IsVisiblityCancelOrder,
                      child: GestureDetector(
                        onTap: (){
                          showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              // return object of type Dialog
                              return AlertDialog(
                                title: new Text("Sure Cancel"),
                                content: new Text("Do You Realy Want To Cancel Your Order ?"),
                                actions: <Widget>[
                                  // usually buttons at the bottom of the dialog
                                  new TextButton(
                                    child: new Text("No"),
                                    onPressed: () {
                                      Navigator.of(context).pop();
                                    },
                                  ),
                                  new TextButton(
                                    child: new Text("Yes"),
                                    onPressed: () {
                                      Navigator.of(context).pop();
                                      cancelOrder(myOrderList1[i].orderno);
                                    },
                                  ),
                                ],
                              );
                            },
                          );

                        },
                        child: Container(
                          height: 40,
                          color: Colors.grey[100],
                          width: double.infinity,
                          child: Row(
                            children: [
                              Padding(
                                padding: const EdgeInsets.only(left:8.0),
                                child: Text("Cancel Your Order",style: GoogleFonts.aBeeZee(
                                    fontSize: 11.5, color: Colors.black)),
                              ),
                              Spacer(),
                              Align(
                                  alignment: Alignment.centerRight,
                                  child: Padding(
                                    padding: const EdgeInsets.only(right:8.0),
                                    child: Icon(
                                      Icons.arrow_forward_ios,
                                      size: 15,
                                    ),
                                  ))
                            ],
                          ),
                        ),
                      ),
                    )
                  ],
                ),
              ),
            ),
          );
        });
  }

  void cancelOrder(String orderId) async{

    data={
      "uid":UserId,
      "order_number":orderId
    };

    try{
      
      final response=await http.post(Uri.parse(ApiUrls.CanelOrderAPI),body: data);
      if(response.statusCode==200){
        data=json.decode(response.body);

          if(data["status"]=="success"){
            AwesomeDialog(
              context: context,
              dialogType: DialogType.SUCCES,
              borderSide: BorderSide(color: Colors.white, width: 1),
              width: MediaQuery.of(context).size.height*0.50,
              buttonsBorderRadius: BorderRadius.all(Radius.circular(6)),
              animType: AnimType.SCALE,
              title: 'Order Cancelled !',
              desc: data["msg"],
              showCloseIcon: false,
              btnOkOnPress: () {
                setState(() {

                });
              },
            )..show();
          }else{
            AwesomeDialog(
              context: context,
              dialogType: DialogType.ERROR,
              borderSide: BorderSide(color: Colors.white, width: 1),
              width: MediaQuery.of(context).size.height*0.50,
              buttonsBorderRadius: BorderRadius.all(Radius.circular(6)),
              animType: AnimType.SCALE,
              title: 'Opps...',
              desc: data["msg"],
              showCloseIcon: false,
              btnOkOnPress: () {
                setState(() {

                });
              },
            )..show();
          }


      }
      
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
    
  }
}
