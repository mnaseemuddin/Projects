import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
// import 'package:fluttertoast/fluttertoast.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/sawidgets/controllers/app_text_controller.dart';
import 'package:royal_q/app/shared/sawidgets/sa_count_down.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

import '../controllers/registration_controller.dart';

class RegistrationView extends GetView<RegistrationController> {
  final List<AcceptText>texts = [
    AcceptText(text: 'read_carefully'.tr),
    AcceptText(text: 'service_and_agreement'.tr, isBtn: true),
    AcceptText(text: 'And'.tr),
    AcceptText(text: 'user_privacy_policies'.tr, isBtn: true),
  ];

  @override
  Widget build(BuildContext context) {

    var mcontroller = Get.isRegistered<RegistrationController>()
        ? Get.find<RegistrationController>()
        : Get.put(RegistrationController(apiRepository: Get.find()));

    return Scaffold(
      appBar: AppBar(elevation: 0,backgroundColor: Colors.transparent,
        brightness: Brightness.dark,
      iconTheme: IconThemeData(color: ColorConstants.iconTheme),),
      body: Container(
        padding: EdgeInsets.all(32),
        child: ListView(
          shrinkWrap: true,
          children: [
            Text('Register'.tr, style: textStyleTitle(color: ColorConstants.white),),
            Padding(padding: EdgeInsets.only(top: 8, bottom: 8),
              child: Text('Welcome_you_to_join'.tr, style: textStyleLabel(color: ColorConstants.white60),),),

            SelectItem(onChangeLocation: (val){
              print('onLocation Changed => $val');
              controller.location.value = FieldData(data: val, valid: true);
              controller.checkDataValidity();
            },),

            SizedBox(height: 8,),

            TextFieldIcon(onChanged: (val){
              controller.email.value = val;
              controller.checkDataValidity();
            }, hintText: 'Please_Enter_E-mail'.tr, labelText: 'E-mail', validate: VALIDATE.EMAIL, ),

            TextFieldIcon(onChanged: (val){
              controller.mobile.value = val;
              controller.checkDataValidity();
            }, hintText: 'Enter mobile number'.tr, labelText: 'Mobile', validate: VALIDATE.PNUMBER, ),

            TextFieldIcon(onChanged: (val){
              controller.password.value = val;
              controller.checkDataValidity();
            }, hintText: 'Set_6-12_digit_login_password'.tr, labelText: 'Enter_login_password'.tr, obscureText: true,
              suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: ColorConstants.white54),
                secondary: Icon(Icons.remove_red_eye, color: ColorConstants.APP_SECONDARY_COLOR,),), validate: VALIDATE.PASSWORD,),

            TextFieldIcon(onChanged: (val){
              controller.confPassword.value = val;
              controller.checkDataValidity();
            }, hintText: 'Set_6-12_digit_login_password'.tr, labelText: 'Confirm_password'.tr, obscureText: true,
              suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: ColorConstants.white54,),
                secondary: Icon(Icons.remove_red_eye, color: ColorConstants.APP_SECONDARY_COLOR,),),
              validate: VALIDATE.PASSWORD,),

            // TextFieldIcon(onChanged: (val){
            //   controller.transPassword.value = val;
            //   controller.checkDataValidity();
            // }, hintText: 'Set_6-12_digit_login_password'.tr, labelText: 'Enter_trans_password'.tr, obscureText: true,
            //   suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: ColorConstants.white54),
            //     secondary: Icon(Icons.remove_red_eye, color: ColorConstants.APP_SECONDARY_COLOR,),), validate: VALIDATE.PASSWORD,),
            //
            // TextFieldIcon(onChanged: (val){
            //   controller.transConfPassword.value = val;
            //   controller.checkDataValidity();
            // }, hintText: 'Set_6-12_digit_login_password'.tr, labelText: 'Conf_trans_password'.tr, obscureText: true,
            //   suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: ColorConstants.white54,),
            //     secondary: Icon(Icons.remove_red_eye, color: ColorConstants.APP_SECONDARY_COLOR,),)
            //   , validate: VALIDATE.PASSWORD,),

            Obx(() => TextFieldIcon(
              controller: controller.controllerOTP,
              onChanged: (val){
              controller.verCode.value = val;
              if('${controller.OTP.value}' == val){
                controller.verCode.value = FieldData(data: val.data, valid: true);
              }
              controller.checkDataValidity();
            }, onPressed: (){
              print('Send verification code');
            }, hintText: 'Enter_verification_code'.tr, labelText: 'Enter_verification_code'.tr,
              suffix: (controller.OTP.value==0?SuffixWidget(primary: TextButton(onPressed: (){
                if(controller.email.value!.valid){
                  controller.sendOTP();
                }else{
                  EasyLoading.showToast('Enter_valid_email_id'.tr);
                }
              }, child: Text('Send'.tr, style: textStyleLabel(color: controller.email.value!.valid?ColorConstants.APP_SECONDARY_COLOR:ColorConstants.white54),)),
                secondary: Text('Send'.tr, style: textStyleLabel(color: ColorConstants.white),),)
                  :SuffixWidget(primary: SACountDown(onComplete: () {
                controller.OTP.value=0;
              },))), validate: VALIDATE.OTP,)),

            TextFieldIcon(
              controller: controller.controllerInv,
              onChanged: (val){
              controller.invCode.value = val;
              controller.checkDataValidity();
            }, hintText: 'Enter_invitation_code'.tr, labelText: 'Enter_invitation_code'.tr, validate: VALIDATE.USER,),

            AcceptTerms(accept: (val){
              controller.isAccept.value = val;
              controller.checkDataValidity();
            }, texts: texts,),

            SizedBox(height: 24,),
            Obx(() => SubmitButton(title: 'Register_now'.tr, isActive: controller.isSignup.value!,
                onPressed: () => controller.register(context))),
          
            SizedBox(height: 24,),
            Align(alignment: Alignment.center, child: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
              Text('Already have an account?'),
              TextButton(onPressed: () => Get.back(), child: Text('Login', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),))
            ],),)
            // Obx(() => Text('${controller.OTP.value!>0?controller.OTP.value:''}', style: textStyleLabel(color: ColorConstants.white24),))
          ],),
      ),
    );
  }

}
