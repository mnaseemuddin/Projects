import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:transparent_image/transparent_image.dart';
import 'get_booster_controller.dart';

class GetBoosterView extends  GetView<GetBoosterController>  {

  final List<String> _tabs = ['Get Booster'.tr, 'Running Get Booster'.tr];
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final PageController _pageController = PageController();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        title: Text('Get Booster'),
      ),
      body: SafeArea(child: Column(children: [
        SizedBox(height: 8,),
        SATabview(titles: ['Get Booster'.tr, 'Running Get Booster'.tr,],
          onPageChange: _onPageChange, onSelect: (page){
            _pageController.animateToPage(page, duration: Duration(milliseconds: 200), curve: Curves.ease);
            _onPageChange.add(page);
          }, expand: true,),
        Expanded(child: PageView(
            controller: _pageController,
            onPageChanged: (page){
              _onPageChange.add(page);
            },
            children: [
              Container(padding: EdgeInsets.all(8),
                child: Column(children: [
                  Obx(() => SACellContainer(child: Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      FadeInImage.memoryNetwork(
                        placeholder: kTransparentImage,
                        image: 'https://xpertgain.io/symbol/${controller.boosterCurResponse.value.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
                        width: 40, height: 40,
                        imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
                      ),
                      SizedBox(width: 24,),
                      Expanded(child: Container(child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [

                          SizedBox(height: 8,),
                          Row(children: [
                            Text(controller.boosterCurResponse.value.symbol.replaceAll('USDT', ''), style: textStyleHeading(color: ColorConstants.white),),
                            Text('/USDT', style: textStyleHeading3(color: ColorConstants.white),),
                          ],),
                          Divider(color: ColorConstants.blue),
                          SizedBox(height: 24,),
                          Row(children: [
                            Text('Current Price:', style: textStyleHeading2(),),
                            SizedBox(width: 8,),
                            Expanded(child: Container(
                              padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                              child: Text('${double.parse(controller.boosterCurResponse.value!.price).toStringAsFixed(5)} USDT',
                                style: textStyleHeading(color: ColorConstants.blue),),
                            decoration: BoxDecoration(
                              border: Border.all(color: ColorConstants.blue, width: 1),
                              color: ColorConstants.white12
                            ),),)
                            // Text('USDT', style: textStyleLabel(color: ColorConstants.white70),),
                          ],),
                          SizedBox(height: 40,),
                          Row(
                            mainAxisSize: MainAxisSize.max,
                            // mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              // ElevatedButton.icon(onPressed: (){}, icon: Icon(Icons.not_started), label: Text('Start')),
                              Spacer(),
                              GestureDetector(
                                child: SACellContainer(
                                    margin: EdgeInsets.all(8),
                                    padding: EdgeInsets.symmetric(horizontal: 36, vertical: 16),
                                    color: ColorConstants.blue, child: Obx(() => Text(controller.boosterCurResponse.value.boosterMode==1?'Stop':'Start'))),
                                onTap: (){
                                  // controller.boosterModeSetting(controller.boosterCurResponse.value.symbol, 'UpdatePositionModestatus');
                                  controller.boosterModeSetting(controller.boosterCurResponse.value.symbol, 'BoosterModeSetting',);
                                },
                              ),
                              Obx(() => controller.boosterCurResponse.value.boosterMode==1?GestureDetector(child: SACellContainer(
                                  margin: EdgeInsets.all(8),
                                  color: ColorConstants.white12, child: Text('Clear')),
                                onTap: (){
                                  controller.boosterModeSetting(controller.boosterCurResponse.value.symbol, 'UpdateClearBoosterMode');
                                },):SizedBox()),
                              GestureDetector(
                                child: SACellContainer(color: ColorConstants.white12, child: Text('Cancel')),
                                onTap: () => Get.back(),
                              ),
                            ],)
                        ],),))
                    ],))),
                  Spacer(),
                ],),),
              Container(
                  child: Obx(() => ListView(children: controller.boosterUserListResponse.map((model){
                    String title = model.symbol.replaceAll('USDT', '');
                    return GestureDetector(
                      child: Container(
                        margin: EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                        padding: EdgeInsets.all(16),
                        child: Column(children: [
                          Row(children: [
                            FadeInImage.memoryNetwork(
                              placeholder: kTransparentImage,
                              image: 'https://xpertgain.io/symbol/${model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
                              width: 24, height: 24,
                              imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
                            ),

                            SizedBox(width: 8,),
                            Expanded(child: Text(title, style: textStyleHeading2(),)),

                          ],),
                          Divider(color: ColorConstants.white24,),
                          Row(children: [
                            Expanded(child: Text('Price', style: textStyleLabel()),),
                            Text(model.price.toStringAsFixed(5), style: textStyleLabel(),),
                            Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                          ],),

                          Row(children: [
                            Expanded(child: Text('Quantity', style: textStyleLabel()),),
                            Text(model.quantity.toStringAsFixed(5), style: textStyleLabel()),
                            Text(' $title', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                          ],),

                          Row(children: [
                            Expanded(child: Text('Price change(24h)', style: textStyleLabel()),),
                            Text(model.priceChange.toStringAsFixed(4), style: textStyleLabel()),
                            Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                          ],),

                          Row(children: [
                            Expanded(child: Text('Quantity', style: textStyleLabel()),),
                            Text(model.quantity.toStringAsFixed(5), style: textStyleLabel()),
                            Text(' $title', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                          ],),

                          Row(children: [
                            Expanded(child: Text('Profit', style: textStyleLabel()),),
                            Text(model.profit.toStringAsFixed(5), style: textStyleLabel()),
                            Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                          ],),

                          Row(children: [
                            Expanded(child: Text('Change', style: textStyleLabel()),),
                            Text(model.returnprofit.toStringAsFixed(5), style: textStyleLabel()),
                            Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                          ],),
                        ],),
                        decoration: textBgWhite,
                      ),
                      onTap: (){
                        controller.setBooster(model);
                        _onPageChange.add(0);
                        _pageController.animateToPage(0, duration: Duration(milliseconds: 200), curve: Curves.ease);
                      },
                    );
                  }).toList(),))
              ),
            ]))
      ],)),
    );
  }
}

