import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/modules/TradeSettings/controllers/trade_settings_controller.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/shared.dart';

class BuySellDialog extends GetView<TradeSettingsController> {
  final bool isBuy;
  final String symbol;
  const BuySellDialog({Key? key, required this.isBuy,required this.symbol, }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    isBuy?controller.getBuyMode(0):controller.getSellMode();
    return isBuy?_buyDialog(context):_sellDialog(context);
  }

  Widget _buyDialog(context){
    return Dialog(

      backgroundColor: ColorConstants.APP_PRIMARY_COLOR,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 8),
        child: Obx(() => ListView(
          shrinkWrap: true,
          children: [
            SizedBox(height: 16,),
            Align(child: Text('Buy'.tr, style: textStyleHeading(),),),
            SizedBox(height: 8,),
            Divider(),
            _createRowChild('Position_amounts'.tr, '${controller.getBuyModeResponse.value.positionamt} USDT'),
            _createRowChild('Avg_price'.tr, '${controller.getBuyModeResponse.value.avgprice} USDT'),
            _createRowChild('Position_quanty'.tr,'${controller.getBuyModeResponse.value.positionquantity} $symbol'),
            _createRowChild('Current_price'.tr, '${controller.getBuyModeResponse.value.currentprice} USDT'),
            _createRowChild('PPAL'.tr, '${controller.getBuyModeResponse.value.positionprofitloss}%'),
            Divider(),
            _createRowChild('Estimated_Avg_price'.tr, '${controller.getBuyModeResponse.value.estimatedavgprice}'),

            _createRowChild('Estimated_holding_profit_and_loss'.tr, '${controller.getBuyModeResponse.value.estimatedholdprofitloss}'),
            // _createRowChild('Trigger price(Buy at market price after reading the trigger price)', '${controller.getBuyModeResponse.value.estimatedholdprofitloss}'),
            /**
            Row(children: [
              Expanded(child: _textBoxContainer(TextFormField(
                controller: controller.triggerPriceController,
                decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Please enter trigger price',
                    hintStyle: textStyleLabel()
                ),
              )), flex: 70,),
              SizedBox(width: 4,),
              Expanded(child: _textBoxContainer(TextFormField(
                controller: controller.marketPriceController,
                decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Market price',
                    hintStyle: textStyleLabel()
                ),
              )), flex: 30,),
            ],),
            **/
            //
            _createRowChild('Amount_of_margin_call'.tr, ''),
            //
            Row(children: [
              Expanded(child: _textBoxContainer(TextFormField(
                controller: controller.purchaseAmountController,
                onChanged: (val){
                  controller.getBuyMode(val.isNotEmpty?val:0);
                },
                onEditingComplete: (){
                  controller.getBuyMode(controller.purchaseAmountController.value.text);
                },
                keyboardType: TextInputType.numberWithOptions(
                  decimal: true,
                  signed: false),
                decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Enter amount'.tr
                ),
              )), flex: 70,),
              SizedBox(width: 4,),
              SACellContainer(child: Text('USDT'))
            ],),

            // SizedBox(height: 16,),

            // _createRowChild('Remaining position amount', '14.68USDT'),

            // Divider(),
            //
            // Row(
            //   mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            //   children: [
            //     SACellContainer(child: Text('25%')),
            //     SACellContainer(child: Text('50%')),
            //     SACellContainer(child: Text('75%')),
            //     SACellContainer(child: Text('100%')),
            //   ],),

            Divider(),
            Row(children: [
              Expanded(child: TextButton(onPressed: (){
                Get.back();
              }, child: Text('Cancel'.tr, style: textStyleHeading3()))),
              Container(width: 1, height: 40, color: Colors.white10,),
              Expanded(child: TextButton(onPressed: (){
                if(controller.purchaseAmountController.value.text.isEmpty){
                  EasyLoading.showToast('Enter amount'.tr);
                  return;
                }
                AppFocus.unfocus(context);
                controller.updateBuySellMode(isBuy: true,
                    amount: double.parse(controller.purchaseAmountController.value.text??'0.0'));
              }, child: Text('Confirm'.tr, style: textStyleHeading3(color: ColorConstants.APP_SECONDARY_COLOR),))),
              // Expanded(child: Container(alignment: Alignment.center, child: Text('Cancel'),)),
              // Container(width: double.infinity, color: Colors.white10,),
              // Expanded(child: Container(alignment: Alignment.center, child: Text('Confirm', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),),)),
            ],)

          ],)),
      ),
      // backgroundColor: Colors.transparent,
      // child: SACellContainer(child: Column(
      //   mainAxisSize: MainAxisSize.min,
      //   children: [
      //     Text('BUY', style: textStyleHeading(),),
      //   Divider(),
      //   _createRowChild('Position amount', '14.68USDT'),
      //   _createRowChild('Avg price', '14.68USDT'),
      //   _createRowChild('Position quantity', '0.378ATOM'),
      //   _createRowChild('Current price', '0.378ATOM'),
      //   _createRowChild('Position profit and loss', '-27.2%'),
      //   Divider(),
      //   _createRowChild('Estimated Avg price', '-27.2%'),
      //   _createRowChild('Trigger price(Buy at market price after reading the trigger price)', '-27.2%'),
      //
      //   Row(children: [
      //     Expanded(child: _textBoxContainer(TextFormField(
      //       decoration: InputDecoration(
      //         border: InputBorder.none,
      //         hintText: 'Please enter trigger price'
      //       ),
      //     )), flex: 70,),
      //     SizedBox(width: 4,),
      //     Expanded(child: _textBoxContainer(TextFormField(
      //       decoration: InputDecoration(
      //           border: InputBorder.none,
      //           hintText: 'Market price'
      //       ),
      //     )), flex: 30,),
      //   ],),
      //   //
      //   _createRowChild('Amount of margin call', ''),
      //   //
      //   Row(children: [
      //     Expanded(child: _textBoxContainer(TextFormField(
      //       decoration: InputDecoration(
      //           border: InputBorder.none,
      //           hintText: 'Please enter purchased amount'
      //       ),
      //     )), flex: 70,),
      //     SizedBox(width: 4,),
      //     Expanded(child: _textBoxContainer(TextFormField(
      //       decoration: InputDecoration(
      //           border: InputBorder.none,
      //           hintText: 'USDT'
      //       ),
      //     )),flex: 30,),
      //   ],),
      //
      //   SizedBox(height: 16,),
      //
      //     _createRowChild('Remaining position amount', '14.68USDT'),
      //
      //   Divider(),
      //
      //   Row(
      //     mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      //     children: [
      //     SACellContainer(child: Text('25%')),
      //     SACellContainer(child: Text('25%')),
      //     SACellContainer(child: Text('25%')),
      //     SACellContainer(child: Text('25%')),
      //   ],),
      //
      //   Divider(),
      //   // Container(height: 1, width: double.infinity, color: Colors.white10,),
      //
      //
      //
      //   Row(children: [
      //     Expanded(child: TextButton(onPressed: (){}, child: Text('Cancel', style: textStyleHeading3()))),
      //     Container(width: 1, height: 40, color: Colors.white10,),
      //     Expanded(child: TextButton(onPressed: (){}, child: Text('Confirm', style: textStyleHeading3(color: ColorConstants.APP_SECONDARY_COLOR),))),
      //     // Expanded(child: Container(alignment: Alignment.center, child: Text('Cancel'),)),
      //     // Container(width: double.infinity, color: Colors.white10,),
      //     // Expanded(child: Container(alignment: Alignment.center, child: Text('Confirm', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),),)),
      //   ],)
      //
      // ],)),
    );
  }

  Widget _sellDialog(context){
    return Dialog(
      backgroundColor: ColorConstants.APP_PRIMARY_COLOR,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 8),
        child: Obx(() => ListView(
          shrinkWrap: true,
          children: [
            SizedBox(height: 16,),
            Align(child: Text('Sell'.tr, style: textStyleHeading(),),),
            SizedBox(height: 8,),
            Divider(),
            _createRowChild('Position_amounts'.tr, '${controller.getSellModeResponse.value.positionamt} USDT'),
            _createRowChild('Avg_price'.tr, '${controller.getSellModeResponse.value.avgprice} USDT'),
            _createRowChild('Position_quanty'.tr,'${controller.getSellModeResponse.value.positionquantity} $symbol'),
            _createRowChild('Current_price'.tr, '${controller.getSellModeResponse.value.currentprice} USDT'),
            _createRowChild('PPAL'.tr, '${controller.getSellModeResponse.value.positionprofitloss}%'),
            Divider(),

            _createRowChild('Amount of margin call', ''),
            //
            Row(children: [
              Expanded(child: _textBoxContainer(TextFormField(
                controller: controller.purchaseAmountController,
                keyboardType: TextInputType.numberWithOptions(
                    decimal: true,
                    signed: false),
                decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Enter amount'.tr
                ),
              )), flex: 70,),
              SizedBox(width: 4,),
              SACellContainer(child: Text(symbol))
              // Expanded(child: _textBoxContainer(TextFormField(
              //   decoration: InputDecoration(
              //       border: InputBorder.none,
              //       hintText: 'USDT'
              //   ),
              // )),flex: 30,),
            ],),

            // SizedBox(height: 16,),
            //
            // _createRowChild('Remaining position amount', '14.68USDT'),

            Divider(),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                GestureDetector(child: SACellContainer(child: Text('25%')), onTap: (){
                  controller.binanceQunatityPercent(25);
                  // controller.purchaseAmountController.text = '${1*controller.getSellModeResponse.value.positionquantity/4}';
                },),
                GestureDetector(child: SACellContainer(child: Text('50%')), onTap: (){
                  controller.binanceQunatityPercent(50);
                  //controller.purchaseAmountController.text = '${2*controller.getSellModeResponse.value.positionquantity/4}';
                },),
                GestureDetector(child: SACellContainer(child: Text('75%')), onTap: (){
                  controller.binanceQunatityPercent(75);
                  // controller.purchaseAmountController.text = '${3*controller.getSellModeResponse.value.positionquantity/4}';
                },),
                GestureDetector(child: SACellContainer(child: Text('100%')), onTap: (){
                  controller.binanceQunatityPercent(100);
                  // controller.purchaseAmountController.text = '${4*controller.getSellModeResponse.value.positionquantity/4}';
                },),
              ],),

            Divider(),
            // Container(height: 1, width: double.infinity, color: Colors.white10,),

            Row(children: [
              Expanded(child: TextButton(onPressed: (){
                Get.back();
              }, child: Text('Cancel'.tr, style: textStyleHeading3()))),
              Container(width: 1, height: 40, color: Colors.white30,),
              Expanded(child: TextButton(onPressed: (){
                // Get.back();
                // updateClearModeAPI
                controller.updateClearMode(symbol);
              }, child: Text('Clear Mode'.tr, style: textStyleHeading3()))),

              Container(width: 1, height: 40, color: Colors.white30,),
              Expanded(child: TextButton(onPressed: (){
                if(controller.purchaseAmountController.value.text.isEmpty){
                  EasyLoading.showToast('Enter_amount'.tr);
                  return;
                }
                controller.updateBuySellMode(isBuy: false,
                    amount: double.parse(controller.purchaseAmountController.value.text));

              }, child: Text('Confirm'.tr, style: textStyleHeading3(color: ColorConstants.APP_SECONDARY_COLOR),))),
            ],)

          ],)),
      ),
    );
  }




  Widget _createRowChild(child1, child2) => Padding(padding: EdgeInsets.symmetric(vertical: 8),
  child: Row(children: [
    Expanded(child: Text(child1),),
    SizedBox(width: 8,),
    child2 is String?Text(child2):child2,
  ],),);

  Widget _textBoxContainer(child) => SACellContainer(
    child: child,
    padding: EdgeInsets.symmetric(horizontal: 8),
    // decoration: BoxDecoration(
    //   color: Colors.white.withOpacity(0.05),
    // ),
  );
}
