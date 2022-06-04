
import 'package:flutter/material.dart';
import 'package:flutter_custom_clippers/flutter_custom_clippers.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/AFLoginMenuView.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/AffiliateLoginView.dart';
import 'package:royal_q/app/shared/sawidgets/app_logo.dart';


import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';

import 'package:royal_q/app/shared/sawidgets/app_logo.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';

import '../../../routes/app_pages.dart';
import 'modules/AffilateDashBoardView.dart';

class AffiliateSplashScreen extends StatefulWidget {
  const AffiliateSplashScreen({Key? key}) : super(key: key);

  @override
  State<AffiliateSplashScreen> createState() => _AffiliateSplashScreenState();
}

class _AffiliateSplashScreenState extends State<AffiliateSplashScreen> {

  var isInitialize;


  @override
  Widget build(BuildContext context){
    Size size = MediaQuery.of(context).size;
    return Stack(children: [
      AFBg(child: Column(children: [
        SizedBox(height: 16,),
        // Expanded(child: Center(child: Container(
        //   padding: EdgeInsets.all(32),
        //   child: Padding(padding: EdgeInsets.only(right: 20,),
        //     child: Image.asset('assets/expgain/darklogo.png', width: 3*size.width/4, height: size.width/2,
        //       color: Color(0xFF60F684), ),),
        //   decoration: BoxDecoration(border: Border.all(
        //     color: Color(0xFF60F684), width: 10,
        //   ), shape: BoxShape.circle),
        // ),), flex: 55,),

        Expanded(child: AFAppLogo(), flex: 50,),

        Expanded(child: Center(
          child: Container(
            padding: EdgeInsets.symmetric(horizontal: 32, vertical: 16),
            child: Column(children: [
              Text('The next gen of your investment', style: GoogleFonts.roboto(
                  fontSize: 6*size.height/100, fontWeight: FontWeight.w700, color: Colors.black
              )),

              SizedBox(height: 16,),
              Spacer(),

              GestureDetector(
                child: Container(
                  height: 50,
                  padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  alignment: Alignment.center,
                  child: Row(children: [
                    Expanded(child: Text('Let\'s Start', style: GoogleFonts.roboto(
                        fontSize: 18, fontWeight: FontWeight.w600, color: Color(0xFFFFFFFF)
                    ),),),

                    Icon(Icons.arrow_forward, color: Color(0xFFFFFFFF),)

                  ],),
                  decoration: BoxDecoration(
                    color: Color(0xFFFF5555),
                    borderRadius: BorderRadius.circular(25),

                  ),
                ),
                onTap: (){
                  // _lunchScreen();
                  Get.to(() => AFLoginMenu());
                },
              ),

              SizedBox(height: 48,),

            ],),
            decoration: BoxDecoration(
                color: Color(0xFFF6FFF6),
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(0),
                  topRight: Radius.circular(100),
                  bottomLeft: Radius.circular(100),
                ),
              boxShadow: [BoxShadow(color: Color(0x8FFF5555), offset: Offset(4, -4), blurRadius: 6)]
            ),
          ),
        ), flex: 50,),
      ],))
    ],);
  }

  @override
  void initState() {
    // _lunchScreen();
  }
  void _lunchScreen() async{
    getUser().then((value){
      if(value!=null){
        navPush(context,AffiliateDashBoardView());
      }else{
        navPush(context, AffiliateLoginView());
      }
    });
  }
}