/**
 *
    children: [
    Container(padding: EdgeInsets.all(8),
    child: Obx(() => Controller.model.value!=null?SACellContainer(child: Row(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
    FadeInImage.memoryNetwork(
    placeholder: kTransparentImage,
    image: 'https://xpertgain.io/symbol/${Controller.model.value!.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
    width: 40, height: 40,
    imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
    ),
    SizedBox(width: 24,),
    Expanded(child: Container(child: Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
    SizedBox(height: 8,),
    Row(children: [
    Text(Controller.model!.value!.symbol.replaceAll('USDT', ''), style: textStyleHeading(color: ColorConstants.white),),
    Text('/USDT', style: textStyleHeading3(color: ColorConstants.white),),
    ],),
    SizedBox(height: 16,),
    Row(children: [
    Text('Current Price:', style: textStyleHeading2(),),
    SizedBox(width: 8,),
    Expanded(child: Text('${Controller.model!.value!.lastPrice} USDT', style: textStyleHeading(color: ColorConstants.blue),),)
    // Text('USDT', style: textStyleLabel(color: ColorConstants.white70),),
    ],),
    SizedBox(height: 40,),
    Row(
    mainAxisSize: MainAxisSize.max,
    // mainAxisAlignment: MainAxisAlignment.spaceBetween,
    children: [
    // ElevatedButton.icon(onPressed: (){}, icon: Icon(Icons.not_started), label: Text('Start')),
    GestureDetector(
    child: SACellContainer(
    margin: EdgeInsets.all(8),
    padding: EdgeInsets.symmetric(horizontal: 36, vertical: 16),
    color: ColorConstants.blue, child: Obx(() => Text(Controller.boosterCurResponse.value.boosterMode==1?'Stop':'Start'))),
    onTap: (){
    Controller.boosterModeSetting(Controller.model.value!.symbol, 'UpdatePositionModestatus');
    },
    ),
    Obx(() => Controller.boosterCurResponse.value.boosterMode==1?GestureDetector(child: SACellContainer(
    margin: EdgeInsets.all(8),
    color: ColorConstants.white12, child: Text('Clear')),
    onTap: (){
    Controller.boosterModeSetting(Controller.model.value!.symbol, 'UpdateClearBoosterMode');
    },):SizedBox()),
    SACellContainer(color: ColorConstants.white12, child: Text('Cancel')),
    ],)
    ],),))
    ],)):SizedBox()),),
    Container(
    child: Obx(() => ListView(children: Controller.boosterUserListResponse.map((model){
    String title = model.symbol.replaceAll('USDT', '');
    return Container(
    margin: EdgeInsets.all(4),
    padding: EdgeInsets.all(16),
    child: Column(children: [
    Row(children: [
    FadeInImage.memoryNetwork(
    placeholder: kTransparentImage,
    image: 'https://xpertgain.io/symbol/${model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
    width: 24, height: 24,
    imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
    ),

    SizedBox(width: 8,),
    Expanded(child: Text(title, style: textStyleHeading2(),)),

    ],),
    Divider(color: ColorConstants.white24,),
    Row(children: [
    Expanded(child: Text('Price', style: textStyleLabel()),),
    Text(model.price.toStringAsFixed(5), style: textStyleLabel(),),
    Text('USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
    ],),

    Row(children: [
    Expanded(child: Text('Quantity', style: textStyleLabel()),),
    Text(model.quantity.toStringAsFixed(5), style: textStyleLabel()),
    Text(title, style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
    ],),

    Row(children: [
    Expanded(child: Text('Price change', style: textStyleLabel()),),
    Text(model.priceChange.toStringAsFixed(4), style: textStyleLabel()),
    Text('USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
    ],),

    Row(children: [
    Expanded(child: Text('Quantity', style: textStyleLabel()),),
    Text(model.quantity.toStringAsFixed(4), style: textStyleLabel()),
    Text(title, style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
    ],),

    Row(children: [
    Expanded(child: Text('Profit', style: textStyleLabel()),),
    Text(model.price.toStringAsFixed(4), style: textStyleLabel()),
    Text('USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
    ],),

    Row(children: [
    Expanded(child: Text('Change', style: textStyleLabel()),),
    Text(model.returnprofit.toStringAsFixed(5), style: textStyleLabel()),
    ],),
    ],),
    decoration: textBgWhite,
    );

    }).toList(),))
    ),
    ]
 *
 */

