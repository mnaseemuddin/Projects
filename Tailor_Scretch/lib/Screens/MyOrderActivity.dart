



import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/OrderDetailsActivity.dart';
import 'package:tailor/Model/OrderHistory.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';

import '../ApiRepositary.dart';

class MyOrderActivity extends StatefulWidget {
  const MyOrderActivity({ Key? key }) : super(key: key);

  @override
  _MyOrderActivityState createState() => _MyOrderActivityState();
}

class _MyOrderActivityState extends State<MyOrderActivity> {
  OrderHistory? _orderHistoryModel;



  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    getOrderHistory(userModel!.data.first.UserId).then((value){
      setState(() {
        if(value.status){
          _orderHistoryModel=value.data;
        }
      });
    });
    super.initState();
  }


  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (cContext,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          backgroundColor: Colors.white,
          appBar: AppBar(
            brightness: Brightness.dark,
            backgroundColor: Colors.black,
            title: Text("My Orders",style: GoogleFonts.aBeeZee(fontSize: 16),),
          ),
          body: _orderHistoryModel==null?Center(
            child: CircularProgressIndicator(strokeWidth: 5.5,color:
            UISupport.APP_PRIMARY_COLOR,),
          ):Padding(
              padding: const EdgeInsets.only(top:20.0,bottom: 50,left: 10,right: 10),
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(15),
                  boxShadow: [
                    BoxShadow(
                      color: const Color(0xffdedede),
                      offset: Offset(2, 1),
                      blurRadius: 2,
                    ),
                  ],
                ),
                child: ListView.builder(
                    itemCount: _orderHistoryModel!.data.length,
                    itemBuilder: (context,index){
                      Datum datum=_orderHistoryModel!.data.elementAt(index);
                      var estimate=datum.astimate_date.split("T");
                      print(estimate);
                      return Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: GestureDetector(
                          onTap: (){
                            Get.to(OrderDetailsActivity(tblorderId: datum.tblOrderId,EstimatedDelivery: estimate[0],));
                          },
                          child: Column(children: [
                            Row(children: [
                              Container(
                                width: MediaQuery.of(context).size.width*.60,
                                color: Colors.white,
                                child: Column(
                                  children: [
                                    Padding(
                                      padding: const EdgeInsets.only(top:15.0,left: 15),
                                      child: Align(
                                          alignment: Alignment.centerLeft,
                                          child: Text("Order# : "+datum.orderNumber,style: GoogleFonts.aBeeZee(fontWeight: FontWeight.w700))),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.only(top:8.0,left: 15),
                                      child: Align(
                                          alignment: Alignment.centerLeft,
                                          child: Text(/*"06-Sep-2021, 4:00 PM"*/datum.orderDate,style: GoogleFonts.aBeeZee(color:
                                          Colors.grey[600],fontWeight: FontWeight.w700))),
                                    ),

                                    // Padding(
                                    //   padding: const EdgeInsets.only(top:8.0,left: 15),
                                    //   child: Align(
                                    //     alignment: Alignment.centerLeft,
                                    //     child: Text("Estimated Delivery on "+estimate[0],style: GoogleFonts.aBeeZee(color:Colors.green,
                                    //         fontSize: 13)),
                                    //   ),
                                    // )

                                  ],
                                ),
                              ),
                              Container(
                                width: MediaQuery.of(context).size.width*.28,
                                height: 80,
                                child:Align(
                                  alignment: Alignment.centerRight,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:10.0,bottom: 8),
                                    child: Image.network(
                                      datum.image,
                                      fit: BoxFit.fill,
                                    ),
                                  ),
                                ),
                              )

                            ],),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Divider(color: Colors.grey[200],height: 1,),
                            )

                          ],),
                        ),
                      );
                    }),
              )
          ),
        ):InternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }
}



