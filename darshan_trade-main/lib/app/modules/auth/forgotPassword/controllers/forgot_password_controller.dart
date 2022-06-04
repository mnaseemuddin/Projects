import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/api/api_flags.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';

class ForgotPasswordController extends GetxController {
  //TODO: Implement ForgotPasswordController

  final index = 0.obs;
  final OTP = 0.obs;

  TextEditingController controllerEmail    = TextEditingController();

  TextEditingController controllerPasswordOld = TextEditingController();
  TextEditingController controllerPasswordNew = TextEditingController();
  TextEditingController controllerPasswordReset = TextEditingController();

  var email = Rxn<FieldData>();
  var passwordOld = Rxn<FieldData>();
  var passwordNew = Rxn<FieldData>();
  var passwordReset = Rxn<FieldData>();

  var isEmailSend   = RxBool(false);

  @override
  void onInit() {
    super.onInit();
    email.value    = FieldData(data: 'sher1@ali.com', valid: false);
    passwordNew.value    = FieldData(data: '', valid: false);
    passwordReset.value    = FieldData(data: '', valid: false);
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {}

  void sendOTP(){
    OTP.value = generateOTP();
    print('${OTP.value}');
    Map body = {
      'Email': email.value!.data,
      'Subject': "verification code From Jackbot",
      'Body': 'verification code is  ${OTP.value}, please do not share with other'
    };
    EasyLoading.show(status: 'Sending...');
    sendEmailOTPAPI(body).then((value){
      EasyLoading.dismiss();
      ApiResponse response = value;
      if(response.status){
        index.value = 1;
      }else{
        EasyLoading.showToast('${response.data['message']}');
      }
    });
  }

  void changePassword(){
    Map body = {
      'Email': email.value!.data,
      'Password': passwordNew.value!.data
    };

    EasyLoading.show(status: '');
    forgotPasswordAPI(body).then((value){
      EasyLoading.dismiss();
      ApiResponse response = value;
      EasyLoading.showToast('${response.data['message']}');
        if(value.status){
            Get.back();
          }
    });

  }
}
