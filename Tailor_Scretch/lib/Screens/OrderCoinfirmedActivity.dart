

import 'package:flutter/material.dart';
import 'package:flutter_gifimage/flutter_gifimage.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:tailor/Support/UISupport.dart';

import 'DashBoardActivity.dart';

class OrderConfirmActivity extends StatefulWidget {
  final String OrderNo;
  const OrderConfirmActivity({Key? key,required this.OrderNo}) : super(key: key);

  @override
  _OrderConfirmActivityState createState() => _OrderConfirmActivityState();
}

class _OrderConfirmActivityState extends State<OrderConfirmActivity> {
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
        Get.offAll(DashBoard());
        return Future.value(true);
      },
      child: Scaffold(
        body: ListView(
          children: [
            Container(
              height: MediaQuery.of(context).size.height*0.30,
              decoration: BoxDecoration(
                color: Color(0xffDBF5DB),
                borderRadius: BorderRadius.only(
                  bottomRight: Radius.circular(70.0),
                  bottomLeft: Radius.circular(70.0),
                ),
              ),
              child: Padding(
                padding: const EdgeInsets.only(left:58.0,right: 58),
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(top:28.0),
                      child: Text("Order Confirmed!!",textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(fontSize: 16,color: Colors.black
                          ,fontWeight: FontWeight.w600),),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(18.0),
                      child: Container(
                        height: 110,
                        child: Image.asset(
                          "assets/confirmgif.gif",
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Container(
              height: MediaQuery.of(context).size.height*0.40,
              child: Align(
                alignment: Alignment.center,
                child: Padding(
                  padding: const EdgeInsets.all(20.0),
                  child: Text("Your Order Number is "+widget.OrderNo+" Your Order was placed Successfully" +
                      " and you will soon receive your order confirmation via email.",style: GoogleFonts.aBeeZee(
                      fontSize: 13,color: Colors.black
                  ),),
                ),
              ),
            ),
            Container(
              height: MediaQuery.of(context).size.height*0.20,
              child: Align(
                alignment: Alignment.bottomCenter,
                child: Padding(
                  padding: const EdgeInsets.all(14.0),
                  child: GestureDetector(
                    onTap: (){
                      Get.offAll(DashBoard());
                    },
                    child: Container(
                      height: 50,
                      decoration: BoxDecoration(
                        border: Border.all(
                            width: 1.0, color: const Color(0xffece7e7)),
                        borderRadius: BorderRadius.circular(5),
                        color: UISupport.APP_PRIMARY_COLOR,
                      ),
                      child: Center(child: Text("Back To Home".toUpperCase(),style:
                      GoogleFonts.aBeeZee(fontSize: 14,color: Colors.white,fontWeight: FontWeight.w600),)),
                    ),
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
