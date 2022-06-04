




import 'dart:convert';

import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/MyOrderDetail.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:http/http.dart' as http;
import 'package:google_fonts/google_fonts.dart';

class MyOrderDetailsActivity extends StatefulWidget {

  final String OrderId;
  const MyOrderDetailsActivity({Key? key,required this.OrderId}) : super(key: key);

  @override
  _MyOrderDetailsActivityState createState() => _MyOrderDetailsActivityState();
}

class _MyOrderDetailsActivityState extends State<MyOrderDetailsActivity> {
  late Map data;
  late String UserId="";

  List<MyOrderDetail> myOrderDetailList=[];

  String DateAndTime="",orderDeliveryDate="",orderDeliveryTime="",custName="",cust_title="",
      cust_csz="",cust_mobile="",OrderSubTotalAmt="",ExtraDiscount="",order_shippingcharge="";

  bool IsLoading=true;


  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      setState(() {
        UserId=value;
        getOrdersDetails(UserId);
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[100],
      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),
        title: Text('My Orders Details',style: GoogleFonts.aBeeZee(color:Colors.white,fontSize: 16),),
      ),
      body: IsLoading==false?ListView(
        shrinkWrap: true,
        scrollDirection: Axis.vertical,
        children: [
          Container(
              height: 30,
              color: Colors.white,
              child: Align(
                  alignment: Alignment.centerLeft,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text("Order ID  - "+widget.OrderId,style: GoogleFonts.aBeeZee(fontSize: 13.2,color: Colors.black),),
                  ))),
          Column(
            mainAxisSize: MainAxisSize.min,
            children: [
             ListView.builder(
               physics: NeverScrollableScrollPhysics(),
               shrinkWrap: true,
                 itemCount: myOrderDetailList.length==null?0:myOrderDetailList.length,
                 itemBuilder: (BuildContext ctx,int i){
               return  Padding(
                 padding: const EdgeInsets.only(right:14.0,top: 10,bottom: 10,left: 10),
                 child: Container(
                   height: 101,
                   color: Colors.white,
                   child: Row(
                     children: [
                       Container(
                         width: MediaQuery.of(context).size.width*0.25,
                         height: double.infinity,
                         child: Padding(
                           padding: const EdgeInsets.all(8.0),
                           child: Image.network(myOrderDetailList[i].pimage,
                             height: double.infinity,
                             errorBuilder: (context,error,stackTrace)=>
                                 Center(child: Image.asset("drawable/logo.png"),),),
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
                                     text: TextSpan(text: myOrderDetailList[i].pname,style: GoogleFonts.aBeeZee(
                                         fontSize: 11,
                                         color: Colors.black)),
                                   ),
                                 ),
                               ),
                               Row(
                                 mainAxisAlignment: MainAxisAlignment.start,
                                 children: [
                                   Padding(
                                     padding: const EdgeInsets.only(left:8.0,top:5),
                                     child: Text("Quantity : ",style: GoogleFonts.aBeeZee(
                                         fontSize: 11,
                                         color: Colors.black)),

                                   ),
                                   Padding(
                                     padding: const EdgeInsets.only(left:8.0,top:5),
                                     child: Text(myOrderDetailList[i].qnty,style: GoogleFonts.aBeeZee(
                                           fontSize: 11,
                                           color: Colors.black)),

                                   ),
                                 ],
                               ),
                               Padding(
                                 padding: const EdgeInsets.all(8.0),
                                 child: Row(
                                   children: [
                                     Padding(
                                       padding: const EdgeInsets.only(left:0.0,top:10),
                                       child: Image.asset("drawable/rupee.png",height: 10,width: 10,),
                                     ),
                                     Padding(
                                       padding: const EdgeInsets.only(left:0.0,top:10),
                                       child: Text(myOrderDetailList[i].price,style: GoogleFonts.aBeeZee(
                                           fontSize: 11,
                                           color: Colors.black)),

                                     ),
                                     Spacer(),
                                     Padding(
                                       padding: const EdgeInsets.only(left:0.0,top:10,right: 15),
                                       child: Text(myOrderDetailList[i].weight,style: GoogleFonts.aBeeZee(
                                           fontSize: 11,
                                           color: Colors.black)),

                                     ),
                                   ],
                                 ),
                               )
                             ],
                           ))
                     ],
                   ),
                 ),
               );
             })

            ],
          ),
          Container(
            color: Colors.white,
            height: 35,
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left:0.0,top:0),
                    child: Text("Order Date And Time",style: GoogleFonts.aBeeZee(
                        fontSize: 11,
                        color: Colors.black)),

                  ),
                  Spacer(),
                  Padding(
                    padding: const EdgeInsets.only(left:0.0,top:0),
                    child: Text(DateAndTime,style: GoogleFonts.aBeeZee(
                        fontSize: 11,
                        color: Colors.black)),

                  ),
                ],
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top:8.0),
            child: Container(
              height: 94,
              color: Colors.white,
              child: Column(
                children: [

                  Padding(
                    padding: const EdgeInsets.only(left:8.0,top: 12),
                    child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text("Delivery Date And Time",style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black),)),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left: 8.0,top:10,right: 10,bottom: 10),
                    child: Container(height: 1.2,color: Colors.grey[100],),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left:8.0,top:0),
                        child: Text("Delivery Date",style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:0),
                        child: Text(orderDeliveryDate,style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left:8.0,top:10),
                        child: Text("Delivery Time",style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:10),
                        child: Text(orderDeliveryTime,style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

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
              height: 125,
              color: Colors.white,
              child: Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left:8.0,top:10),
                    child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text("Shipping Details",style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black),)),
                  ),

                  Padding(
                    padding: const EdgeInsets.only(left:8.0,top:8),
                    child: Container(
                      height: 1.2,
                      color: Colors.grey[100],
                    ),
                  ),

                  Padding(
                    padding: const EdgeInsets.only(left:8.0,top:8),
                    child: Align(
                      alignment: Alignment.centerLeft,
                      child: Text(custName,style: TextStyle(fontSize: 12.4,color: Colors.black,fontWeight:
                      FontWeight.w400),),
                    ),
                  ),


                  Padding(
                    padding: const EdgeInsets.only(left:8.0,top:10),
                    child: Align(
                      alignment: Alignment.centerLeft,
                      child: Text(cust_title+"\n"+cust_csz,style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black,
                      ),),
                    ),
                  ),

                  Padding(
                    padding: const EdgeInsets.only(left:8.0,top:10),
                    child: Align(
                      alignment: Alignment.centerLeft,
                      child: Text("Phone Number"+"      :  "+cust_mobile,style: TextStyle(fontSize: 12.4,color: Colors.black,
                          fontWeight:
                      FontWeight.w400),),
                    ),
                  )

                ],
              ),
            ),
          ),

          Padding(
            padding: const EdgeInsets.only(top:8.0),
            child: Container(
              height: 140,
              color: Colors.white,
              child: Column(
                children: [

                  Padding(
                    padding: const EdgeInsets.only(left:8.0,top: 12),
                    child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text("Price Details",style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black),)),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left: 8.0,top:10,right: 10,bottom: 10),
                    child: Container(height: 1.2,color: Colors.grey[100],),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left:8.0,top:0),
                        child: Text("Order SubTotal",style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:0),
                        child: Image.asset("drawable/rupee.png",height: 8.5,width: 8.5,),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:0,right: 8),
                        child: Text(OrderSubTotalAmt,style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left:8.0,top:10),
                        child: Text("Extra Discount ",style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:10),
                        child: Image.asset("drawable/rupee.png",height: 8.5,width: 8.5,),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:10,right:8),
                        child: Text(ExtraDiscount,style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left:8.0,top:10),
                        child: Text("Shipping  Change",style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:10),
                        child: Image.asset("drawable/rupee.png",height: 8.5,width: 8.5,),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:10,right:8),
                        child: Text(order_shippingcharge,style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                    ],
                  ),

                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left:8.0,top:14),
                        child: Text("Total Amount",style: GoogleFonts.aBeeZee(
                            fontSize: 11,
                            color: Colors.black)),

                      ),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:14),
                        child: Image.asset("drawable/rupee.png",height: 8.5,width: 8.5,),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left:0.0,top:14,right:8),
                        child: Text(OrderSubTotalAmt,style: TextStyle(
                            fontSize: 11,
                            color: Colors.black,fontWeight: FontWeight.w500)),

                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),

        ],
      ):Center(child: CircularProgressIndicator(strokeWidth: 6.0,color: Color(0xfff4811e),))
    );

  }

  getOrdersDetails(String orderId)async{

    data={
      "uid":UserId,
      "order_no":widget.OrderId
    };

    final response=await http.post(Uri.parse(ApiUrls.GetOrderDetails),body: data);
    if(response.statusCode==200){
      data=json.decode(response.body);
      setState(() {
        IsLoading=false;
        if(data["status"]=="success"){
          DateAndTime=data["order_date"];
          orderDeliveryDate=data["order_deliverydate"];
          orderDeliveryTime=data["order_deliverytime"];
          custName=data["cust_name"];
          cust_title=data["cust_title"];
          cust_csz=data["cust_csz"];
          cust_mobile=data["cust_mobile"];
          OrderSubTotalAmt=data["order_subtotal"];
          ExtraDiscount=data["order_discount"];
          order_shippingcharge=data["order_shippingcharge"];
          if(order_shippingcharge==""){
            order_shippingcharge="0";
          }
          var res=data['products'] as List;
          myOrderDetailList=res.map((e) => MyOrderDetail.formJson(e)).toList();
        }
      });
    }
  }

}





