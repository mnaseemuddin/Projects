import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/tabs/circle/robot_transaction.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/robot_controller.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:transparent_image/transparent_image.dart';

import '../home/transaction_record.dart';

class RobotDeposit extends GetView<RobotController> {
  final CurrencyModel model;
  const RobotDeposit({Key? key, required this.model, }) : super(key: key);

  @override
  Widget build(BuildContext context) {

    String title = model.symbol.replaceAll('USDT', '').replaceAll('DOWN', '').replaceAll('UP', '');
    return Scaffold(
      appBar: AppBar(title: Text('Robot', style: textStyleHeading(),),
      actions: [
        TextButton(onPressed: () => Get.to(RobotTransactionRecord()),
            child: Text('Transaction_record'.tr,
          style: textStyleLabel(color: ColorConstants.black),)),
      ],
      iconTheme: IconThemeData(color: ColorConstants.white),
      elevation: 0, backgroundColor: Colors.transparent,),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 16),
        child: ListView(children: [

          SizedBox(height: 16,),

          Row(children: [

            FadeInImage.memoryNetwork(
              placeholder: kTransparentImage,
              image: 'https://xpertgain.io/symbol/${model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
              width: 40, height: 40,
              imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
            ),

            // loadImage('assets/images/icon/${controller.model.value.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png', Size(40, 40)),
            SizedBox(width: 12,),
            Text(title, style: textStyleHeading(color: ColorConstants.white),),
            Text('/USDT', style: textStyleHeading3(color: ColorConstants.white),),
          ],),

          ItemView(title: 'Price_:'.tr, child: Text(model.lastPrice, style: textStyleHeading2(color: ColorConstants.white))),
          ItemView(title: 'Amount'.tr,  child: Container(
            padding: EdgeInsets.only(left: 8),
            child: TextField(
              controller: controller.controllerText,
              style: textStyleHeading2(color: ColorConstants.white),

              keyboardType: TextInputType.numberWithOptions(decimal: true),
              inputFormatters: [
                FilteringTextInputFormatter.allow(RegExp(r"[0-9.]")),
                TextInputFormatter.withFunction((oldValue, newValue) {
                  try {
                    final text = newValue.text;
                    if (text.isNotEmpty) double.parse(text);
                    return newValue;
                  } catch (e) {
                    print(e.toString());
                  }
                  return oldValue;
                }),
              ],
              onChanged: (val){
                print('val => $val');
                if(val.isNotEmpty) {
                  // setState(() {
                    controller.dollarValue.value = double.parse(val);
                  //   // double total = double.parse(val)/_currencyValue;
                  // });
                }
              },

              decoration: InputDecoration(
                border: InputBorder.none,
                hintText: 'Enter_amount_USDT'.tr,
                hintStyle: TextStyle(color: ColorConstants.white30),
              ),
            ),
            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: ColorConstants.APP_SECONDARY_COLOR, width: 1)
            ),
          )),

          ItemView(title: 'Net_Amount'.tr,  child: Obx(() => Text('${controller.dollarValue.value/double.parse(model.lastPrice)} $title', style: textStyleHeading2(color: ColorConstants.white),))),
          SizedBox(height: 32,),
          Obx(() => SubmitButton(onPressed: (){
            if(double.parse(controller.controllerText.text)>0){
              double amount = double.parse(controller.controllerText.text);
              if(amount<100){
                EasyLoading.showToast('Enter minimum 100 USDT');
                return;
              }

              Map body = {
                'userid': userInfo!.id,
                'Symbol': model.symbol,
                'Exchangetype': exchangeValue,
                'amount': double.parse(controller.controllerText.text)
              };

              controller.changeStrategyMode(body);

            }else{
              EasyLoading.showToast('Enter correct amount');
            }
          }, title: model.strategyMode==0?'Start':'Pause', isActive: controller.buttonActive.value,)),

          SizedBox(height: 16,),

          model.strategyMode==1?SubmitButton(onPressed: (){
            controller.updateClearAtmMode(model);
          }, title: 'Stop'):SizedBox(),

        ],),
      ),
    );
  }
}
