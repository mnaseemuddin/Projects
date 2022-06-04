


import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/models/OrderDetailsModel.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

class OrderDetailsActivity extends StatefulWidget {
  final String OrderNo,DateAndTime;
  const OrderDetailsActivity({Key? key,required this.OrderNo,required this.DateAndTime}) : super(key: key);

  @override
  _OrderDetailsActivityState createState() => _OrderDetailsActivityState();
}

class _OrderDetailsActivityState extends State<OrderDetailsActivity> {


  var IsLoading=true;
  List<OrderDetailsModel>OrderDetailModelList=[];

  String totalAmt="0",MobileNo="",AddressType="",Address="",Name="",
      orderDate='',mainWallet='0',tdcWallet='0',state='',city='',country='';
     int? zipCode;



@override
  void initState() {
  Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
  orderDetailsAPI();
  super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
            backgroundColor: Colors.grey[100],
            appBar: AppBar(
              brightness: Brightness.dark,
              backgroundColor: Colors.black,
              title: Text('My Orders Details',style: GoogleFonts.aBeeZee(color:Colors.white,fontSize: 16),),
            ),
            body: IsLoading==false?ListView(
              shrinkWrap: true,
              scrollDirection: Axis.vertical,
              children: [
                Container(
                    height: 30,
                    color: Colors.white,
                    child: Row(
                      children: [
                        Align(
                            alignment: Alignment.centerLeft,
                            child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Text("Order #"+widget.OrderNo,style:
                              GoogleFonts.aBeeZee(fontSize: 13.2,color: Colors.black),),
                            )),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right: 8.0),
                          child: Text('Order Date : $orderDate',style: GoogleFonts.aBeeZee(fontSize: 13.2,color: Colors.black)),
                        )
                      ],
                    )),
                SingleChildScrollView(
                  scrollDirection: Axis.vertical,
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        child: ListView.builder(
                            physics: NeverScrollableScrollPhysics(),
                            shrinkWrap: true,
                            itemCount: OrderDetailModelList.length==null?0:OrderDetailModelList.length,
                            itemBuilder: (BuildContext ctx,int i){
                              if(OrderDetailModelList[i].total_tdc_detect==0){
                                OrderDetailModelList[i].visiblityTdcAmount=false;
                              }else{
                                OrderDetailModelList[i].visiblityTdcAmount=true;
                              }
                              return  Padding(
                                padding: const EdgeInsets.only(right:14.0,top: 10,bottom: 10,left: 10),
                                child: Container(
                                  height: 101,
                                  color: Colors.white,
                                  child: Row(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Container(
                                        width: MediaQuery.of(context).size.width*0.25,
                                        decoration: BoxDecoration(
                                            borderRadius: BorderRadius.only(topLeft: Radius.circular(8),
                                                bottomLeft:  Radius.circular(8))
                                        ),
                                        height: double.infinity,
                                        child: Padding(
                                          padding: const EdgeInsets.all(0.0),
                                          child: ClipRRect(
                                            borderRadius: BorderRadius.only(topLeft: Radius.circular(5),bottomLeft:
                                            Radius.circular(5)),
                                            child: Image.network(OrderDetailModelList[i].productImage,
                                              height: double.infinity,fit: BoxFit.fitHeight,
                                              errorBuilder: (context,error,stackTrace)=>
                                                  Center(child: Image.asset(""),),),
                                          ),
                                        ),
                                      ),
                                      Container(
                                          height: double.infinity,
                                          width: MediaQuery.of(context).size.width*0.67,
                                          child: Column(
                                            children: [
                                              Padding(
                                                padding: const EdgeInsets.only(left:8.0,top:15),
                                                child: Align(
                                                  alignment: Alignment.centerLeft,
                                                  child: RichText(
                                                    overflow: TextOverflow.ellipsis,
                                                    text: TextSpan(text: OrderDetailModelList[i].productName,style: GoogleFonts.aBeeZee(
                                                        fontSize: 11,
                                                        color: Colors.black)),
                                                  ),
                                                ),
                                              ),
                                              Padding(
                                                padding: const EdgeInsets.only(left:8.0,top:5),
                                                child: Container(
                                                  width: double.infinity,
                                                  child: Text("Quantity :"+OrderDetailModelList[i].quantity.toString(),
                                                      textAlign: TextAlign.left,style: GoogleFonts.aBeeZee(
                                                      fontSize: 11, color: Colors.black)),
                                                ),

                                              ),
                                              Padding(
                                                padding: const EdgeInsets.only(left:8.0,top: 4),
                                                child: Align(
                                                  alignment: Alignment.centerLeft,
                                                  child: Text("\$"+OrderDetailModelList[i].unitPrice+".00",
                                                      style: GoogleFonts.aBeeZee(
                                                          fontSize: 13,
                                                          fontWeight: FontWeight.w600,
                                                          color: Colors.black)),
                                                ),
                                              ),
                                              Visibility(
                                                visible:  OrderDetailModelList[i].visiblityTdcAmount,
                                                child: Padding(
                                                  padding: const EdgeInsets.only(top:0.0,left: 8),
                                                  child: Align(
                                                    alignment: Alignment.centerLeft,
                                                    child: Text('Detect From TDC Wallet '+"\$"+OrderDetailModelList[i].
                                                    total_tdc_detect.toString()+".00",
                                                        style: GoogleFonts.aBeeZee(
                                                            fontSize: 12,
                                                            fontWeight: FontWeight.w500,
                                                            color: Colors.black)),
                                                  ),
                                                ),
                                              )
                                            ],
                                          ))
                                    ],
                                  ),
                                ),
                              );
                            }),
                      ),
                    ],
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top:8.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        color: Colors.white,
                        child: Column(
                          children: [
                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:10,bottom: 4),
                              child: Align(
                                  alignment: Alignment.centerLeft,
                                  child: Text("Shipping Address",style:
                                  Theme.of(context).textTheme.bodyText1)),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:8),
                              child: Container(
                                height: 1.2,
                                color: Colors.grey[100],
                              ),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:10),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text(AddressType,style: GoogleFonts.aBeeZee(fontSize: 12,
                                  color: Colors.black,fontWeight: FontWeight.w600
                                ),),
                              ),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:8),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text(Name,style: TextStyle(fontSize: 12.4,color: Colors.black,fontWeight:
                                FontWeight.w400),),
                              ),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:10),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text(Address,maxLines: 2,style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black,
                                ),),
                              ),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:10),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text(state+' '+city+' '+zipCode.toString()+', $country',maxLines: 1,
                                  style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black,
                                ),),
                              ),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:10),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text("Contact"+": "+MobileNo,style: TextStyle(fontSize: 12.4,
                                    color: Colors.black, fontWeight: FontWeight.w400),),
                              ),
                            ),

                            SizedBox(height: 10,)

                          ],
                        ),
                      ),
                    ],
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.only(top:8.0),
                  child: Container(
                    height: 40,
                    color: Colors.white,
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:14),
                              child: Text("Detect From TDC Wallet",style: GoogleFonts.aBeeZee(
                                  fontSize: 13,
                                  color: Colors.black)),

                            ),
                            Spacer(),
                            Padding(
                              padding: const EdgeInsets.only(left:0.0,top:14,right:8),
                              child: Text("\$$tdcWallet"+'.00',style: TextStyle(
                                  fontSize: 14,
                                  color: Colors.black,fontWeight: FontWeight.w500)),

                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top:8.0),
                  child: Container(
                    height: 40,
                    color: Colors.white,
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:14),
                              child: Text("Detect From Main Wallet",style: GoogleFonts.aBeeZee(
                                  fontSize: 13,
                                  color: Colors.black)),

                            ),
                            Spacer(),
                            Padding(
                              padding: const EdgeInsets.only(left:0.0,top:14,right:8),
                              child: Text("\$$mainWallet"+'.00',style: TextStyle(
                                  fontSize: 14,
                                  color: Colors.black,fontWeight: FontWeight.w500)),

                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.only(top:8.0),
                  child: Container(
                    height: 40,
                    color: Colors.white,
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top:14),
                              child: Text("Grand Amount",style: GoogleFonts.aBeeZee(
                                  fontSize: 13,
                                  color: Colors.black)),

                            ),
                            Spacer(),
                            Padding(
                              padding: const EdgeInsets.only(left:0.0,top:14,right:8),
                              child: Text("\$"+totalAmt.toString()+".00",style: TextStyle(
                                  fontSize: 14,
                                  color: Colors.black,fontWeight: FontWeight.w500)),

                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),

              ],
            ):Center(child: Container(
                padding: EdgeInsets.all(16),
                decoration: BoxDecoration(
                    color: APP_SECONDARY_COLOR,
                    borderRadius: BorderRadius.all(Radius.circular(16))
                ),
                child: CircularProgressIndicator(strokeWidth: 6.0)))
        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }
  orderDetailsAPI()async{
    try{
      final response=await http.get(Uri.parse(OrderDetailsUrl+widget.OrderNo.toString()),headers:
      headers);
      Map data =json.decode(response.body);
      print(data);
      print(widget.OrderNo);
      if(data["status"]!=ERROR){
        if(!mounted)
          return;
        setState(() {
          IsLoading=false;
          var res=data["order_products_data"] as List;
          List otherDetailList=data["orderdata"];
          MobileNo=otherDetailList[0]["mobile"];
          orderDate=otherDetailList[0]['order_date_time'];
          Name=otherDetailList[0]["name"];
          AddressType=otherDetailList[0]["address_type"];
          Address=otherDetailList[0]["address"];
          totalAmt=otherDetailList[0]["total_amount"];
          mainWallet=otherDetailList[0]['other_wallet'];
          tdcWallet=otherDetailList[0]['all_tdc_detect'];
          state=otherDetailList[0]['state'];
          city=otherDetailList[0]['city'];
          zipCode=otherDetailList[0]['zipcode'];
          country=otherDetailList[0]['country'];

          OrderDetailModelList =res.map((e) => OrderDetailsModel.fromJson(e)).toList();
        });
      }else{
        Fluttertoast.showToast(msg: data["message"]);
      }
    }catch(e){
      Fluttertoast.showToast(msg: e.toString());
    }
  }
}
