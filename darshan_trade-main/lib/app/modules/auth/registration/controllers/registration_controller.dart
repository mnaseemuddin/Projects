import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/request/register_request.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';

class RegistrationController extends GetxController {

  final ApiRepository apiRepository;
  RegistrationController({required this.apiRepository});

  TextEditingController controllerOTP = TextEditingController();
  TextEditingController controllerInv = TextEditingController(text: '');

  var isSignup = RxnBool(false);
  var isAccept = RxnBool(false);

  var location         = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();
  var email            = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();
  var mobile           = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();
  var password         = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();
  var confPassword     = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();

  // var transPassword         = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();
  // var transConfPassword     = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();

  var verCode          = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();
  var invCode          = Rxn<FieldData>();//FieldData(data: '', valid: false).obs();
  RxnInt OTP = RxnInt(0);
  // int OTP = 0.obs();

  final count = 0.obs;
  @override
  void onInit() {
    super.onInit();
    location.value         = FieldData(data: '', valid: false).obs();
    mobile.value           = FieldData(data: '', valid: false).obs();
    email.value            = FieldData(data: '', valid: false).obs();
    password.value         = FieldData(data: '', valid: false).obs();
    confPassword.value     = FieldData(data: '', valid: false).obs();

    // transPassword.value         = FieldData(data: '', valid: false).obs();
    // transConfPassword.value     = FieldData(data: '', valid: false).obs();

    verCode.value          = FieldData(data: '', valid: false).obs();
    invCode.value          = FieldData(data: '', valid: false).obs();
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {}
  void increment() => count.value++;

  checkDataValidity(){
    bool isSign = location.value!.valid&&email.value!.valid&&password.value!.valid
        &&(password.value!.data==confPassword.value!.data)
        &&mobile.value!.valid
        // &&(transPassword.value!.data==transConfPassword.value!.data)
        &&verCode.value!.valid&&invCode.value!.valid&&isAccept.value!;
      isSignup.value = isSign;//isSignup.value !=isSign;
      // refresh();
  }

  void register(BuildContext context) async{
    AppFocus.unfocus(context);
    isToast = false;
    final res = await apiRepository.register(
      RegisterRequest(
          // name:         name.value!.data,
          username:           email.value!.data,
          email:              email.value!.data,
          password:           password.value!.data,
          MobileNo:           mobile.value!.data,
          countryname:        location.value!.data,
          referralcode:       invCode.value!.data
      ),
    );

    printInfo(info: res.toString());
    if(res?.status == "succeed"){
      print(res?.message);
      Get.toNamed(Routes.LOGIN);
      EasyLoading.showToast('Login to continue.');
    }else{
      EasyLoading.dismiss();
    }
    EasyLoading.showToast(res?.message??'');
  }

  void sendOTP() async{
    OTP.value = generateOTP();
    verCode.value = FieldData(data: '$OTP', valid: true);
    Map body = emailBody(appName: 'Darshan Trade', email: email.value!.data, OTP: OTP, subject: ' to complete registration');
    EasyLoading.show();
    ApiResponse resp = await sendEmailOTPAPI(body);
    EasyLoading.dismiss();
    EasyLoading.showToast('An Email send on you email ${email.value!.data}');
    print('response => ${resp.data}');
    refresh();
  }



}
