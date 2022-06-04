import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/auth/forgotPassword/controllers/forgot_password_controller.dart';
import 'package:royal_q/app/shared/sawidgets/app_logo.dart';
import 'package:royal_q/app/shared/sawidgets/sa_appbar.dart';
import 'package:royal_q/app/shared/sawidgets/sa_count_down.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/app/shared/widgets/app_textbox.dart';

class ForgotPasswordView extends GetView<ForgotPasswordController> {
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return AFBg(child: Scaffold(
      backgroundColor: Colors.transparent,
      // appBar: AppBar(
      //   title: Text('Forgot_Password'.tr, style: textStyleHeading2(color: ColorConstants.white),),
      //   iconTheme: IconThemeData(color: ColorConstants.white),
      //   centerTitle: true,
      //   elevation: 0,
      //   // brightness: Brightness.dark,
      //   backgroundColor: Colors.transparent,
      // ),
      body: SafeArea(child: Column(children: [
        SizedBox(height: 16,),
        SHAppbar(title: 'Forgot_Password'.tr),
        Expanded(child: Container(
          padding: EdgeInsets.symmetric(horizontal: 16),
          alignment: Alignment.center,
          // child: Controller.isEmailSend.value?_resetPassword():_forgotPassword(size),
          // child: Obx(() => IndexedStack(
          //   index: Controller.index.value,
          //   children: [
          //     _forgotPassword(size),
          //     _confirmOTP(),
          //     _resetPassword()
          //   ],))
          child: Obx(() => _getView(controller.index.value)),
        ))
      ],)),
    ));
  }

  Widget _getView(int index){
    switch(index){
      case 0:
        return _forgotPassword();
      case 1:
        return _confirmOTP();
      case 2:
        return _resetPassword();
      default:
        return _forgotPassword();
    }
  }
  
  Widget _forgotPassword() => Builder(builder: (context) {
    Size size = MediaQuery.of(context).size;
    return ListView(
      shrinkWrap: true,
      children: [

        AppLogo(),
        SizedBox(height: 40,),

        // TextFieldIcon( hintText: 'Enter_email'.tr, labelText: EMAIL, validate: VALIDATE.EMAIL, onChanged: (val){
        //   controller.email.value = val;
        //   print('${val.data}, bool ${val.valid}');
        // }, controller: controller.controllerEmail,),

        AFTextBox(label: 'Email', hint: 'Enter_email'.tr, onChanged: (val){
          controller.email.value = FieldData(data: val, valid: true);
        }, textEditingController: controller.controllerEmail,),

        SizedBox(height: 16,),
        Padding(padding: EdgeInsets.symmetric(horizontal: 16),
        child: Obx(() => AFSubmitButton(onPressed: (){
          // controller.index.value = 1;
          controller.sendOTP();
        }, title: 'Submit'.tr, isActive: controller.email.value!.valid)),),
        SizedBox(height: size.height/4,),

      ],);
  });

  Widget _resetPassword() => ListView(
    shrinkWrap: true,
    children: [

      Text('Reset_password'.tr, style: textStyleHeading(color: Colors.white),),
      SizedBox(height: 16,),

    AFTextBox(label: 'New_password'.tr, hint: 'Enter_new_password'.tr, obscureText: true, onChanged: (val){
      controller.passwordNew.value=FieldData(data: val, valid: true);
    },textEditingController: controller.controllerPasswordNew, suffix: Icon(Icons.remove_red_eye, color: Color(0xFFFF5555),), suffixSec: Icon(Icons.remove_red_eye, color: Colors.white54,),),


      AFTextBox(label: 'Confirm_password'.tr, hint: 'Confirm_password'.tr, obscureText: true, onChanged: (val){
        controller.passwordReset.value = FieldData(data: val, valid: true);
      },textEditingController: controller.controllerPasswordReset,
        suffix: Icon(Icons.remove_red_eye, color: Color(0xFFFF5555),),
        suffixSec: Icon(Icons.remove_red_eye, color: Colors.white54,),),


    //   TextFieldIcon(suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: Colors.white54,),
    //     secondary: Icon(Icons.remove_red_eye, color: Color(0xFFFF5555),)),
    //   labelText: 'New_password'.tr, hintText: 'Enter_new_password'.tr, validate: VALIDATE.PASSWORD,
    //   obscureText: true, onChanged: (val) => controller.passwordNew.value=val, controller: controller.controllerPasswordNew,),
    //
    //   SizedBox(height: 8,),
    //
    // TextFieldIcon(suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: Colors.white54,),
    //     secondary: Icon(Icons.remove_red_eye, color: Color(0xFFFF5555),)),
    //   labelText: 'Confirm_password'.tr, hintText: 'Confirm_password'.tr, validate: VALIDATE.PASSWORD,
    //   obscureText: true, onChanged: (val) => controller.passwordReset.value=val, controller: controller.controllerPasswordReset,),
    SizedBox(height: 16,),

    AFSubmitButton(onPressed: (){
      if(controller.passwordNew.value!.data==controller.passwordReset.value!.data){
        controller.changePassword();
      }else {
        EasyLoading.showToast('Password_do_not_matched'.tr);
      }
    }, title: 'Submit'.tr, isActive: true,//controller.passwordNew.value!.valid&&controller.passwordReset.value!.valid,
    )
  ],);

  Widget _confirmOTP() => Column(children: [
    Container(

      child: TextFormField(
          onChanged: (val){
            if(val == '${controller.OTP.value}'){
              controller.index.value = 2;
            }
          },
          cursorColor: Colors.white,
          style: textStyleLabel(color: Colors.white),
          keyboardType: TextInputType.number,
          textAlignVertical: TextAlignVertical.center,
          textAlign: TextAlign.center,

          decoration: InputDecoration(
              hintText: 'Please_enter_OTP'.tr,
              border: InputBorder.none,
              hintStyle: textStyleLabel(color: Colors.white38),
              suffixIcon: SACountDown(onComplete: (){
                controller.index.value = 0;
                EasyLoading.showToast('Resend_OTP'.tr);
              },))
      ),
      margin: EdgeInsets.only(top: 40),
      padding: EdgeInsets.only(left: 8),
      decoration: BoxDecoration(
          border: Border.all(width: 1, color: Colors.white24),
          borderRadius: BorderRadius.circular(30)
      ),
    ),
    SizedBox(height: 16,),
    Padding(padding: EdgeInsets.symmetric(horizontal: 16),
    child: Text('Enter_recv_opt'.trParams({'email': controller.email.value!.data}),
      style: textStyleLabel(color: Colors.white24),),)
  ],);
}
