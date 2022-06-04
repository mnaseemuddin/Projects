


import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/TradeSettings/views/distribute_profit_view.dart';
import 'package:royal_q/app/modules/TradeSettings/views/margin_config_view.dart';
import 'package:royal_q/app/modules/TradeSettings/views/trade_setting_cell.dart';
import 'package:royal_q/app/modules/dashboard/dashboard.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'package:intl/intl.dart';

import '../../../TradeSettings/views/input_cell.dart';
import '../controllers/ftrade_setting_controller.dart';
import 'fmargin_config_view.dart';

class FTradeSettingsView extends GetView<FTradeSettingsController> {

  String contVal = '0';


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Trade Settings', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        brightness: Brightness.dark,
        elevation: 0,
        backgroundColor: ColorConstants.whiteRev,

        actions: [
          TextButton(onPressed: (){
            AppFocus.unfocus(context);
            controller.saveTradeValue().then((value){
              controller.onInit();
              if(value)Get.back();
            });
            //API Save Time Error
            //{id: 1, firstBuy: 15.0, openPosition: 0, maginCallLimit: 10, wholePRatio: 1.2, wholePCallback: 0.3, buyCallback: 0.5, presetBuyprice: 0.0, stopCloseprice: 0.0, selllosspositin: 0.0, status: succeed, symbol: INCHUSDT, ExchangeType: 1, marginList: []}
            // I/flutter ( 6974): http://rpc.darshantrade.com/api/account/updateTradeSetting
            // I/flutter ( 6974): {message: null}

            //Save Successfully
            //{id: 1, firstBuy: 1000.0, openPosition: 0, maginCallLimit: 1, wholePRatio: 0.3, wholePCallback: 0.01, buyCallback: 0.1, presetBuyprice: 0.0, stopCloseprice: 0.0, selllosspositin: 0.01, status: succeed, symbol: ADAUSDT, ExchangeType: 1, marginList: [{id: 1889, calls: 3.5, times: 20, iscall: 0}]}
            // I/flutter ( 6974): http://rpc.darshantrade.com/api/account/updateTradeSetting
            // I/flutter ( 6974): {status: succeed, message: Update Successfully !}
          }, child: Text('Save'.tr, style: textStyleHeading2(),))
        ],
      ),
      backgroundColor: Color(0xFF545454),
      body: Stack(children: [
        Positioned(
          top: 0,left: 0, right: 0,
          child: Container(
            padding: EdgeInsets.symmetric(horizontal: 8,vertical: 8),
            // color: Colors.orange[50],
            color: ColorConstants.whiteRev,
            child: Row(children: [
              Icon(Icons.info, color: ColorConstants.orange,),
              SizedBox(width: 8,),
              Expanded(child: Text('The first but in amount is calculated according to the currency pair, principle and trade unit.margin call limit has to be greater than Sell Loss Position.'.tr, style:
              textStyleLabel(color: ColorConstants.orange, fontSize: 12.0),))
            ],),),
        ),
        Positioned(
            top: 60,left: 0, right: 0,bottom: 0,
            child: Container(
              padding: EdgeInsets.symmetric(horizontal: 16),
              child: ListView(children: [

                Obx(() => SACellContainer(child: TradeSettingCell(title: 'First_Buy_in_amount'.tr, subView: InputCell(controller: TextEditingController(text: '${controller.tradeResponse.value.firstBuy}'), onChanged: (val){
                  controller.tradeResponse.value.firstBuy = double.parse(val);
                },), trailing: Text('USDT', style: textStyleLabel(),), isDivider: false,),)),
                SACellContainer(child: Obx(() => TradeSettingCell(title: controller.switchValue.value?'Martingale strategy':'Custom amount', trailing: CupertinoSwitch(
                  activeColor: ColorConstants.APP_SECONDARY_COLOR,
                  value: controller.switchValue.value,
                  onChanged: (value) {
                    controller.updatePositionModestatus(value);
                    // controller.switchValue.value = value;
                    // controller.tradeResponse.value.openPosition = value?1:0;
                  },
                ), isDivider: false,)),),

                Obx(() => SACellContainer(child: Column(children: [
                  TradeSettingCell(title: 'Margin_call_limit'.tr, subView: InputCell(

                    onContChanged: (val){
                      contVal = val;

                    },

                    controller:
                    TextEditingController(text: '${controller.tradeResponse.value.maginCallLimit}'),
                    isOnComplete: true,
                    onChanged: (val){
                      _saveChangedValue(val);
                      // int count = int.parse(val);
                      // if(count<=0)return;
                      // printInfo(info: 'val => $val');
                      // controller.tradeResponse.value.maginCallLimit = int.parse(val);
                      // if(count > controller.marginList.length){
                      //   int diff = count - controller.marginList.length;
                      //   print(diff);
                      //   int len = controller.marginList.length;
                      //   print(len);
                      //   for(int i=0; i<diff; i++){
                      //     MarginListItem item = len+i<controller.defaultTradeResponse.value.
                      //     marginList.length?controller.defaultTradeResponse.value.
                      //     marginList.elementAt(len+i):controller.
                      //     defaultTradeResponse.value.marginList.last;
                      //    // print('Margin List  - ${item.calls}');
                      //     controller.marginList.add(MarginListItem(id: item.id+i, calls: item.calls,
                      //         times: item.times, iscall: item.iscall));
                      //   }
                      // }
                      //
                      // if(count < controller.marginList.length){
                      //   int diff = controller.marginList.length - count;
                      //   for(int i=diff; i>0; i--){
                      //     controller.marginList.remove(controller.marginList.last);
                      //   }
                      // }

                    },isInt: true, minVal: 0,), trailing: Text('Times', style: textStyleLabel(),),),

                  Obx(() => controller.switchValue.value?TradeSettingCell(title: 'Martingale setup'.tr, trailing: Icon(Icons.chevron_right, color: ColorConstants.white,size: 40,),onPressed: () => Get.to(MarginConfigView(isMartingle: true,)),)
                      :TradeSettingCell(title: 'Custom amount setup'.tr,
                    trailing: Icon(Icons.chevron_right, color: ColorConstants.white,size: 40,)
                    ,onPressed: (){
                      AppFocus.unfocus(context);
                      _saveChangedValue(contVal);
                      Get.to(FMarginConfigView());
                    },),),

                  Obx(() => TradeSettingCell(title: controller.profileTrigger.value.tr, subView: InputCell(
                    controller: TextEditingController(text: '${controller.tradeResponse.value.wholePRatio}'), onChanged: (val){
                    controller.tradeResponse.value.wholePRatio = double.parse(val);
                  },),
                    trailing: Text('%', style: textStyleLabel(),),),),
                  TradeSettingCell(title: controller.profitPosition.value.tr, subView: InputCell(
                    controller: TextEditingController(text: '${controller.tradeResponse.value.wholePCallback}'), onChanged: (val){
                    controller.tradeResponse.value.wholePCallback = double.parse(val);
                  },),
                    trailing: Text('%', style: textStyleLabel(),),),

                  //Add New
                  TradeSettingCell(title: controller.lossPosition.value.tr, subView: InputCell(
                    controller: TextEditingController(text: '${controller.tradeResponse.value.selllosspositin}'), onChanged: (val){
                    controller.tradeResponse.value.selllosspositin= double.parse(val);
                  },),
                    trailing: Text('%', style: textStyleLabel(),),),
                  //Exit



                  // TradeSettingCell(title: 'Custom amount setup'.tr, trailing: Icon(Icons.chevron_right, color: ColorConstants.white,size: 40,),onPressed: controller.switchValue.value?null:() => Get.to(MarginConfigView()),),
                  // TradeSettingCell(title: 'Martingale setup'.tr, trailing: Icon(Icons.chevron_right, color: ColorConstants.white,size: 40,),onPressed: () => Get.to(MarginConfigView(isMartingle: true,)),),

                  // TradeSettingCell(title: 'Custom amount setup'.tr, trailing: Icon(Icons.chevron_right, color: ColorConstants.white,size: 40,),onPressed: () => Get.to(MarginConfigView()),),

                  TradeSettingCell(title: controller.buySellCallBack.value.tr, subView: InputCell(controller: TextEditingController(text: '${controller.tradeResponse.value.buyCallback}'), onChanged: (val){
                    controller.tradeResponse.value.buyCallback = double.parse(val);
                  },), trailing: Text('%', style: textStyleLabel(),),),
                  // controller.tradeResponse.value.maginCallLimit>4?TradeSettingCell(title: 'Margin_Configuration'.tr, trailing: Icon(Icons.chevron_right, color: ColorConstants.white, size: 40,), onPressed: () => Get.to(DistributeProfitView()),):SizedBox(),
                  // controller.tradeResponse.value.maginCallLimit>4?TradeSettingCell(title: 'Sub-position_take_profit_callback'.tr, subView: InputCell(controller: TextEditingController(text: '${controller.tradeResponse.value.subPCallback}'), onChanged: (val){
                  //   controller.tradeResponse.value.subPCallback = double.parse(val);
                  // },), trailing: Text('%', style: textStyleLabel(),),):SizedBox(),


                  //Add New Column Loss Callback Trigger

                  TradeSettingCell(title: controller.lossCallBackTrigger.value.tr, subView:
                  InputCell(controller: TextEditingController(text:
                  '${controller.tradeResponse.value.lossCallBackTrigger}'), onChanged: (val){
                    controller.tradeResponse.value.lossCallBackTrigger = double.parse(val);
                  },), trailing: Text('%', style: textStyleLabel(),),),

                  //End

                  //Add New ParaMeter Excess Up Trigger

                  TradeSettingCell(title: controller.excessUpTrigger.value.tr, subView:
                  InputCell(controller: TextEditingController(text:
                  '${controller.tradeResponse.value.excessUpTrigger}'), onChanged: (val){
                    controller.tradeResponse.value.excessUpTrigger = double.parse(val);
                  },), trailing: Text('%', style: textStyleLabel(),),),
                  //End
                  // TradeSettingCell(title: 'Preset_buy_price'.tr, subView:
                  // InputCell(controller: TextEditingController(text: '${controller.tradeResponse.value.presetBuyprice}'), onChanged: (val){
                  //   controller.tradeResponse.value.presetBuyprice = double.parse(val);
                  // },), trailing: Text('', style: textStyleLabel(),),),
                  //
                  //
                  // TradeSettingCell(title: 'Stop_close_price'.tr, subView: InputCell(controller: TextEditingController(text: '${controller.tradeResponse.value.stopCloseprice}'), onChanged: (val){
                  //   controller.tradeResponse.value.stopCloseprice = double.parse(val);
                  // },), trailing: Text('', style: textStyleLabel(),), isDivider: false,),

                ],))),
                SACellContainer(
                  padding: EdgeInsets.symmetric(vertical: 4),
                  child: TextButton.icon(onPressed: (){
                    controller.getTradeValues(defUser: true);
                  },
                      icon: Icon(Icons.refresh, color: ColorConstants.white,),
                      label: Text('Auto_trade_strategy'.tr, style: textStyleHeading2(),)),)
              ],),))
      ],),
    );
  }

  _saveChangedValue(val){
    int count = int.parse(val);
    if(count<=0)return;
    printInfo(info: 'val => $val');
    controller.tradeResponse.value.maginCallLimit = int.parse(val);
    if(count > controller.marginList.length){
      int diff = count - controller.marginList.length;
      print(diff);
      int len = controller.marginList.length;
      print(len);
      for(int i=0; i<diff; i++){
        MarginListItem item = len+i<controller.defaultTradeResponse.value.
        marginList.length?controller.defaultTradeResponse.value.
        marginList.elementAt(len+i):controller.
        defaultTradeResponse.value.marginList.last;
        // print('Margin List  - ${item.calls}');
        controller.marginList.add(MarginListItem(id: item.id+i, calls: item.calls,
            times: item.times, iscall: item.iscall));
      }
    }

    if(count < controller.marginList.length){
      int diff = controller.marginList.length - count;
      for(int i=diff; i>0; i--){
        controller.marginList.remove(controller.marginList.last);
      }
    }
  }
}


