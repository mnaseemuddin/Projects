import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';

class GetBoosterController extends GetxController {

  final model = Rxn<CurrencyModel>();
  final dynamic argumentData = Get.arguments;

  final boosterUserListResponse = RxList<BoosterUserListResponse>();
  final tabIndex = 0.obs;

  // {"symbol":"LINKUSDT","price":"23.94000000","boosterMode":0,"qunatity":"0.00000000"}

  // final boosterCurResponse = Rxn<BoosterCurResponse>();
  final boosterCurResponse = BoosterCurResponse(symbol: 'BTCUSDT', price: '0.0', boosterMode: 0, qunatity: '0.0').obs;

  @override
  void onInit(){
    super.onInit();
    model.value = argumentData[0]['model'];
    boosterCurResponse.value = BoosterCurResponse(symbol: model.value!.symbol, price: model.value!.lastPrice, boosterMode: 0, qunatity: model.value!.lastQty);
    getBoosterUserList();
    getUserBoosterCur();
  }

  void setBooster(BoosterUserListResponse mModel){
    boosterCurResponse.value = BoosterCurResponse(symbol: mModel.symbol, price: '${mModel.price}', boosterMode: mModel.boosterMode, qunatity: '${mModel.quantity}');
  }

  void getBoosterUserList() async{
    ApiResponse apiResponse = await getBoosterUserListAPI();
    boosterUserListResponse.value = apiResponse.data;
  }

  void boosterModeSetting(symbol, method) async{

    Map body = {
      'userid': userInfo!.id,
      'Symbol': symbol,
      'Exchangetype': exchangeValue
    };


    ApiResponse response = await boosterModeSettingAPI(body, method);
    if(response.status){
      EasyLoading.showToast(response.data['message']);
      Get.back();
      // tabIndex.value = 1;
    }else{
      EasyLoading.showToast(response.data['message']);
    }
  }

  void getUserBoosterCur()async{
    ApiResponse response = await getUserBoosterCurAPI(model.value!.symbol);
    if(response.status){
      boosterCurResponse.value = response.data;
    }
  }
}
