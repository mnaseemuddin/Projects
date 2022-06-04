



import 'dart:async';
import 'dart:convert';
import 'dart:math';

import 'package:anandmart/Activity/MyAddressActivtity.dart';
import 'package:anandmart/Activity/OrderConfirmActivity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:cashfree_pg/cashfree_pg.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:google_fonts/google_fonts.dart';
import 'package:progress_dialog/progress_dialog.dart';

class ReviewYourOrderActivity extends StatefulWidget {
  final String Title,Locality,City,Address,timeSlot,daydateSlots,AID,MobileNo;
  const ReviewYourOrderActivity({ Key? key ,required this.Title,required this.Locality,required this.City,
  required this.Address,required this.timeSlot,required this.daydateSlots,required this.AID,required this.MobileNo}) :
   super(key: key);

  @override
  _ReviewYourOrderActivityState createState() => _ReviewYourOrderActivityState();
}

class _ReviewYourOrderActivityState extends State<ReviewYourOrderActivity> {
  
  
  String ratiopaymentMnethod="COD";

  late Map map;

  String userId="",deviceId="",walletAmt='',cartSubTotal='',shippingCharge='',
  savingAmt="",walletDiscount='',couponCode="",promoCodeDiscountAmt="";

  bool IsCouponCodeApplied=false;

  TextEditingController editCoupon=new TextEditingController();

  var _key=new GlobalKey<FormState>();

  double totalAmt=0.0;
  bool Ischecked=false,IsLoading=true;

  String paymentMethod="",UserId="";
  String mobileNo="",tokenNo="";

  var data;

  var IsVisibilityApplyCoupon=false;

  late ProgressDialog pr;

