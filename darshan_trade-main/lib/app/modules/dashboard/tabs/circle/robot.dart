import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/tabs/circle/robot_deposite.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/robot_controller.dart';
import 'package:royal_q/app/shared/shared.dart';

class Robot extends GetView<RobotController> {

  var mcontroller = Get.isRegistered<RobotController>()
      ? Get.find<RobotController>()
      : Get.put(RobotController());

  @override
  Widget build(BuildContext context) {
    controller.listCurrencySearch.value = controller.currencyController.listBinance.value;

    return Container(
      padding: EdgeInsets.symmetric(horizontal: 0),
      child: Column(children: [
        SizedBox(height: 16,),
      SizedBox(child: SASearch(onChange: (val){
        controller.listCurrencySearch.value = controller.currencyController.listBinance.value!.where((element) =>
            element.symbol.replaceAll('USDT', '').toLowerCase().contains(val.toLowerCase())).toList();
      },),),
      SizedBox(height: 8,),
      Expanded(child: Obx(() => controller.listCurrencySearch.value!=null?ListView.builder(itemBuilder: (context, index){
        CurrencyModel model = controller.listCurrencySearch.value!.elementAt(index);
        return CurrencyCell(model: model, onPress: (){
          Get.to(RobotDeposit(model: model,));
        },
        );
      }, itemCount: controller.listCurrencySearch.value!.length,):SizedBox()))
    ],),);
    // return QuantitativeCell(currencyModels: Controller.currencyController.listBinance.value??[], exchangeType: 1, isShowTab: false,);
  }
}
