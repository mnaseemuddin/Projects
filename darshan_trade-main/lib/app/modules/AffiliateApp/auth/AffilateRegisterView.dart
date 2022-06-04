
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/AffiliateLoginView.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/auth_controller.dart';
import 'package:royal_q/app/shared/sawidgets/controllers/SAETextField.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/app/shared/widgets/app_textbox.dart';

import '../../../../api/apis.dart';
import '../../../shared/constants/colors.dart';
import '../../../shared/constants/common.dart';

// class AffilateRegsiterView extends StatefulWidget {
//   const AffilateRegsiterView({Key? key}) : super(key: key);
//
//   @override
//   _AffilateRegsiterViewState createState() => _AffilateRegsiterViewState();
// }

class AffilateRegsiterView extends GetView<AuthController> {
  // var obscureText=true;
  // var controllerName=TextEditingController();
  // var controllerEmailId=TextEditingController();
  // var controllerMobileNo=TextEditingController();
  // var controllerRefralCode=TextEditingController();
  // var controllerOTP=TextEditingController();
  // var controllerPassword=TextEditingController();
  //
  // var country = '';
  // var countryCode = '';

  // AuthController authController = Get.find<AuthController>();

  @override
  Widget build(BuildContext context) {
    Get.isRegistered<AuthController>()?Get.find<AuthController>():Get.put(AuthController());

    return AFBg(child: Scaffold(
      appBar: AppBar(elevation: 0,backgroundColor: Colors.transparent,
        iconTheme: IconThemeData(color: Colors.white),
      title: Text('REGISTER', style: textStyleHeading(color: Colors.white),),),
      backgroundColor: Colors.transparent,
      body: SafeArea(
        top: true,
        child: Padding(padding: EdgeInsets.symmetric(horizontal: 8),
        child: ListView(
          children: [
            SizedBox(height: 36,),

            SizedBox(height: 60, child: AFSelectItem(onChangeLocation: (name, code){
              print('onLocation Changed => $name, code =>$code');
              controller.country = name;
              controller.countryCode = code;
              controller.controllerMobileNo.text = controller.countryCode;
            },),),

            AFTextBox(label: 'Full Name', textEditingController: controller.controllerName),
            AFTextBox(label: 'Email ID(User Id)', textEditingController: controller.controllerEmailId),
            AFTextBox(label: 'Mobile Number', textEditingController: controller.controllerMobileNo, onChanged: (val){
              // if(controllerMobileNo.text.length<_countryCode.length)controllerMobileNo.text=_countryCode;
            }, prefix: Text(controller.countryCode.isNotEmpty?'${controller.countryCode} - \t':''),),
            AFTextBox(label: 'Referral Code', textEditingController: controller.controllerRefralCode),
            AFTextBox(label: 'OTP', textEditingController: controller.controllerOTP,
                suffix: Obx(() => controller.getOTP.isEmpty?TextButton(onPressed: (){
                  controller.sendRegOTP(controller.controllerEmailId.text);
                }, child: Text('Get OTP', style: TextStyle(color: Colors.white),)):TextButton(onPressed: (){
                  controller.sendRegOTP(controller.controllerEmailId.text);
                }, child: Text('Resend OTP', style: TextStyle(color: Colors.white)))), suffixSec: Obx(() => controller.getOTP.isEmpty?TextButton(onPressed: (){
                controller.sendRegOTP(controller.controllerEmailId.text);
              }, child: Text('Get OTP', style: TextStyle(color: Colors.white),)):TextButton(onPressed: (){
                controller.sendRegOTP(controller.controllerEmailId.text);
              }, child: Text('Resend OTP', style: TextStyle(color: Colors.white)))),),
            AFTextBox(label: 'Password', textEditingController: controller.controllerPassword),
            minSpacing(40),
            Padding(
              padding: const EdgeInsets.only(left:30.0,right: 30),
              child: AFSubmitButton(onPressed: (){
                if(validation()) {
                  EasyLoading.show();
                  register();
                }
              }, title: "Register"),
            ),

            SizedBox(height: 24,),
            Align(alignment: Alignment.center, child: GestureDetector(
              onTap: (){
                Get.back();
              },
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text('Already have an account? ',style: textStyleLabel(color:
                  Colors.white)),
                  Text('Login', style: textStyleHeading2(color:
                  Colors.white,),)
                ],),
            ),)

          ],
        ),),
      ),
    ));
  }

  minSpacing(double height) {
    return SizedBox(height: height,);
  }
  /**
  passwordTextField() =>Padding(
    padding: const EdgeInsets.only(left:30.0,top: 15,right: 30),
    child: TextFormField(
      controller: controllerPassword,
      obscureText: obscureText,
      validator: (v) {
        if (v!.trim().isEmpty) {
          return 'Please Enter Password .';
        }
        return null;
      },
      textInputAction: TextInputAction.next,
      inputFormatters: [
        LengthLimitingTextInputFormatter(35),
        FilteringTextInputFormatter.deny(RegExp('[ ]')),
      ],
      decoration: InputDecoration(
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1,color: Color(0xffece7e7))
          ),
          suffixIcon:  IconButton(onPressed: (){
            setState(() {
              if(obscureText) {
                obscureText=false;
              } else {
                obscureText=true;
              }
            });
          },icon: obscureText?Icon(Icons.visibility,
            color: Colors.grey[600],):const Icon(Icons.visibility_off,
            color: Colors.black,),),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),

          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: 'Password',
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: true,
          fillColor: Colors.grey[200]
      ),
    ),
  );

  mobileNumberTextField() =>Padding(
    padding: const EdgeInsets.only(left:30.0,right: 30),
    child: TextFormField(
      controller: controllerMobileNo,
      style: const TextStyle(fontSize: 14.5),
      textInputAction: TextInputAction.next,
      keyboardType: TextInputType.number,
      inputFormatters: [
          LengthLimitingTextInputFormatter(13),
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],
      decoration: InputDecoration(
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(width: 1,
                color: Color(0xffe9e9ea)),
          ),
          contentPadding: const EdgeInsets.only(top: 20,left: 10),
          hintText: "Mobile No",

          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: true,
          fillColor: Colors.grey[200]
      ),
    ),
  );
**/

  bool validation() {

    if(controller.controllerName.text.trim().isEmpty){
      toastMessage("Please Enter Full Name .");
      return false;
    }


    if(controller.controllerEmailId.text.trim().isEmpty){
      toastMessage("Please Enter Email ID .");
      return false;
    }

    if(controller.controllerMobileNo.text.trim().isEmpty){
      toastMessage("Please Enter Mobile Number .");
      return false;
    }

    if(controller.controllerRefralCode.text.trim().isEmpty){
      toastMessage("Please Enter Referral Code .");
      return false;
    }

    if(controller.getOTP.value != controller.controllerOTP.text){
      toastMessage("Enter valid otp");
      return false;
    }

    if(controller.controllerPassword.text.trim().isEmpty){
      toastMessage("Please Enter Password .");
      return false;
    }
    return true;
  }

  void debugPrintMsg(String msg) {
    print(msg);
  }

  void register() async{
    Map mao = {
      "full_name": controller.controllerName.text,
      "email": controller.controllerEmailId.text,
      "mobile": '${controller.countryCode}-${controller.controllerMobileNo.text}',
      "password": controller.controllerPassword.text,
      "used_referal_code": controller.controllerRefralCode.text
    };
    debugPrintMsg(mao.toString());
    affiliateRegisterAPI(mao).then((value) {
      if (value.status) {
        EasyLoading.dismiss();
        toastMessage(value.message.toString());
        // navPushReplace(context,AffiliateLoginView());
        Get.off(() => AffiliateLoginView());
      } else {

        EasyLoading.dismiss();
        toastMessage(value.message.toString());
      }
    });
  }


}
