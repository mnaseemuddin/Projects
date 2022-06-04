import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';

class DashboardController extends GetxController {

  final banners = RxList<BannerResponse>();

  final count = 0.obs;
  RxnInt bottomIndex = RxnInt(0);
  bool binanceEnable = false.obs();
  final  balance = 0.0.obs;
  final  balanceFund = 0.0.obs;
  final exchnagetype = 1.obs;
  final isBinanceReceived = false.obs;
  final topPadding = 0.0.obs;
  final OTP = 0.obs;
  final encryptionBalance=false.obs;
  final ScrollController scrollController = ScrollController();
  final USDTBalance=0.0.obs;
  final encryptionFUSDTBalance=false.obs;
  final futureTabSelected=0.obs;

  @override
  void onInit() {
    super.onInit();
    // getRevenueList();
    getXpertgainBalance();

    scrollController.addListener(() {
      topPadding.value = -scrollController.offset/2;
    });
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {}
  void increment() => count.value++;
  void updateBottomIndex(int index){
    bottomIndex.value = index;
    print('current => $index');
    refresh();
  }

  void selectFutureTab(int selected){
    futureTabSelected.value=selected;
  }

  encryptionWalletBalance(){
    if(encryptionBalance.value){
      encryptionBalance.value=false;
    }else{
      encryptionBalance.value=true;
    }
    refresh();
  }

  FUSDTWalletBalance(){
    if(encryptionFUSDTBalance.value){
      encryptionFUSDTBalance.value=false;
    }else{
      encryptionFUSDTBalance.value=true;
    }
    refresh();
  }


  Future<double> getXpertgainBalance() async{
    EasyLoading.show();
    // balance.value = 0.0;
    return await checkBinanceBalance();
    // ApiResponse response = await getApiSecretKeyAPI({
    //   'id': userInfo!.id,
    //   'exchanetype': 1
    // });
    //   if(response.status){
    //     binanceEnable = true;
    //     double balance = await checkBinanceBalance();
    //     return balance;
    //   }{
    //     EasyLoading.showToast('API not binded');
    //     return 0.0;
    //   }
  }

  Future<double> checkBinanceBalance() async{
    // ApiResponse response = await chkBalanceAPI(exchnagetype: exchnagetype.value);
    ApiResponse response = await chkBalanceAPI();
    if(response.status){
      EasyLoading.dismiss();
      isBinanceReceived.value = true;
      bool valid = double.tryParse('${response.data}')!=null;//RegExp(r"^[0-9]+$").hasMatch(response.data);
      print('isvalid: => $valid');

      balance.value = response.data=='Failed'?0.0:double.parse('${response.data}');
      getUSDTBalance();
      return balance.value;
    }else{
      EasyLoading.dismiss();
      isBinanceReceived.value = false;
      return 0.0;
    }
  }

  Future<double> getFundBalance() async{
    ApiResponse response = await getFundBalanceAPI();
    if(response.status){
      balanceFund.value = double.parse('${response.data['totalAmount']??'0.0'}');
      return balanceFund.value;
    }else{
      return 0.0;
    }
  }

  void updateUserInfo() async{
    ApiResponse response = await getProfileAPI(userInfo!.id);
    if(response.status){
      await GetStorage().write('user_info', response.data);
    }
  }

  void loadBanners() async{
    ApiResponse response = await bannerAPI();
    banners.value = response.data;
  }

  void generateOTPDialog(context, message, Function()onConfirm){
    OTP.value = 0;
    sendOTP(message);
    TextEditingController controllerOTP = TextEditingController();
    showDialog(context: context, builder: (context) => Dialog(

      backgroundColor: Colors.transparent,
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          color: Colors.yellow[100],
        ),
        padding: EdgeInsets.symmetric(vertical: 16, horizontal: 8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Generate OTP to confirm your transaction'),
            SizedBox(height: 16,),
            Row(children: [
              Expanded(child: TextFormField(
                controller: controllerOTP,
                decoration: InputDecoration(
                    hintText: 'Enter OTP'
                ),
              )),
              ElevatedButton(onPressed: (){
                sendOTP(message);
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

                if((OTP.value ==0 || '${OTP.value}'!=controllerOTP.text.trim())){
                  EasyLoading.showToast('Enter correct OTP');
                  return;
                }

                onConfirm();
                Get.back();

              }, child: Text('Submit', style: textStyleLabel(color: ColorConstants.white),)),
            ],)
          ],),
      ),
    ),
        barrierDismissible: false);
  }

  void sendOTP(String message) async{
    OTP.value = generateOTP();
    Map body = emailBody(appName: 'Darshan', email: userInfo!.email, OTP: OTP.value, subject: message);
    // Map body = emailBody(appName: 'JackBOT', email: 'zulfiqar108@gmail.com', OTP: OTP.value, subject: message);
    EasyLoading.show();
    ApiResponse resp = await sendEmailOTPAPI(body);
    EasyLoading.dismiss();
    EasyLoading.showToast('An Email send on you email ${userInfo!.email}');
    print('response => ${resp.data}');
    refresh();
  }


  Future<double> getUSDTBalance() async{
    EasyLoading.show();
    // balance.value = 0.0;
    return await checkUSDTBalance();
    // ApiResponse response = await getApiSecretKeyAPI({
    //   'id': userInfo!.id,
    //   'exchanetype': 1
    // });
    //   if(response.status){
    //     binanceEnable = true;
    //     double balance = await checkBinanceBalance();
    //     return balance;
    //   }{
    //     EasyLoading.showToast('API not binded');
    //     return 0.0;
    //   }
  }


  Future<double> checkUSDTBalance() async{
    // ApiResponse response = await chkBalanceAPI(exchnagetype: exchnagetype.value);
    ApiResponse response = await chkUSDTBalanceAPI();
    if(response.status){
      EasyLoading.dismiss();
      isBinanceReceived.value = true;
      bool valid = double.tryParse('${response.data}')!=null;//RegExp(r"^[0-9]+$").hasMatch(response.data);
      print('isvalid: => $valid');

      USDTBalance.value = response.data=='Failed'?0.0:double.parse('${response.data}');
      return USDTBalance.value;
    }else{
      EasyLoading.dismiss();
      isBinanceReceived.value = false;
      return 0.0;
    }
  }
}
