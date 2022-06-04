import 'dart:convert';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_datetime_picker/flutter_datetime_picker.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:image_picker/image_picker.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/request/update_request.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';
import 'package:royal_q/app/shared/utils/utils.dart';

class PersonalInfoController extends GetxController {

  final ApiRepository apiRepository;
  PersonalInfoController({required this.apiRepository});

  final count = 0.obs;
  final box      = GetStorage();
  var mUsersInfo = Rxn<UserInfo>();
  var updateData = Rxn<UpdateRequest>();
  final OTP = 0.obs;
  final _showOldPass = true.obs;
  final _showNewPass = true.obs;
  final _showConfPass = true.obs;

  @override
  void onInit() {
    super.onInit();
    // usersInfo.value = box.read('user_info');
    // mUsersInfo.value = UserInfo(status: 'succeed', message: 'message', id: 1, username: 'username',
    //     password: 'password', name: 'name', referralcode: 'referralcode',
    //     countryname: 'countryname', email: 'email@test.com', pic: 'pic');
    mUsersInfo.value = userInfo;
    updateData.value = UpdateRequest(id: userInfo!.id, name: userInfo!.name, referralcode: userInfo!.referralcode, countryname: userInfo!.countryname, email: userInfo!.email);
  }

  @override
  void onReady() {
    super.onReady();
  }

  void changeName(BuildContext context)async{
    var name = await getUpdateVal(context, mUsersInfo.value!.name, 'Nickname');
    if(name !=null){
      updateData.value!.name = name;
      updateProfile();
    }
  }

  void changeLocation(BuildContext context)async{
    var name = await getUpdateVal(context, mUsersInfo.value!.name, 'Location');
    if(name !=null){
      updateData.value!.countryname = name;
      updateProfile();
    }
  }

  void changeEmail(BuildContext context)async{
    var name = await getUpdateVal(context, mUsersInfo.value!.email, 'Email');
    if(name !=null){
      updateData.value!.email = name;
      updateProfile();
    }
  }

  void updatePassword(body) async{
    EasyLoading.show();
    ApiResponse response = await changePasswordModeAPI(body);
    if(response.status){
      EasyLoading.showToast(response.data['message']);
      clearAPICache();
      Get.offAllNamed(Routes.LOGIN);
    }else{
      EasyLoading.showToast(response.data['message']);
    }
    EasyLoading.dismiss();
  }

  void updateTransPassword(body) async{
    EasyLoading.show();
    ApiResponse response = await changeTransPasswordAPI(body);
    if(response.status){
      EasyLoading.showToast(response.data['message']);
      Get.back();
    }else{
      EasyLoading.showToast(response.data['message']);
    }
    EasyLoading.dismiss();
  }

