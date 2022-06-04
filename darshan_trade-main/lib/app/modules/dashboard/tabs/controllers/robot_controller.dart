import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';

import 'currency_controller.dart';

class RobotController extends GetxController{

  final CurrencyController currencyController = Get.find<CurrencyController>();
  final listCurrencySearch = Rxn<List<CurrencyModel>>();
  final TextEditingController controllerText = TextEditingController();
  final dollarValue = 0.0.obs;
  final buttonActive = true.obs;

  // final tradeStatusResponse = TradeStatusResponse(sell: 0, buy: 0, cycleShotMode: 0, strategyMode: 0, trademode: 0, marginLimit: 0, status: 'succeed').obs;

  @override
  void onInit() {
    super.onInit();
    // listCurrencySearch.value = currencyController.listBinance.value;
    print('RobotController');
  }

  @override
  void onClose(){
    print('OnResume called');
  }

  void onResumed(){
    print('OnResume called');
  }

  void changeStrategyMode(body) async{
    buttonActive.value = false;
    EasyLoading.show();
    ApiResponse response = await changeStrategyModeAPI(body);
    if(response.status){}
    buttonActive.value = true;
    EasyLoading.dismiss();
    EasyLoading.showToast(response.data['message']);
  }

  void updateClearAtmMode(CurrencyModel model) async{

    Map map = {
      'id': userInfo!.id,
      'Symbol': model.symbol,
      'ExchangeType': exchangeValue
    };

    EasyLoading.show();
    ApiResponse response = await updateClearAtmModeAPI(map);
    if(response.status){
      EasyLoading.showToast(response.data['message']);
      Get.back();
    }
    EasyLoading.dismiss();
    print(model);
  }

}