  String tvApplyCode="Apply Coupon Code";


  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      SharePreferenceManager.instance.getDeviceID("DeviceID").then((value){
setState(() {
 deviceId=value; 
 getReviewOrder(userId,deviceId);
});
      });
    setState(() {
      userId=value;
    });
    });
    print(widget.Address);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {

pr=new ProgressDialog(context,isDismissible: true);
    pr.style(
        message: 'Please wait...',
        borderRadius: 10.0,
        backgroundColor: Colors.white,
        progressWidget: Padding(
          padding: const EdgeInsets.all(8.0),
          child: CircularProgressIndicator(strokeWidth: 5.0,color: Colors.black,),
        ),
        elevation: 20.0,
        insetAnimCurve: Curves.easeInOut,
        progress: 0.0,
        maxProgress: 100.0,
        progressTextStyle: TextStyle(
            color: Colors.black, fontSize: 13.0, fontWeight: FontWeight.w400),
        messageTextStyle: TextStyle(
            color: Colors.black, fontSize: 19.0, fontWeight: FontWeight.w600)
    );
    return Scaffold(
      backgroundColor: Colors.grey[100],
      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),title: Center(
        child: Text("Review Your Order",
        style: GoogleFonts.aBeeZee(fontSize: 16,color: Colors.white),),
      ),),
      body: IsLoading==false?ListView(children: [

    Container(
      height:MediaQuery.of(context).size.height*0.81,
      child: SingleChildScrollView(
        child: Column(children: [
        Container(
          color: Color(0xffffffff),
          height: 180,
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.only(left:14.0,top: 10),
                child: Align(
                    alignment: Alignment.centerLeft,
                    child: Text("Order Summary".toUpperCase(),style: GoogleFonts.aBeeZee(fontSize:12,color: Colors.black))),
              ),
              Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Text("Basket Value",style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.grey[700])),
                  ),
                  Spacer(),Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Image.asset(
                      "drawable/rupee.png",
                      height: 8,
                      width: 8,
                      color: Colors.grey[800],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:1.0,top: 10,right: 10),
                    child: Text(cartSubTotal,style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
                  ),

                ],
              ),
              Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Text("Delivery Charge",style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black87)),
                  ),
                  Spacer(),Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Image.asset(
                      "drawable/rupee.png",
                      height: 8,
                      width: 8,
                      color: Colors.grey[800],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:1.0,top: 10,right: 10),
                    child: Text(shippingCharge,style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
                  ),
                ],
              ),
              Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Text("Your Total Saving ",style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black87)),
                  ),
                  Spacer(),Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Image.asset(
                      "drawable/rupee.png",
                      height: 8,
                      width: 8,
                      color: Colors.grey[800],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:1.0,top: 10,right: 10),
                    child: Text(savingAmt,style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
                  ),
                ],
              ),
              Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 14),
                    child: Text("Wallet Discount ",style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
                  ),
                  Spacer(),Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Image.asset(
                      "drawable/rupee.png",
                      height: 8,
                      width: 8,
                      color: Colors.grey[800],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:1.0,top: 10,right: 10),
                    child: Text(walletDiscount,style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
                  ),
                ],
              ),
              Padding(
                padding: const EdgeInsets.only(top:18.0),
                child: Container(
                  height: 1,
                  color: Colors.grey[200],
                  width: double.infinity,
                ),
              ),

              Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 14),
                    child: Text("Total Amount".toUpperCase(),style: GoogleFonts.aBeeZee(fontSize:11.5,color: Colors.black)),
                  ),
                  Spacer(),Padding(
                    padding: const EdgeInsets.only(left:14.0,top: 10),
                    child: Image.asset(
                      "drawable/rupee.png",
                      height: 8,
                      width: 8,
                      color: Colors.grey[800],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:1.0,top: 10,right: 10),
                    child: Text(totalAmt.toString(),style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
                  ),
                ],
              ),

            ],
          ),
        ),




   GestureDetector(
     onTap: (){
         _openPromoCodeBottomSheet();
     },
     child: Padding(
         padding: const EdgeInsets.only(left:0.0,top: 6),
         child: Container(
           color: Color(0xffffffff),
           height: 45,
           child: Row(
             mainAxisAlignment: MainAxisAlignment.center,
             children: [
             Padding(
                padding: const EdgeInsets.only(left:14.0,top: 0),
               child: Image.asset("drawable/dis.png",color: Colors.black,height: 18,width: 18,),
             ),Padding(padding: const EdgeInsets.only(left:6.0,top: 0,right: 10),
                  child: Text(tvApplyCode,style: GoogleFonts.aBeeZee(fontSize:12,color: Color(
                      0xff090909))),
                ),
                Spacer(),
               Visibility(
                 visible: IsVisibilityApplyCoupon,
                 child: Padding(
                   padding: const EdgeInsets.only(left:14.0,top: 0),
                   child: Image.asset(
                     "drawable/rupee.png",
                     height: 8,
                     width: 8,
                     color: Colors.green,
                   ),
                 ),
               ),
               Visibility(
                 visible: IsVisibilityApplyCoupon,
                 child: Padding(padding: const EdgeInsets.only(left:6.0,top: 0,right: 10),
                   child: Text(promoCodeDiscountAmt,style: GoogleFonts.aBeeZee(
                       fontSize:12,color:
                   Colors.green,fontWeight: FontWeight.bold)),
                 ),
               ),
               Padding(
                  padding: const EdgeInsets.only(right:15.0,top:0),
                  child: Icon(
                                              Icons.arrow_forward_ios,
                                              size: 15,
                                            ),
                ),
                Visibility(
                  visible: IsVisibilityApplyCoupon,
                  child: GestureDetector(
                    onTap:(){
                     setState(() {
                       IsVisibilityApplyCoupon=false;
                       couponCode="";
                       pr.show();
                       getReviewOrder(userId, deviceId);
                     });
                    },
                    child: Padding(padding: const EdgeInsets.only(left:6.0,top: 0,right: 15),
                      child: Text("Remove",style: GoogleFonts.aBeeZee(fontSize:12,color: Color(
                          0xfff4811e))),
                    ),
                  ),
                ),
           ],),
         ),
     ),
   ),

   Padding(
     padding: const EdgeInsets.only(top:6.0),
     child: Container(
         color: Color(0xffffffff),
         height: 45,
         child: Row(
           mainAxisAlignment: MainAxisAlignment.center,
           children: [
           Padding(
              padding: const EdgeInsets.only(left:0.0,top: 0),
             child: Checkbox(
               checkColor: Colors.white,
               activeColor: Colors.black,
               onChanged: (bool? value) {
               setState(() {
                 if(walletAmt=="0"){
                   Ischecked=false;
                   AlertDialogManager().IsErrorAlertDialog(context, "Status",
                       "Insufficient wallet balance ..", "");
                 }else {
                   Ischecked = value!;
                   if(Ischecked){
                     SharePreferenceManager.instance.getWalletMinimumDediction("walletdetection").then((value){
                       setState(() {
                         pr.show();
                         double walletDedication=totalAmt*double.parse(value)/100;
                         double walletAmt1=double.parse(walletAmt)-walletDedication;
                         walletAmt=walletAmt1.toString();
                         walletDiscount=walletDedication.toString();
                         totalAmt=totalAmt-walletDedication;
                         Timer(
                             Duration(seconds: 2),
                                 () {
                               SharePreferenceManager.instance.IsSharePreferenceInitialized("Initialize").then((value){
                                 setState(() {
                                   pr.hide();
                                 });
                               });

                             });
                       });
                     });
                   }else{
                     setState(() {
                       pr.show();
                       getReviewOrder(userId,deviceId);
                     });
                   }
                 }

               });
              }, value: Ischecked,),
           ),Padding(padding: const EdgeInsets.only(left:6.0,top: 0,right: 10),
                child: Text("Use Your Wallet",style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
              ),
              Spacer(),Padding(
                padding: const EdgeInsets.only(right:15.0,top:0),
                child: Icon(
                                            Icons.arrow_forward_ios,
                                            size: 15,
                                          ),
              ),
         ],),
     ),
   ),

   Padding(
     padding: const EdgeInsets.only(top:6.0),
     child: Container(
         color: Color(0xffffffff),
         height: 40,
         child: Row(
           mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Padding(
                padding: const EdgeInsets.only(left:14.0,top: 0),
                child: Text("Your Wallet Amount",style: GoogleFonts.aBeeZee(fontSize:11.5,color: Colors.black)),
              ),
              Spacer(),Padding(
                padding: const EdgeInsets.only(left:14.0,top: 0),
                child: Image.asset(
                                                                          "drawable/rupee.png",
                                                                          height: 8,
                                                                          width: 8,
                                                                          color: Colors.grey[800],
                                                                        ),
              ),
                                                                      Padding(
                padding: const EdgeInsets.only(left:1.0,top: 0,right: 20),
                child: Text(walletAmt,style: GoogleFonts.aBeeZee(fontSize:11,color: Colors.black)),
              ),
            ],
          ),
     ),
   ),

