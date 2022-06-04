import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/apis.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';

class AuthController extends GetxController{

  var obscureText=true;
  var controllerName=TextEditingController();
  var controllerEmailId=TextEditingController();
  var controllerMobileNo=TextEditingController();
  var controllerRefralCode=TextEditingController();
  var controllerOTP=TextEditingController();
  var controllerPassword=TextEditingController();

  var country = '';
  var countryCode = '';

  final getOTP = ''.obs;

  void sendRegOTP(String email) async{
    if(email.trim().isEmpty){
      EasyLoading.showToast('Enter valid email address');
      return;
    }
    int OTP = generateOTP();
    getOTP.value = '$OTP';
    Map body = emailBody(appName: 'Darshan Trade', email: email, OTP: OTP, subject: ' to complete registration');
    EasyLoading.show();
    ApiResponse resp = await sendEmailOTPAPI(body);
    EasyLoading.dismiss();
    EasyLoading.showToast('An Email send on you email $email');
    print('response => ${resp.data}');
  }
}