  void changePassword(context, bool isLogin) async{
    TextEditingController oldPassword   = TextEditingController();
    TextEditingController oldNewPass    = TextEditingController();
    TextEditingController confirmPass   = TextEditingController();
    TextEditingController controllerOTP = TextEditingController();

    var rData = await showDialog(context: context, builder: (context) => Dialog(

      backgroundColor: Colors.transparent,
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          color: isLogin?Colors.yellow[100]:Colors.red[50],
        ),
        padding: EdgeInsets.symmetric(vertical: 16, horizontal: 8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(children: [
              Icon(isLogin?Icons.password:Icons.list_alt_rounded, color: ColorConstants.APP_SECONDARY_COLOR, size: 50,),
              SizedBox(width: 8,),
              Expanded(child: Text(isLogin?'Change Password':'Change transaction password', style: textStyleHeading(color: ColorConstants.white),),)
            ],),

            isLogin?Obx(() => TextFormField(
              obscureText: _showOldPass.value,
              controller: oldPassword,
              decoration: InputDecoration(
                  hintText: 'old password',
                  suffixIcon: IconButton(onPressed: (){
                    _showOldPass.value = !_showOldPass.value;
                  }, icon: Icon(_showOldPass.value?Ionicons.eye_off:Ionicons.eye))
              ),
            )):SizedBox(),

            Obx(() => TextFormField(
              obscureText: _showNewPass.value,
              controller: oldNewPass,
              decoration: InputDecoration(
                  hintText: 'New password',
                  suffixIcon: IconButton(onPressed: (){
                    _showNewPass.value = !_showNewPass.value;
                  }, icon: Icon(_showNewPass.value?Ionicons.eye_off:Ionicons.eye))
              ),
            )),

            Obx(() => TextFormField(
              obscureText: _showConfPass.value,
              controller: confirmPass,
              decoration: InputDecoration(
                  hintText: 'confirm password',
                  suffixIcon: IconButton(onPressed: (){
                    _showConfPass.value = !_showConfPass.value;
                  }, icon: Icon(_showConfPass.value?Ionicons.eye_off:Ionicons.eye))
              ),
            )),


            isLogin?SizedBox():Row(children: [
              Expanded(child: TextFormField(
                controller: controllerOTP,
                decoration: InputDecoration(
                    hintText: 'Enter OTP'
                ),
              )),
              ElevatedButton(onPressed: (){
                sendOTP();
              }, child: Obx(() => OTP.value>0?Text('Re-send'):Text('send')))
            ],),

            SizedBox(height: 8,),
            Row(children: [
              Expanded(child: Container()),
              TextButton(onPressed: (){
                Get.back();
              }, child: Text('Cancel', style: textStyleLabel(color: ColorConstants.white),)),
              TextButton(onPressed: (){
                AppFocus.unfocus(context);
                // Get.back(result: oldPassword.text);

                if(isLogin&&oldPassword.text!=userInfo!.password){
                  EasyLoading.showToast('Incorrect old password');
                  return;
                }

                if(oldNewPass.text.length<5){
                  EasyLoading.showToast('Password length must be greater than 5');
                  return;
                }

                if(oldNewPass.text != confirmPass.text){
                  EasyLoading.showToast('Password do not matched');
                  return;
                }

                if((OTP.value ==0 || '${OTP.value}'!=controllerOTP.text.trim())&&!isLogin){
                  EasyLoading.showToast('Enter correct OTP');
                  return;
                }

                // Map map = {
                //   'id': userInfo!.id,
                //   'oldpassword': oldPassword.text,
                //   'newpassword': oldNewPass.text,
                // };

                if(isLogin){
                  Map map = {
                    'id': userInfo!.id,
                    'oldpassword': oldPassword.text,
                    'newpassword': oldNewPass.text,
                  };
                  updatePassword(map);
                }else{
                  Map map = {
                    'id': userInfo!.id,
                    'oldpassword': oldNewPass.text,
                    'newpassword': oldNewPass.text,
                  };
                  updateTransPassword(map);
                }

              }, child: Text('Update', style: textStyleLabel(color: ColorConstants.white),)),
            ],)
          ],),
      ),
    ),
        barrierDismissible: false);
    // return rData;
  }

  Future<String?> getUpdateVal(BuildContext context, String data, String title) async{
    TextEditingController cntName = TextEditingController(text: data);

     var rData = await showDialog(context: context, builder: (context) => Dialog(
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 16, horizontal: 8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(title, style: textStyleHeading(color: ColorConstants.TEXT_COLOR),),
            TextFormField(
              controller: cntName,
              decoration: InputDecoration(
              ),
            ),

            SizedBox(height: 8,),
            Row(children: [
              Expanded(child: Container()),
              TextButton(onPressed: (){
                Get.back();
              }, child: Text('Cancel')),
              TextButton(onPressed: (){
                AppFocus.unfocus(context);
                Get.back(result: cntName.text);
              }, child: Text('Update')),
            ],)
        ],),
      ),
    ));
     return rData;
  }

  void updateProfile() async{
    print(updateData.value!.name);
    var resp = await apiRepository.updateProfile(updateData.value!);
    print(resp?.message);
    if(resp?.status=='succeed'){
      getUserInfo(userInfo!.id);
    }
  }

  void getUserInfo(int id) async{
    final resp = await apiRepository.getUserInfo(id);
    print(resp);

    if(resp is UserInfo){
      userInfo = resp;
      mUsersInfo.value = resp;
      await box.write('user_info', resp);
    }

    refresh();
  }

  void updateDp() async{
    final ImagePicker _picker = ImagePicker();
    final PickedFile? image = await _picker.getImage(source: ImageSource.gallery);
    print(image?.readAsBytes());
    if(image!=null){
      final bytes = File(image!.path).readAsBytesSync();
      String base64Image = base64Encode(bytes);
      var resp = await apiRepository.updateDp(UpdateDpRequest(id: userInfo!.id, docBase64: base64Image));
      if(resp!=null&&resp!.status=='succeed'){
        getUserInfo(userInfo!.id);
      }else{
        EasyLoading.showToast('${resp?.message}');
      }
      print(resp?.message);
    }else{
      EasyLoading.showToast('Update cancelled');
    }
  }

  void sendOTP() async{
    OTP.value = generateOTP();
    Map body = emailBody(appName: 'Darshan', email: userInfo!.email, OTP: OTP.value, subject: ' to change transaction password');
    EasyLoading.show();
    ApiResponse resp = await sendEmailOTPAPI(body);
    EasyLoading.dismiss();
    EasyLoading.showToast('An Email send on you email ${userInfo!.email}');
    print('response => ${resp.data}');
    refresh();
  }

  @override
  void onClose() {}
}