Padding(
         padding: const EdgeInsets.only(left:0.0,top: 6),
     child: Container(
         height: 25,
         width: double.infinity,
         color: Colors.grey[200],
         child: Padding(
           padding: const EdgeInsets.only(left:14.0,top: 0),
           child: Align(
             alignment: Alignment.centerLeft,
             child: Text("Deliever To :",style: GoogleFonts.aBeeZee(fontSize: 13,color: Colors.black),)),
         ),

     ),
   ),

   Container(height: 120,color: Colors.white,
   child: Column(children: [
        Row(
          children: [
            Padding(
              padding: const EdgeInsets.only(left:14.0,top:10),
              child: Text(widget.Title.toUpperCase(),style: GoogleFonts.aBeeZee(fontSize:13,color:Color(0xff000000))),
            ),
            Spacer(),
            GestureDetector(
              onTap: (){
                Get.to(MyAddressActivity(CType: "",));
              },
              child: Padding(
                padding: const EdgeInsets.only(left:14.0,top:10,right: 10),
                child: Text("Edit Address",style: GoogleFonts.aBeeZee(fontSize:13,color:
                Color(0xfff4811e))),
              ),
            ),
          ],
        ),

        Align(
          alignment: Alignment.centerLeft,
          child: Padding(
            padding: const EdgeInsets.only(left:14.0,top: 8),
            child: Text(widget.Locality+","+widget.City,maxLines: 1,style: GoogleFonts.aBeeZee(fontSize:13,color:
                  Color(0xff000000))),
          ),
        ),

     Align(
         alignment: Alignment.centerLeft,
         child: Padding(
           padding: const EdgeInsets.only(left:14.0,top: 8),
           child: Text(widget.Address,maxLines:2,overflow: TextOverflow.ellipsis,style: GoogleFonts.aBeeZee(fontSize:13,color:
           Color(0xff000000))),
         ),
     ),

   Align(
          alignment: Alignment.centerLeft,
          child: Padding(
            padding: const EdgeInsets.only(left:14.0,top: 8),
            child: Text(widget.MobileNo,style: GoogleFonts.aBeeZee(fontSize:13,color:
                  Color(0xff000000))),
          ),
        ),

   ],),),

   Padding(
         padding: const EdgeInsets.only(left:0.0,top: 6),
     child: Container(
         height: 25,
         width: double.infinity,
         color: Colors.grey[200],
         child: Padding(
           padding: const EdgeInsets.only(left:14.0,top: 0),
           child: Align(
             alignment: Alignment.centerLeft,
             child: Text("Payment Option".toUpperCase(),style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black),)),
         ),

     ),
   ),

 Container(
   child: Column(
     mainAxisSize: MainAxisSize.min,
     children: [
         Row(children: [
            Radio(
                          value: "COD",
                          groupValue: ratiopaymentMnethod,
                          onChanged: (value) {
                            setState(() {
                             ratiopaymentMnethod=value as String;
                            });
                          },
                          activeColor: Colors.black,
                        ),
                        Text("Cash On Delivery".toUpperCase(),style: GoogleFonts.aBeeZee(fontSize:13,color:
                        Color(0xff111111)),)
         ],),
         Padding(
           padding: const EdgeInsets.only(left:45.0),
           child:   Container(height: 1,width: double.infinity,color: Colors.grey[200],),
         ),
         Row(children: [
           Radio(
             value: "Online",
             groupValue: ratiopaymentMnethod,
             onChanged: (value) {
               setState(() {
                 ratiopaymentMnethod=value as String;
               });
             },
             activeColor: Colors.black,
           ),
           Text("Online".toUpperCase(),style: GoogleFonts.aBeeZee(fontSize:13,color:  Color(0xff111111)),)
         ],)
     ],
   ),
 ),




        ],),
      ),
    )
      ,GestureDetector(
          onTap: (){
            if(ratiopaymentMnethod=="COD"){
            makepayment("");
            }else if(ratiopaymentMnethod=="Online"){
              startOnlinePayment();
            }
          },
        child: Container(
          height: MediaQuery.of(context).size.height*0.06,
          color:Color(0xfff4811e),
          child: Center(
            child: Text("Place Order".toUpperCase(),style:
            GoogleFonts.aBeeZee(fontSize: 13,color:Colors.white),),
          ),
        ),
      )
      ],):Center(child: CircularProgressIndicator(
        strokeWidth: 6.0,color: Color(0xfff4811e),
      )),
    );
  }
  getReviewOrder(String userId,String deviceId1)async{

    map = {
      "uid": userId,
      "mid": deviceId1,
      "distance": "1",
      "promocode": couponCode
    
    };
    try{
  final response=await http.post(Uri.parse(ApiUrls.UserCheckOut),body: map);
  if(response.statusCode==200){
    map=json.decode(response.body);
    setState(() {
      if(pr.isShowing()){
        pr.hide();
      }
      IsLoading=false;
      if(map["status"]=="success"){
      walletAmt=map["wallet_amt"];
      cartSubTotal=map["cart_subtotal"];
      shippingCharge=map["shipping_charge"];
      savingAmt=map["discount_amt"];
      walletDiscount=map["wallet_redeem"].toString();
      totalAmt= double.parse(cartSubTotal)+double.parse(shippingCharge);
      }else{
        AlertDialogManager().IsErrorAlertDialog(context,"Status",map["status"], "");
      }

    });
     }
   }catch(e){
  AlertDialogManager().IsErrorAlertDialog(context,"Status",e.toString(), "");
   }
 }

  getReviewOrderWithCouponApply(String userId,String deviceId1)async{
    map = {
        "uid": userId,
        "mid": deviceId1,
        "distance": "1",
        "coupon": "1",
        "promocode": couponCode
      };

    try{
      final response=await http.post(Uri.parse(ApiUrls.UserCheckOut),body: map);
      if(response.statusCode==200){
        map=json.decode(response.body);
        setState(() {
          pr.hide();
          if(map["promocodestatus"]=="success"){
            walletAmt=map["wallet_amt"];
            cartSubTotal=map["cart_subtotal"];
            promoCodeDiscountAmt=map["promodiscount"];
            shippingCharge=map["shipping_charge"];
            savingAmt=map["discount_amt"];
            walletDiscount=map["wallet_redeem"].toString();
            IsVisibilityApplyCoupon=true;
           totalAmt= double.parse(cartSubTotal)+double.parse(shippingCharge);
           couponCode="";
           tvApplyCode="Coupon Applied";
          }else{
            showDialog(
              context: context,
              builder: (BuildContext context) {
                // return object of type Dialog
                return AlertDialog(
                  content: new Text(map["promocodestatus"]),
                  actions: <Widget>[
                    // usually buttons at the bottom of the dialog
                    new TextButton(
                      child: new Text("OK"),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),
                  ],
                );
              },
            );
          }

        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context,"Status",e.toString(), "");
    }
  }

   _openPromoCodeBottomSheet() {
    return showModalBottomSheet(
      context:  context,
      builder: (context){
        return Form(
          key: _key,
          child: Column(
            children: [
              Container(
                width: double.infinity,
                color: Color(0xff131313),
                height: 40,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Align(
                        alignment: Alignment.centerLeft,
                        child: GestureDetector(
                          onTap:(){
                           Navigator.of(context).pop();
                          },
                          child: Padding(
                            padding: const EdgeInsets.only(left:10.0),
                            child: Image.asset("drawable/ic_remove.png",width: 20,height: 20,color: 
                            Colors.white,),
                          ),
                        )),
                  Spacer(),
                   Align(
                     alignment: Alignment.centerRight,
                      child: Text("Apply Coupon",style: GoogleFonts.aBeeZee(fontSize: 15,color:Color(
                          0xffffffff)),),
                    ),
                    Spacer(),
                  ],
                ),
              ),
              Container(
                height: 60,
                child: Row(
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(left: 8.0,right: 0),
                      child: Container(
                        width: MediaQuery.of(context).size.width*0.70,
                        child: TextFormField(
                          controller: editCoupon,
                                    keyboardType: TextInputType.text,
                                    validator: (v){
                                      if(v!.isEmpty){
                                        return "Please Enter Promo Code here ..";
                                      }
                                      return null;
                                    },
                          decoration: InputDecoration(
                            prefixIcon: Padding(
                              padding: const EdgeInsets.only(top:15.0,bottom: 15,left: 0),
                              child: Image.asset("drawable/discount1.png",width: 1,height: 1,color: Colors.black,),
                            ),
                            hintText: "Enter Coupon Code",
                            hintStyle: GoogleFonts.aBeeZee(fontSize: 15,color: Color(
                                0xff999494)),
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(3),
                              borderSide: BorderSide(
                                color: Color(0xffc4bfbf),
                                width: 0.70,
                              ),
                            ),
                            contentPadding: EdgeInsets.only(bottom:2,top: 2),
                                        border: OutlineInputBorder(
                                            borderRadius: BorderRadius.circular(3.0),
                                            borderSide: BorderSide(color: Color(0xffc4bfbf))
                                        ),
                                        focusedBorder: OutlineInputBorder(
                                          borderSide: BorderSide(width:1.0,color: Color(0xffc4bfbf)),
                                          borderRadius: BorderRadius.circular(3.0),
                                        ),
                          ),

                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(right:8.0,top:7,bottom: 7),
                      child: GestureDetector(
                        onTap: (){
                          if(_key.currentState!.validate()) {
                            setState(() {
                              couponCode = editCoupon.text;
                              Navigator.of(context).pop();
                              pr.show();
                              getReviewOrderWithCouponApply(userId, deviceId);
                            });
                          }
                        },
                        child: Container(
                          width: MediaQuery.of(context).size.width*0.24,
                          height: 50,
                          color: Color(0xfff4811e),
                          child: Center(child: Text("Apply".toUpperCase(),style: GoogleFonts.aBeeZee(fontSize: 14,color: Colors.white),)),
                        ),
                      ),
                    )
                  ],
                ),
              )

            ],
          ),
        );
      });
  }
  
  
  makepayment(String transationMsg)async{

    map={
      "store_id":"0",
      "uid":userId,
      "daydate":widget.daydateSlots,
      "device_type":"Mobile",
      "aid":widget.AID,
      "payment_method":ratiopaymentMnethod,
      "time_slot":widget.timeSlot,
      "use_wallet":Ischecked.toString(),
      "promocode":couponCode,
      "p_status":transationMsg

    };

    try{
      
      final response=await http.post(Uri.parse(ApiUrls.completeOrderAPI),body: map);
      if(response.statusCode==200){
        map=json.decode(response.body);
        setState(() {
          if(map["status"]=="success"){
             Get.offAll(OrderConfirmActivity(OrderNo:map["orderid"]));
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status",map["status"], "");
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }

  }

  void startOnlinePayment() async{

  SharePreferenceManager.instance.getMobileNo("MobileNo").then((value){
    setState(() {
      mobileNo=value;
    });
  });

   const _chars = 'AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890';
   Random _rnd = Random();

   String getRandomString(int length) => String.fromCharCodes(Iterable.generate(
    length, (_) => _chars.codeUnitAt(_rnd.nextInt(_chars.length))));

   String OrderID=getRandomString(9);
 
   http.Response res=await http.post(Uri.parse("https://api.cashfree.com/api/v2/cftoken/order"),headers: {
     "x-client-id":ApiUrls.LiveCashFreeAppID,
     "x-client-secret":ApiUrls.LiveCashFreeSecretKey

   },body: json.encode({
    "orderId":OrderID.toUpperCase(),
    "orderAmount":totalAmt.toString(),
    "orderCurrency":"INR"
   }));
  
  if(res.statusCode==200){
    Map<String,dynamic>map=json.decode(res.body);
   tokenNo=map["cftoken"];
  Map<String,dynamic>inputPrams={
   "tokenData":tokenNo,
   "orderId":OrderID.toUpperCase(),
   "orderAmount": totalAmt.toString(),
   "customerName":"TestUser",
   "orderNote":"TestOrder",
   "orderCurrency":"INR",
   "appId":ApiUrls.LiveCashFreeAppID,
   "customerPhone":mobileNo,
   "customerEmail":"Test123@gmail.com",
   "stage":"PROD",
   //"notifyUrl":"https://test.gocashfree.com/notify"

   };
  print(inputPrams.toString());
   CashfreePGSDK.doPayment(inputPrams).then((value) =>{
    // {txStatus: SUCCESS, txMsg: Transaction Successful, paymentMode: Wallet, orderAmount: 1.00, 
    //referenceId: 1021246, signature: vYBejTPsfMnyjPAYokrUA7Escbi5O5YEQTr83FF7R4k=, 
    //txTime: 2021-08-18 13:06:58, orderId: lgO5o7vEw}
    data=value,
    if(data["txStatus"]=="SUCCESS"){
     makepayment(data["txMsg"])
    }
  });
   }
  } 
  //"txStatus" -> "SUCCESS"
//"orderAmount" -> "1.00"
//"paymentMode" -> "UPI"
//"orderId" -> "Q24MFVT4R"
//"txTime" -> "2021-08-20 12:27:44"
//"signature" -> "BL12LHNdg/3KZ2fVn4hsLpvgJ8nnYXTPyZixMj15lEk="
//"signature" -> "BL12LHNdg/3KZ2fVn4hsLpvgJ8nnYXTPyZixMj15lEk="
//"orderId" -> "Q24MFVT4R"
//"referenceId" -> "497912491"


}