

import 'dart:math';

import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/CartModel.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:trading_apps/screens/Shopping/AddNewAddressActivity.dart';
import 'package:trading_apps/screens/Shopping/MyAddressActivity.dart';
import 'package:trading_apps/screens/Shopping/OrderSuccessActivity.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

class OrderReviewActivity extends StatefulWidget {
  final String AddressId,AddressType,Name,EmailId,Address,MobileNo,City,Country,State,ZipCode;
  const OrderReviewActivity({Key? key,required this.AddressId,required this.AddressType,required this.Name,required this.EmailId,required this.Address,required this.MobileNo,
  required this.City,required this.Country,required this.State,required this.ZipCode}) : super(key: key);

  @override
  _OrderReviewActivityState createState() => _OrderReviewActivityState();
}

class _OrderReviewActivityState extends State<OrderReviewActivity> {

  List<CartModel>cartModelList=[];

  var NoOfCartItems="0";
  int totalAmt=0,tdcWalletAmt=0,mainWalletAmt=0;



  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    cartAPI();
    totalCartItemAPI();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          backgroundColor: Colors.white,
          appBar: AppBar(
            backgroundColor: Colors.black,
            title: Text('Review Your Order',style: GoogleFonts.aBeeZee(fontSize:14,
                color: Colors.white),),
          ),
          body: Column(
            children: [
              Expanded(
                  flex: 10,
                  child: Container(
                    margin: EdgeInsets.only(left:10),
                    child: Row(children: [
                    Text("Shipping Address",style: textStyleHeading(color: APP_PRIMARY_COLOR,fontSize: 17.0)),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(bottom:8.0),
                        child: CommonButton(
                            height: 35,width: 90,borderRadius: 12,
                            onPressed: (){
                              Navigator.of(context).pop();
                            }, buttonName: 'Change',
                            buttoncolors: 0xFF222222, buttontextcolor: Colors.white),
                      )
                  ],),)),

              Expanded(
                  flex: 17,
                  child: Container(child: Row(children: [
                    Expanded(
                      flex: 10,
                      child: Padding(
                        padding: const EdgeInsets.all(5.0),
                        child: Align(
                          alignment: Alignment.topLeft,
                          child: Container(
                            height: 30,
                            width: 30,
                            decoration: BoxDecoration(
                              color: Colors.black,
                              borderRadius: BorderRadius.circular(30),
                            ),
                            child: Icon(Icons.location_on_rounded,size: 18,color: Colors.white,),
                          ),
                        ),
                      ),
                    ),
                    Expanded(
                        flex: 80,
                        child: Container(child: Column(
                          children: [
                            Align(
                                alignment: Alignment.topLeft,
                                child: Text(widget.Name, overflow: TextOverflow.ellipsis,maxLines: 1,
                                  style: GoogleFonts.aBeeZee(color:
                                  Colors.grey[900]),)),
                            Align(
                                alignment: Alignment.topLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(top:4),
                                  child: Text(widget.MobileNo,style: GoogleFonts.aBeeZee(color:
                                  Colors.grey[900]),),
                                )),
                            Align(
                                alignment: Alignment.topLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(top:4.0),
                                  child: Text(widget.Address, overflow: TextOverflow.ellipsis,
                                    maxLines: 2,style: GoogleFonts.aBeeZee(color:
                                    Colors.grey[900]),),
                                )),

                            Align(
                                alignment: Alignment.topLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(top:2.0),
                                  child: Text(widget.State+' '+widget.City+' '+widget.Country+' '+
                                      widget.ZipCode.toString(),maxLines: 1,style: GoogleFonts.aBeeZee(color:
                                  Colors.grey[900]),),
                                )),
                          ],
                        ),))
                  ],),)),

              Expanded(
                flex: 3,
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: Padding(
                    padding: const EdgeInsets.only(left:8.0),
                    child: Text("Order Summary",style: GoogleFonts.aBeeZee(fontSize: 15,
                        color: Colors.black)),
                  ),
                ),),
              
              Expanded(
                  flex: 42,
                  child: ListView.builder(
                      itemCount: cartModelList.length==null?0:cartModelList.length,
                      itemBuilder: (context,i){
                        return Padding(
                          padding: const EdgeInsets.only(top: 8.0,left: 5,right: 5),
                          child: Container(
                            height: 120,
                            decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(4),
                                boxShadow: [
                                  BoxShadow(
                                    color:  Colors.grey,
                                    offset: Offset(4, 3),
                                    blurRadius: 9,
                                  ),
                                ],
                                color: Colors.white),
                            child: Row(
                              children: [
                                Container(
                                  height: double.infinity,
                                  width: MediaQuery.of(context).size.width * .26,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:0.0,left: 0),
                                    child: Image.network(
                                      cartModelList[i].image,fit: BoxFit.fitHeight,),
                                  ),
                                ),Container(
                                  width: MediaQuery.of(context).size.width*.45,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:10.0),
                                    child: Column(
                                      mainAxisAlignment: MainAxisAlignment.start,
                                      children: [
                                        Align(
                                          alignment: Alignment.centerLeft,
                                          child: Padding(
                                            padding: const EdgeInsets.only(left:18.0,top: 15),
                                            child: RichText(
                                              textAlign: TextAlign.left,
                                              overflow:
                                              TextOverflow.ellipsis,
                                              text: TextSpan(text: cartModelList[i].productName,
                                                  style: GoogleFonts.aBeeZee(color: Colors.black,
                                                      fontWeight: FontWeight.bold)),

                                            ),
                                          ),

                                        ),
                                        Padding(
                                          padding: const EdgeInsets.only(top:5.0,left:15),
                                          child: Align(
                                            alignment: Alignment.centerLeft,
                                            child: RichText(
                                              textAlign: TextAlign.left,
                                              overflow:
                                              TextOverflow.ellipsis,
                                              text: TextSpan(
                                                  text:cartModelList[i].productDescription.replaceAll("\n", " "),style:
                                              GoogleFonts.aBeeZee(color: Colors.grey[600])
                                              ),
                                            ),
                                          ),
                                        ),
                                        Align(
                                          alignment: Alignment.centerLeft,
                                          child: Padding(
                                            padding: const EdgeInsets.only(left:18.0,top: 5),
                                            child: Text("\$"+cartModelList[i].unitPrice+".00",style: GoogleFonts.aBeeZee(color: Colors.black,
                                                fontWeight: FontWeight.bold),),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ),
                                Container(
                                  width: MediaQuery.of(context).size.width*.24,
                                  child: Align(
                                    alignment: Alignment.centerRight,
                                    child: Column(
                                      mainAxisAlignment: MainAxisAlignment.center,
                                      children: [
                                        Padding(
                                          padding: const EdgeInsets.all(8.0),
                                          child: Container(
                                            width: 20,
                                            height: 20,
                                          ),
                                        ),
                                        Container(
                                            height: 20,
                                            child: Row(
                                              mainAxisAlignment: MainAxisAlignment.end,
                                              children: [
                                                Image.asset("assets/images/cancel.png",height: 8,width: 8,
                                                  color: Colors.black,),
                                                Padding(
                                                  padding: const EdgeInsets.only(left:5.0),
                                                  child: Text(cartModelList[i].quantity.toString(),style:
                                                  GoogleFonts.aBeeZee(fontSize: 16),),
                                                ),
                                              ],
                                            )),
                                        Padding(
                                          padding: const EdgeInsets.all(8.0),
                                          child: Container(
                                            width: 20,
                                            height: 20,
                                          ),
                                        )
                                      ],
                                    ),
                                  ),
                                )
                              ],
                            ),
                          ),
                        );
                      })),

              Expanded(
                  flex: 18,
                  child: Padding(
                    padding: const EdgeInsets.all(3.0),
                    child: Container(
                      decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(4),
                      border: Border.all(width: 0.3,color: Color(0xffcbcbce)),
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                      Padding(
                        padding: const EdgeInsets.only(top:12.0,left: 10,right: 5),
                        child: Row(
                          children: [
                            Text("Detect From TDC Wallet",style:
                            GoogleFonts.aBeeZee(color: Colors.black,fontSize: 15,
                                fontWeight: FontWeight.w600),),
                            Spacer(),
                            Text("\$$tdcWalletAmt.00",style:
                            GoogleFonts.aBeeZee(color: Colors.black,fontSize: 15,
                                fontWeight: FontWeight.w700),),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top:12.0,left: 10,right: 5),
                        child: Row(
                          children: [
                            Text("Detect From Main Wallet",style:
                            GoogleFonts.aBeeZee(color: Colors.black,fontSize: 15,
                                fontWeight: FontWeight.w600),),
                            Spacer(),
                            Text("\$$mainWalletAmt.00",style:
                            GoogleFonts.aBeeZee(color: Colors.black,fontSize: 15,
                                fontWeight: FontWeight.w700),),
                          ],
                        ),
                      ),

                      Padding(
                        padding: const EdgeInsets.only(top:12.0,left: 10,right: 5),
                        child: Row(
                          children: [
                            Text("Grand Total",style:
                            GoogleFonts.aBeeZee(color: Colors.black,fontSize: 15,
                                fontWeight: FontWeight.w600),),
                            Spacer(),
                            Text("\$$totalAmt.00",style:
                            GoogleFonts.aBeeZee(color: Colors.black,fontSize: 15,
                                fontWeight: FontWeight.w700),),
                          ],
                        ),
                      ),

                    ],),
                    ),
                  )),

              Padding(
                padding: const EdgeInsets.only(bottom:5.0),
                child: Expanded(
                    flex: 10,
                    child: Center(
                      child: CommonButton(onPressed: (){
                        gotoPayment();
                      },borderRadius: 15,buttontextcolor: Colors.white,
                      buttoncolors: 0xFF222222,buttonName: 'Go to Payment',
                        width: MediaQuery.of(context).size.width*0.80, height: 50,),
                    )),
              )
            ],
          ),
        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }
  cartAPI()async{

    try{
      final resposne=await http.get(Uri.parse(CartListUrls+userModel!.
      data.first.userId.toString()),headers:headers);
      Map data=json.decode(resposne.body);
      if(data["status"]!=ERROR){
        if(!mounted)
          return;
        setState(() {
          totalAmt=data["total"];
          tdcWalletAmt=data['all_tdc_detect'];
          mainWalletAmt=totalAmt-tdcWalletAmt;
          var res=data["data"] as List;
          cartModelList=res.map((e) => CartModel.fromJson(e)).toList();
        });
      }else{
        Fluttertoast.showToast(msg: data["message"]);
      }
    }catch(e){
      Fluttertoast.showToast(msg: e.toString());
    }

  }

  void gotoPayment() async{
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    DateTime now = DateTime.now();
    //String formattedDate = DateFormat('kk:mm:ss \n EEE d MMM').format(now);
    String formattedDate = DateFormat('MMMM d, yyyy').format(now);
    Map<String,String>mao={
      "user_id":userModel!.data.first.userId.toString(),
      "total_amount":totalAmt.toString(),
      "order_quantity":NoOfCartItems,
      "order_type":"Wallet",
      "order_date_time":formattedDate,
      "address_type":widget.AddressType,
      "name":widget.Name,
      "email":widget.EmailId,
      "mobile":widget.MobileNo,
      "address":widget.Address,
      "city":widget.City,
      "country":widget.Country,
      "address_id":widget.AddressId,
      'all_tdc_detect':tdcWalletAmt.toString(),
      'other_wallet':mainWalletAmt.toString(),
      'state':widget.State,
      'zipcode':widget.ZipCode

    };
    print(mao);
    try{
      final response=await http.post(Uri.parse(CheckOutAPI),body: mao);
      Map data=json.decode(response.body);
      Navigator.of(context).pop();
      if(data["status"]!="Fail"){
        navPush(context, OrderConfirmActivity(OrderNo: data['Ordernumber'],));
      }else{
        _edgeAlert(data["message"].toString());
      }
    }catch(e){
     _edgeAlert(e.toString());
    }

  }
  void _edgeAlert(String description){
    edgeAlert(context, title: 'Uh oh!', description: description.toString(),duration: 2,
        gravity: Gravity.bottom,icon: Icons.error,backgroundColor: APP_SECONDARY_COLOR);
  }
  totalCartItemAPI()async {
    try {
      final response = await http.get(Uri.parse(
          NoOfItemsInCartsUrls + userModel!.data.first.userId.toString()));
      Map data=json.decode(response.body);
      if(!mounted)
        return;
      setState(() {
        if(data["status"]!=ERROR){
          NoOfCartItems=data["quantity"]==null?"0":data["quantity"];
        }else{
          Fluttertoast.showToast(msg: data["message"]);
        }
      });

    }catch(e){
      Fluttertoast.showToast(msg: e.toString());
    }
  }
}
