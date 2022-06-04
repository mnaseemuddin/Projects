import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/AffilateRegisterView.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/AffiliateLoginView.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffilateDashBoardView.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/shared/sawidgets/app_logo.dart';
import 'package:royal_q/app/shared/utils/navigator_helper.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';

class AFLoginMenu extends StatelessWidget {
  const AFLoginMenu({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return AFBg(child: Scaffold(
      backgroundColor: Colors.transparent,
      body: Column(children: [
        Expanded(child: AFAppLogo(), flex: 50,),
        Expanded(child: Padding(padding: EdgeInsets.symmetric(horizontal: 24),
        /**
          child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
          Expanded(child: GestureDetector(
            child: Container(
              height: 120,
              alignment: Alignment.center,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.login, color: Color(0xFFA90E0E), size: 36,),
                  Text('LOGIN', style: GoogleFonts.roboto(
                      fontSize: 20, fontWeight: FontWeight.w700,
                      color: Colors.white
                  ),)
                ],),
              decoration: BoxDecoration(
                  gradient: LinearGradient(colors: [
                    Color(0xFFFD9B8B),
                    Color(0xFFFF5555),
                  ]),

                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(50),
                    bottomLeft: Radius.circular(8),
                    topRight: Radius.circular(8),
                    bottomRight: Radius.circular(8),
                  ),

                  border: Border.all(
                      color: Color(0xFFFF5555), width: 1
                  )

              ),
            ),
            onTap: (){
              Get.to(() => AffiliateLoginView());
            },
          )),

          SizedBox(width: 16,),

          Expanded(child: GestureDetector(
            child: Container(
              // width: double.infinity,
              height: 120,
              alignment: Alignment.center,

              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.app_registration, color: Color(0xFFA90E0E), size: 36,),
                  Text('REGISTER', style: GoogleFonts.roboto(
                      fontSize: 20, fontWeight: FontWeight.w700,
                      color: Colors.white
                  ),)
                ],),

              decoration: BoxDecoration(
                  gradient: LinearGradient(colors: [
                    Color(0xFFFD9B8B),
                    Color(0xFFFF5555),
                  ]),

                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(8),
                    bottomLeft: Radius.circular(8),
                    topRight: Radius.circular(8),
                    bottomRight: Radius.circular(50),
                  ),

                  border: Border.all(
                      color: Color(0xFFFF5555), width: 1
                  )

              ),
            ),
            onTap: (){
              Get.to(() => AffilateRegsiterView());
            },
          )),

        ],),
            **/

          child: Column(children: [

            SubmitButton(icon: Icons.login, title: 'Login', onTap: () => Get.to(() => AffiliateLoginView())),
            SubmitButton(icon: Icons.app_registration, title: 'Register', onTap: () => Get.to(() => AffilateRegsiterView()),),

          ],),

        ), flex: 50,),

      ],),
    ));
  }
}


class SubmitButton extends StatelessWidget {
  final IconData icon;
  final String title;
  final Function() onTap;
  const SubmitButton({Key? key, required this.icon, required this.title, required this.onTap}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Stack(
        alignment: Alignment.centerLeft,
        children: [

          Container(
            height: 60,
            alignment: Alignment.center,
            margin: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
            padding: EdgeInsets.symmetric(horizontal: 30, vertical: 8),
            width: double.infinity,

            child: Text(title.toUpperCase(), style: GoogleFonts.roboto(
                fontSize: 20, color: Colors.white, fontWeight: FontWeight.w700
            ),textAlign: TextAlign.center,),
            decoration: BoxDecoration(
              gradient: LinearGradient(colors: [
                Color(0xFFFD9B8B),
                Color(0xFFFF5555),
                Color(0xFFFD9B8B),]),
              borderRadius: BorderRadius.circular(30),

            ),
          ),


          Container(
            height: 80,
            width: 80,
            margin: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
            padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Icon(icon, color: Color(0xFF7E0404),),
            decoration: BoxDecoration(
                gradient: LinearGradient(colors: [
                  Color(0xFFFF5555),
                  Color(0xFFFD9B8B)]),
                // borderRadius: BorderRadius.circular(30),
                shape: BoxShape.circle
            ),
          ),

        ],),
      onTap: onTap,
    );
  }
}



void _lunchScreen(context) async{
  getUser().then((value){
    if(value!=null){
      navPush(context,AffiliateDashBoardView());
    }else{
      navPush(context, AffiliateLoginView());
    }
  });
}
