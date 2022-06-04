import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/TradeSettings/controllers/trade_settings_controller.dart';
import 'package:royal_q/app/modules/TradeSettings/views/buy_sell_view.dart';
import 'package:royal_q/app/modules/TradeSettings/views/trade_settings_view.dart';
import 'package:royal_q/app/modules/dashboard/dashboard.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:transparent_image/transparent_image.dart';

class CurrencyDetailView extends GetView<TradeSettingsController> {
  final int exchangeType;
  CurrencyDetailView({required this.exchangeType});

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    String title = controller.model.value.symbol.replaceAll('USDT', '').replaceAll('DOWN', '').replaceAll('UP', '');
    return Scaffold(
      appBar: AppBar(
        elevation: 0,

        brightness: Brightness.dark,
        // backgroundColor: const Color(0xFFF87E02),
        backgroundColor: const Color(0xFFF87E02),
        actions: [
          TextButton(onPressed: () => Get.to(TransactionRecord(symbol: controller.model.value.symbol,
            exchangeType: exchangeValue,)), child: Text('Transaction_record'.tr,
            style: textStyleLabel(color: ColorConstants.whiteRev),)),
        ],
      ),
      body: Stack(children: [
        Obx(() => Positioned(
          top: controller.topPosition.value,
          left: 0, right: 0,
          child: Container(height: 100, color: const Color(0xFFF87E02),),)),
        Column(children: [
          Expanded(child: ListView(
            controller: controller.scrollController,
            children: [

              Container(
                margin: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                padding: EdgeInsets.symmetric(horizontal: 16, vertical: 16),

                child: Column(children: [
                  Row(children: [

                    FadeInImage.memoryNetwork(
                      placeholder: kTransparentImage,
                      image: 'https://xpertgain.io/symbol/${controller.model.value.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
                      width: 40, height: 40,
                      imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
                    ),

                    // loadImage('assets/images/icon/${controller.model.value.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png', Size(40, 40)),
                    SizedBox(width: 12,),
                    Text(title, style: textStyleHeading(color: ColorConstants.white),),
                    Text('/USDT', style: textStyleHeading3(color: ColorConstants.white),),
                  ],),

                  SizedBox(height: 40,),

                  Obx(() => controller.curOneChnangeResponse.value !=null?Table(
                    children: [
                      TableRow(children: [
                        TableCell(child: _labelItem('${controller.curOneChnangeResponse.value.positionamt}',
                            'Position amount(USDT)')),
                        TableCell(child: _labelItem('${controller.curOneChnangeResponse.value.avgprice}',
                            'Avg. price')),
                        TableCell(child: _labelItem('${controller.curOneChnangeResponse.value.margincall}', 'Number of call margin')),
                      ]),
                      _rowSpacer,
                      TableRow(children: [
                        TableCell(child: _labelItem(controller.curOneChnangeResponse.value.positionquantity.toStringAsFixed(3), 'Position quantity ($title)')),
                        TableCell(child: _labelItem('${controller.curOneChnangeResponse.value.currentprice}', 'Current price')),
                        TableCell(child: _labelItem('${controller.curOneChnangeResponse.value.returnrate}%', 'Return rate')),
                      ])
                    ],
                  )
                  :SizedBox()),

                  SizedBox(height: 16,),

                ],),
                decoration: BoxDecoration(
                    color: ColorConstants.whiteRev,
                    borderRadius: BorderRadius.circular(12)
                ),
              ),

              _bodyContainer(child: Column(children: [
                Row(children: [
                  Expanded(child: Obx(() => IconCell(title: controller.tradeStatusResponse.value.cycleShotMode==1?'One-shot':'Cycle',
                    image: 'assets/icons/ic_oneshot-cycle.png', onPressed: (){

                      controller.updateModesConfirm(context, '/account/UpdateCycleShotMode',
                          controller.tradeStatusResponse.value.cycleShotMode==0?1:0, 'One-shot');

                    },)), flex: 35,),
                  Expanded(child: IconCell(title: 'Sell', image: 'assets/icons/ic_sell.png',onPressed: (){

                    // controller.updateModesConfirm(context, '/account/UpdateSellMode',
                    //     controller.tradeStatusResponse.value.sell==0?1:0, 'Selling');

                    showDialog(context: context, builder: (context) =>
                        BuySellDialog(isBuy: false, symbol: controller.model.value.symbol.replaceAll('USDT', ''),), barrierDismissible: false);

                  },), flex: 35,),
                  Expanded(child: IconCell(title: 'Buy'.tr, image: 'assets/icons/ic_buy.png', onPressed: (){

                    // controller.updateModesConfirm(context, '/account/UpdateBuyMode',
                    //     controller.tradeStatusResponse.value.buy==0?1:0, 'Buying');
                    showDialog(context: context, builder: (context) => BuySellDialog(isBuy: true, symbol: controller.model.value.symbol.replaceAll('USDT', ''),), barrierDismissible: false);

                  },), flex: 30,),
                ],),

                SizedBox(height: 16,),

                Row(
                  children: [
                  Expanded(child: Obx(() => Align(
                    alignment: Alignment.centerLeft,
                    child: IconCell(title: controller.
                    tradeStatusResponse.value.marginLimit==1?'Stop margin call':'Start margin call', image: 'assets/icons/ic_stop_margin_call.png', onPressed: (){
                      // https://royalq.bittgc.io/api/account/updateMarginLimit
                      controller.updateModesConfirm(context, '/account/updateMarginLimit',
                          controller.tradeStatusResponse.value.marginLimit==0?1:0, controller.tradeStatusResponse.value.marginLimit==1?'Stop margin call':'Start margin call');

                    },),
                  )), flex: 35,),
                /*  Expanded(child: IconCell(title: 'Get_boost'.tr, image: 'assets/icons/ic_get_boost.png', onPressed: (){
                    Get.toNamed(Routes.GET_BOOSTER, arguments: [{'model': controller.model.value}]);
                    // controller.updateModesConfirm(context, '/account/UpdateStrategyMode',
                    //     controller.tradeStatusResponse.value.strategyMode==0?1:0, 'Strategy_mode'.tr);
                  },), flex: 35,),*/
                  Expanded(child: Container(), flex: 30,),
                ],),
              ],)),
              _bodyContainer(child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Operation_reminder'.tr, style: textStyleHeading2(),),
                  SizedBox(height: 8,),
                  Text('eEzyTradeMessage'.trParams({'name': '$APP_NAME'}), style: textStyleLabel(color: ColorConstants.white54),),
                ],)),
              _bodyContainer(child: GestureDetector(
                child: Row(children: [
                  Expanded(child: Text('First_preset_buy_in_price'.tr, style: textStyleHeading2(),)),
                  Obx(() => Text('${controller.tradeResponse.value.firstBuy}', style: textStyleLabel(),)),
                  Icon(Icons.chevron_right, color: ColorConstants.white,)
                ],),

                  onTap: () => Get.to(TradeSettingsView()),
              )),

              _bodyContainer(child: Table(
                border: TableBorder(verticalInside: BorderSide(width: 1, color: Colors.black12),
                  horizontalInside: BorderSide(width: 1, color: Colors.black12),
                ),

                children: [
                  TableRow(children: [
                    Obx(() => _cellContainer(title: 'First_Buy_in_amount'.tr, value: controller.tradeResponse.value.firstBuy, image: 'assets/icons/ic_quota.png'),),
                    Obx(() => _cellContainer(title: 'Margin_call_limit'.tr,
                        value: controller.tradeResponse.value.maginCallLimit,
                        image: 'assets/icons/ic_order_max.png', isLeft: false)),
                  ]),

                  TableRow(children: [
                    Obx(() => _cellContainer(title: 'Take_profit_ratio'.tr, value: '${controller.tradeResponse.value.wholePRatio}%', image: 'assets/icons/ic_take_profit_rate.png')),
                    Obx(() => _cellContainer(title: 'Earnings_callback'.tr, value: '${controller.tradeResponse.value.wholePCallback}%', image: 'assets/icons/ic_take_profit_back.png', isLeft: false)),
                  ]),

                  TableRow(children: [
                    Obx(() => GestureDetector(
                      onTap: (){
                        print(controller.tradeResponse.value.marginList.first.calls);
                      },
                      child: _cellContainer(title: 'Margin_call_drop'.tr, value:
                      '${controller.tradeResponse.value.marginList.isEmpty?"0.0":
                      controller.tradeResponse.value.marginList.first.calls}%',
                          image: 'assets/icons/ic_cover_rate.png'),
                    )),
                    Obx(() => _cellContainer(title: 'Buy_in_callback'.tr, value:
                    '${controller.tradeResponse.value.buyCallback}%', image: 'assets/icons/ic_cover_back.png', isLeft: false)),
                  ]),
                ],)),

              SizedBox(height: 40,)
            ],
          )),

          Container(
            height: 60,
            // color: ColorConstants.whiteRev,
            child: Row(children: [
              Expanded(child: GestureDetector(
                child: Container(
                  width: double.infinity,
                  height: 60,
                  alignment: Alignment.center,
                  child: Text('Trade_Settings'.tr, style: textStyleHeading2(color: ColorConstants.white),),
                ),
                // onTap: () => Get.toNamed(Routes.TRADE_SETTINGS),
                onTap: () => Get.to(TradeSettingsView()),
              )),
              Obx(() => Expanded(child: controller.tradeStatusResponse.value.trademode==1?GestureDetector(
                child: Container(
                  height: 60,
                  alignment: Alignment.center,
                  color: Colors.red,
                  child: Text('Pause'.tr, style: textStyleHeading2(color: ColorConstants.whiteRev),),),
                onTap: (){
                  controller.exchangeType.value = exchangeValue;
                  controller.startTrading(false);
                },
              ):GestureDetector(
                child: Container(
                  height: 60,
                  alignment: Alignment.center,
                  color: const Color(0xFFF87E02),
                  child: Text('Start'.tr, style: textStyleHeading2(color: ColorConstants.whiteRev),),),
                onTap: (){
                  controller.exchangeType.value = exchangeValue;
                  controller.startTrading(true);
                  // _bindSpotTradeAPI(body);
                },
              ))),
            ],),
          )
        ],)
      ],),
    );
  }

  Widget _cellContainer({required title, required value, required image, isLeft=true}) => Container(
    padding: EdgeInsets.symmetric(vertical: 16),
    child: Row(children: [
      SizedBox(width: isLeft?0:8,),
      Image.asset(image,width: 20,),
      SizedBox(width: 4,),
      Expanded(child: Text(title, style: textStyleLabel(),)),
      Text('$value', style: textStyleLabel(),),
      SizedBox(width: isLeft?8:4,)
    ],),
  );

  Widget _bodyContainer({required child}) => Container(
    child: child,
    margin: EdgeInsets.symmetric(horizontal: 16, vertical: 4),
    padding: EdgeInsets.symmetric(horizontal: 16, vertical: 16),
    decoration: BoxDecoration(
        // color: const Color(0xFF102957),
        color: Colors.white,
        borderRadius: BorderRadius.circular(4)
    ),
  );


  Widget _labelItem(title, label) => Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      Text(title, style: textStyleHeading(color: ColorConstants.white),),
      Text(label, style: textStyleLabel(fontSize: 12, color: ColorConstants.white54),),
    ],);

  final TableRow _rowSpacer=TableRow(children: [
    TableCell(child: SizedBox(height: 16,)),
    TableCell(child: SizedBox(height: 16,)),
    TableCell(child: SizedBox(height: 16,)),
  ]);

}

class IconCell extends StatelessWidget {
  final String title;
  final String image;
  final Function()? onPressed;
  final double? size;
  final bool active;
  final Color? color;
  const IconCell({Key? key, required this.title, required this.image, this.onPressed, this.size, this.active=true, this.color}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Column(children: [
        Image.asset(image,width: size??36, color: active?color:Colors.blueGrey.withOpacity(0.5), ),
        SizedBox(height: 4,),
        Text(title, style: textStyleLabel(),)
      ],),
      onTap: onPressed,
    );
  }
}


class AFIconCell extends StatelessWidget {
  final String title;
  final String image;
  final Function()? onPressed;
  final double? size;
  final bool active;
  final Color? color;
  const AFIconCell({Key? key, required this.title, required this.image, this.onPressed, this.size, this.active=true, this.color}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Column(children: [
        Image.asset(image,width: size??36, color: active?color:Colors.white.withOpacity(0.5), ),
        SizedBox(height: 4,),
        Text(title, style: textStyleLabel(color: Colors.white),)
      ],),
      onTap: onPressed,
    );
  }
}
