import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/modules/TradeSettings/controllers/trade_settings_controller.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'input_cell.dart';
import 'trade_setting_cell.dart';

class DistributeProfitView extends GetView<TradeSettingsController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Distributed_and_Take_Profit'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        elevation: 0,
        brightness: Brightness.dark,
        backgroundColor: ColorConstants.white10.withOpacity(0.09),
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
        child: ListView(children: [
          Container(
              color: ColorConstants.white10.withOpacity(0.09),
              padding: EdgeInsets.symmetric(horizontal: 8),
              child: Column(children: _createList(),)),
          SubmitButton(onPressed: (){}, title: 'Confirm'.tr, margin: EdgeInsets.all(16),)
        ],),
      ),
    );
  }

  List<Widget> _createList(){
    List<Widget> list = [];
    for(int i=0; i<controller.tradeResponse.value.maginCallLimit-4; i++){
      list.add(TradeSettingCell(title: '${5+i} sub-position', subView: InputCell(controller: TextEditingController(text: '1.3')), trailing: Text('%', style: textStyleLabel(),),isDivider: false,),);
      i<controller.tradeResponse.value.maginCallLimit-1?list.add(Divider(color: ColorConstants.white10,)):SizedBox();
    }

    return list;
  }
}
