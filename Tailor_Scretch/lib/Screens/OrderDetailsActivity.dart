
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Model/OrderDetailsModel.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';

import '../ApiRepositary.dart';

class OrderDetailsActivity extends StatefulWidget {
  final int tblorderId;
  final String EstimatedDelivery;
  const OrderDetailsActivity({Key? key,required this.tblorderId,required this.EstimatedDelivery}) : super(key: key);

  @override
  _OrderDetailsActivityState createState() => _OrderDetailsActivityState();
}

class _OrderDetailsActivityState extends State<OrderDetailsActivity> {
  String? orderDateAndTime;
  String? orderNubmer;
  String imgUrl='';
 // bool loading=true;

  OrderDeatilsModel? orderDetailsModel;


  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    getOrderDetailAPI(userModel!.data.first.UserId,
        widget.tblorderId).then((value){
      setState(() {
        if(value.status){
          orderDetailsModel=value.data;
         // loading=false;
          // List list=value.data["data"];
          // orderDateAndTime=list[0]['order_date'];
          // orderNubmer=list[0]["order_number"];
          // imgUrl=list[0]['image'];
        }else{
          Fluttertoast.showToast(msg: value.message!);
        }
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
  Size size=MediaQuery.of(context).size;
    return Consumer<ConnectivityProvider>(builder: (cCtx,conn,child){
      if(conn.isOnline!=null){
        return conn.isOnline?Scaffold(
          backgroundColor: Colors.grey[100],
          appBar: AppBar(
            backgroundColor: UISupport.APP_PRIMARY_COLOR,
            title: Text('View Order Details'),
          ),
          body: orderDetailsModel==null?Center(child: CircularProgressIndicator(strokeWidth: 5.5,
            color:UISupport.APP_PRIMARY_COLOR,)):SingleChildScrollView(
            scrollDirection: Axis.vertical,
            child: Column(
              children: [
                Container(
                  color: Colors.white,
                  height: size.height*.06,
                  width: double.infinity,
                  child: Padding(
                    padding: const EdgeInsets.only(left:10.0),
                    child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text("Order# : "+orderDetailsModel!.data.first.orderNumber.toString(),textAlign: TextAlign.left,style: TextStyle(fontSize: 14,color: Colors.black),)),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top:8.0,bottom: 8),
                  child: Container(
                    height: size.height*.44,
                    color: Colors.white,
                    width: double.infinity,
                    child: Stack(
                      children: [
                        Padding(
                          padding: const EdgeInsets.only(left:8.0,bottom: 0,top: 10),
                          child: Center(
                            child: FadeInImage.assetNetwork(
                              image: orderDetailsModel!.data.first.image,
                              height: 400,fit: BoxFit.fitHeight,
                              placeholder:'assets/loading.gif',
                            ),
                          ),
                        ),
                        Align(
                          alignment: Alignment.topRight,
                          child: Padding(
                            padding: const EdgeInsets.only(right:10.0,top: 8),
                            child: Padding(
                              padding: const EdgeInsets.all(4.0),
                              child: GestureDetector(
                                onTap:(){
                                  easeBottomSheetDialog(orderDetailsModel!.data.first.userMesurment);
                                },
                                child: Container(
                                  decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(15),
                                      color: Colors.black,
                                      border: Border.all(width: 1,color: Colors.black12)
                                  ),
                                  height: 40,
                                  width: 82,
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Center(child: Image.asset("drawable/dry-clean.png",
                                          height: 15,width: 15,color: Colors.white)),
                                      Padding(padding: EdgeInsets.only(left: 6)),
                                      Center(child: Text("EASE",style: GoogleFonts.aBeeZee(
                                          color: Colors.white,fontSize: 13),))
                                    ],),
                                ),
                              ),
                            ),
                          ),
                        )
                      ],
                    ),
                  ),
                ),
                Container(color: Colors.white,
                  child: Column(
                    children: [
                      Container(
                        color: Colors.white,
                        height: size.height*.08,
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Flexible(
                                flex: 1,
                                child: Text("Order Date And Time",
                                    textAlign: TextAlign.start,
                                    style: GoogleFonts.aBeeZee(
                                        fontSize: 12.6,
                                        color: Colors.black)),
                              ),
                              Spacer(),
                              Flexible(
                                flex: 1,
                                child: Text(orderDetailsModel!.data.first.orderDate.toString(),
                                    style: TextStyle(fontSize: 12.6,
                                        color: Colors.black)),
                              ),
                            ],
                          ),
                        ),
                      ),
                      Divider(height: 2,),
                      // Container(
                      //   color: Colors.white,
                      //   height: size.height*.06,
                      //   child: Padding(
                      //     padding: const EdgeInsets.all(8.0),
                      //     child: Row(
                      //       mainAxisAlignment: MainAxisAlignment.center,
                      //       children: [
                      //         Padding(
                      //           padding: const EdgeInsets.only(left:0.0,top:0),
                      //           child: Text("Estimated Delivery on",style: GoogleFonts.aBeeZee(
                      //               fontSize: 13.6,
                      //               color: Colors.black)),
                      //
                      //         ),
                      //         Spacer(),
                      //         Padding(
                      //           padding: const EdgeInsets.only(left:0.0,top:0),
                      //           child: Text(widget.EstimatedDelivery.toString(),style: TextStyle(
                      //               fontSize: 13.6,
                      //               color: Colors.black)),
                      //
                      //         ),
                      //       ],
                      //     ),
                      //   ),
                      // ),
                      // Divider(height: 2,),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Align(
                            alignment: Alignment.centerLeft,
                            child: Text('Shipping Address',style: TextStyle(fontWeight: FontWeight.w500,color: Colors.black,fontSize: 14),)),
                      ),

                      Container(
                        height: size.height*.20,
                        child: Column(
                          children: [
                            Row(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.only(left:14.0,top:10),
                                  child: Text(orderDetailsModel!.data.first.name,style: GoogleFonts.aBeeZee(fontSize:13,color:Color(0xff000000))),
                                ),
                                Spacer(),
                                Padding(
                                  padding: const EdgeInsets.only(left:14.0,top:10,right: 10),
                                  child: Text("",style: GoogleFonts.aBeeZee(fontSize:13,fontWeight: FontWeight.w600,color:
                                  Color(0xff000000))),
                                ),
                              ],
                            ),
                            Align(
                              alignment: Alignment.centerLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(left:14.0,top: 8),
                                child: Text(orderDetailsModel!.data.first.address,style: GoogleFonts.aBeeZee(fontSize:13,color:
                                Color(0xff000000))),
                              ),
                            ),

                            Align(
                              alignment: Alignment.centerLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(left:14.0,top: 8),
                                child: Text(orderDetailsModel!.data.first.city+" "+orderDetailsModel!.data.first.state+" "+
                                    orderDetailsModel!.data.first.zipcode.toString(),style: GoogleFonts.aBeeZee(fontSize:13,color:
                                Color(0xff000000))),
                              ),
                            ),

                            Align(
                                alignment: Alignment.centerLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(left:14.0,top: 8),
                                  child: Text(orderDetailsModel!.data.first.mobile,style: GoogleFonts.aBeeZee(fontSize:13,color:
                                  Color(0xff000000))),
                                )
                            ),
                          ],
                        ),
                      ),

                    ],
                  ),)
              ],
            ),
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

   easeBottomSheetDialog(String userMesurment) {
    return showModalBottomSheet(
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10.0),
        ),
      context: context, builder: (context){
     List<String> userMesurment1=userMesurment.split(",");
     print(userMesurment1);
      return Container(
        child: ListView(
          shrinkWrap: true,
          reverse: true,
          children: userMesurment1.map((e) =>
          Column(
            children: [
              Padding(
                padding: const EdgeInsets.only(top:8.0),
                child: Container(
                  height: 34,
                  child: Center(child: Text(e,style: TextStyle(fontSize: 16,),)),
        ),
              ),
        Divider(height: 1.5,)
            ],
          )).toList(),
        ),
      );
    });
  }
}
