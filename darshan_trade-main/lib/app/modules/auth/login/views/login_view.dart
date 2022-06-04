import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/common_model.dart';
import 'package:royal_q/app/modules/AffiliateApp/AffiliateSplashScreen.dart';

import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/sawidgets/app_logo.dart';
import 'package:royal_q/app/shared/sawidgets/controllers/app_text_controller.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

import '../../../AffiliateApp/auth/AffiliateLoginView.dart';
import '../controllers/login_controller.dart';

class LoginView extends GetView<LoginController> {

  @override
  Widget build(BuildContext context) {
    var controller = Get.isRegistered<LoginController>()
        ? Get.find<LoginController>()
        : Get.put(LoginController(apiRepository: Get.find()));
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      body: Container(
        padding: EdgeInsets.all(32),
        alignment: Alignment.center,
        child: ListView(
          shrinkWrap: true,
          children: [
            AppLogo(),
            SizedBox(height: 24,),

            TextFieldIcon( hintText: 'Enter_email'.tr, labelText: EMAIL, validate: VALIDATE.EMAIL, onChanged: (val){
              controller.email.value = val;
              controller.isLogin.value = controller.email.value!.valid&&controller.password.value!.valid;
              // setState(() => _isLogin = _email.valid&&_password.valid);
            }, controller: controller.controllerEmail,),
            SizedBox(height: 8,),
            TextFieldIcon(suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: ColorConstants.white54,),
                secondary: Icon(Icons.remove_red_eye, color: ColorConstants.APP_SECONDARY_COLOR,)),
              labelText: PASSWORD, hintText: 'Enter_login_password'.tr, validate: VALIDATE.PASSWORD,
              obscureText: true, onChanged: (val){
                controller.isEmailValid(val);
              }, controller: controller.controllerPassword,),

            Align(alignment: Alignment.centerRight,
              child: TextButton(onPressed: (){
                Get.toNamed(Routes.FORGOT_PASSWORD);
              }, child: Text(FORGOT_PASSWORD, style: textStyleHeading3(color: ColorConstants.white70),)),),
            SizedBox(height: 24,),
            Obx(() => SubmitButton(title: LOGIN, isActive: controller.isLogin.value,
                onPressed: (){
              controller.login(context);
                }),),

            SizedBox(height: 16,),
            TextButton(onPressed: (){
              print('Registration');
              Get.toNamed(Routes.REGISTRATION);
            }, child: Row(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text(NOT_REG, style: textStyleLabel(color: ColorConstants.white60, fontSize: 14.0),),
                Text(" "+REG_NOW, style: textStyleLabel(color: ColorConstants.blue, fontSize: 14.0),),
              ],)),
            SizedBox(height: 20,),
             SubmitButton(title: "Affiliate Login", isActive: true,
                onPressed: (){
              Get.to(const AffiliateSplashScreen());
                })
          ],
        ),
      ),
    );
  }
}
