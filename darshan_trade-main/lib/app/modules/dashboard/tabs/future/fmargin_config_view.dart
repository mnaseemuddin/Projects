import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/TradeSettings/controllers/trade_settings_controller.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/shared.dart';
import '../../../TradeSettings/views/samargin_textfield.dart';
import '../controllers/ftrade_setting_controller.dart';

class FMarginConfigView extends GetView<FTradeSettingsController> {
  final bool isMartingle;

  FMarginConfigView({this.isMartingle=false});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Setup_margin_amount'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        elevation: 0,
        brightness: Brightness.dark,
        backgroundColor: Colors.transparent,
      ),
      body: Container(
        padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
        child: ListView(children: [
          SACellContainer(child: _createItems()),
          // SizedBox(height: 16,),
          Padding(padding: EdgeInsets.all(16),
            child: SubmitButton(onPressed: (){
              controller.tradeResponse.value.marginList = [];
              for (var element in controller.marginList) {
                controller.tradeResponse.value.marginList.add(
                    MarginListItem(id: element.id, calls: element.calls, times:
                    element.times, iscall: element.iscall));
              }
              controller.saveTradeValue().then((value){
                if(value) {
                  controller.onInit();
                  Get.back();
                }
              });
            }, title: 'Confirm'.tr),)
        ],),

      ),
    );
  }

  Widget _createItems(){
    List<Widget> list = [];
    list.add(Row(children: [
      Expanded(child: Container(alignment: Alignment.center, child: Text('Margin_call_drop'.tr,
        style: textStyleLabel(),),), flex: 50,),
      Expanded(child: Container(alignment: Alignment.center, child: Text('Multiply_by_in_ration'.tr, style: textStyleLabel()),), flex: 50,),
    ],));

    // Controller.marginList.forEach((element) {
    //   list.add(_item(element));
    // });
    print('Margin List Count - ${controller.marginList.length}');
    for(int i=0; i<controller.marginList.length; i++){
      MarginListItem element = controller.marginList.elementAt(i);
      list.add(_item(i+1, element));
    }

    return Column(children: list,);
  }

  Widget _item(index, MarginListItem element) => Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      SizedBox(height: 8,),
      Text(_call(index), style: textStyleLabel(),),
      SizedBox(height: 4,),
      Row(children: [
        Expanded(child: SAMarginTextField(unit: '%', controller: TextEditingController(text:
        '${element.calls}'), onChanged: (val){
          element.calls = double.parse(val);
        },),),
        SizedBox(width: 4,),
        Expanded(child: SAMarginTextField(unit: isMartingle?'Times':'USDT', controller: TextEditingController(text: '${element.times}'), isInt: true, onChanged: (val){
          double value = double.parse(val);
          if(value <20){
            EasyLoading.showToast('Amount can not be less than 20');
            element.times = int.parse(val);
            return;
          }
          element.times = int.parse(val);
        }, isDisable: isMartingle,),),
      ],)
    ],);

  String _call(int num){
    switch(num){
      case 1:
        return 'First Call';
      case 2:
        return '2nd Call';
      case 3:
        return '3rd Call';
      default:
        return '${num}th Call';
    }
  }
}


