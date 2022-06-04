

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:pinput/pinput.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/PackageView.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffiliateLoginResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/otp_verification.dart';

import 'package:royal_q/app/shared/sawidgets/app_logo.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/app/shared/widgets/app_textbox.dart';
import 'package:royal_q/app_controller.dart';

import '../../../routes/app_pages.dart';
import '../../../shared/constants/colors.dart';
import '../../../shared/constants/common.dart';
import '../../../shared/constants/strings.dart';
import '../modules/AffilateDashBoardView.dart';
import 'AFLoginMenuView.dart';
import 'AffilateRegisterView.dart';


class AffiliateLoginView extends StatefulWidget {
  const AffiliateLoginView({Key? key}) : super(key: key);

  @override
  _AffiliateLoginViewState createState() => _AffiliateLoginViewState();
}

class _AffiliateLoginViewState extends State<AffiliateLoginView> {
  bool obscureText=true;
  final storage=GetStorage();
  // var controllerEmailId=TextEditingController(text: 'manojbisht842@gmail.com');
  // var controllerPassword=TextEditingController(text: '12345d');

  var controllerEmailId=TextEditingController();
  var controllerPassword=TextEditingController();

  TextEditingController controller = TextEditingController();
  final focusNode = FocusNode();
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
        Get.offAll(() => AFLoginMenu());
        return Future.value(true);
      },
      child: AFBg(child: Scaffold(
        backgroundColor: Colors.transparent,
        body: SafeArea(
          top: true,
          child: Padding(padding: EdgeInsets.symmetric(horizontal: 8),
          child: ListView(children: [
            minSpacing(30),
            GestureDetector(
              child: AFAppLogo(),
              onTap: (){
                Get.offAllNamed(Routes.LOGIN);
              },
            ),
            minSpacing(60),

            AFTextBox(label: 'Email Id', textEditingController: controllerEmailId,),
            AFTextBox(label: 'Password', textEditingController: controllerPassword, obscureText: true,
                suffix: Icon(Icons.remove_red_eye, color: Color(0xFFFF5555),),
                suffixSec: Icon(Icons.remove_red_eye, color: Colors.white54,)),

            Padding(
              padding: const EdgeInsets.only(left:20.0,top: 25,right: 20,bottom: 10),
              child: AFSubmitButton(onPressed: (){
                AppFocus.unfocus(context);
                if(validation()){
                  EasyLoading.show();
                  // _loginAPI();
                  authOTP(context);
                }
              }, title: "Login"),
            ),
            Padding(
              padding: const EdgeInsets.only(right:8.0),
              child: Align(alignment: Alignment.centerRight,
                child: TextButton(onPressed: (){
                  Get.toNamed(Routes.FORGOT_PASSWORD);
                }, child: Text(FORGOT_PASSWORD, style: textStyleHeading3(color: Colors.white),)),),
            ),

            TextButton(onPressed: (){
              print('Registration');
              Get.to(AffilateRegsiterView());
            }, child: Row(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text(NOT_REG, style: textStyleLabel(color: Colors.white, fontSize: 14.0),),
                Text(" "+REG_NOW, style: textStyleLabel(color: Colors.white, fontSize: 14.0),),
              ],)),

          ],),),
        ),
      )),
    );
  }

  minSpacing(double height) {
    return SizedBox(height: height,);
  }

  bool validation() {
    if(controllerEmailId.text.trim().isEmpty){
      toastMessage("Please Enter Email ID .");
      return false;
    }

    if(controllerPassword.text.trim().isEmpty){
      toastMessage("Please Enter Password .");
      return false;
    }
    return true;
  }


  void authOTP(BuildContext context,)async{
    AppController appController = Get.find();
    AppFocus.unfocus(context);
    controller = TextEditingController();
    appController.sendOTP(message: 'for login', email: controllerEmailId.text.toLowerCase());
    Get.to(() => OtpPage(Pinput(
      controller: controller,
      focusNode: focusNode,
      length: 6, onChanged: (val){
    }, onCompleted: (val){
    }, validator: (s) {
      if(s == '${appController.OTP.value}'){
        _loginAPI();
        Get.back();
      }
      return s == '${appController.OTP.value}' ? null : 'OTP is incorrect';
    },), controllerEmailId.text.toUpperCase()));
  }


  void _loginAPI() {

    Map mao = {
      "email": controllerEmailId.text.toLowerCase(),
      "password": controllerPassword.text,
    };
    affiliateLoginAPI(mao).then((value){
      if(value.status){
        EasyLoading.dismiss();
        AffiliateLoginResponse model=value.data;
        setUser(model).then((value){
          setAccountType("MLM").then((value){
            if(model.data.first.amountStack==1){
              navPush(context,AffiliateDashBoardView());
            }else {
              navPush(context, AffiliatePackageView());
            }
            //
          });

        });
      }else{
        EasyLoading.dismiss();
        toastMessage(value.message.toString());
      }
    });
  }

}


