import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/views/dashboard_view.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:get_storage/get_storage.dart';

class LoginController extends GetxController {
  final ApiRepository apiRepository;
  LoginController({required this.apiRepository});
  final box      = GetStorage();
  var  isLogin   = RxBool(false);
  TextEditingController controllerEmail    = TextEditingController();
  TextEditingController controllerPassword = TextEditingController();

  var email = Rxn<FieldData>();
  var password = Rxn<FieldData>();

  @override
  void onInit() {
    super.onInit();
    email.value    = FieldData(data: 'sher1@ali.com', valid: false);
    password.value = FieldData(data: '123456', valid: false);
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }

  void isEmailValid(FieldData val){
    password.value = val;
    isLogin.value = email.value!.valid&&password.value!.valid;
    refresh();
  }

  void login(BuildContext context)async{
    AppFocus.unfocus(context);
    // Get.toNamed(Routes.DASHBOARD);
    isToast = false;
    final res = await apiRepository.login(
      LoginRequest(username: email.value!.data,
          Password: password.value!.data
      ),
    );

    printInfo(info: res.toString());
    if(res?.status == "succeed"){
      print(res?.message);
      setAccountType("Bot").then((value){
        getUserInfo(res!.id);
      });
    }else{
      EasyLoading.dismiss();
    }
    EasyLoading.showToast(res?.message??'');
  }

  void getUserInfo(int id) async{
    isToast = false;
    final resp = await apiRepository.getUserInfo(id);
    print(resp);
    if(resp is UserInfo){
      await box.write('user_info', resp);
      userInfo = resp;
      Get.offAllNamed(Routes.DASHBOARD);
    }else{
      EasyLoading.showToast('Try again');
    }
  }
}
