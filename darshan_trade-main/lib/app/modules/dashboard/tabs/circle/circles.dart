import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/tabs/circle/robot_deposite.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/circle_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/trading_cell.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

class Circles extends GetView<CircleController> {

  @override
  Widget build(BuildContext context) {
    var mcontroller = Get.isRegistered<CircleController>()
        ? Get.find<CircleController>()
        : Get.put(CircleController());
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 0),
      child: Column(children: [
        SizedBox(height: 8,),
        SASearch(hintText: 'Search circle', onChange: (string) {},),
        Expanded(child: Obx(() => ListView.builder(itemBuilder: (context, index){
          UserCurrencyResponse model = controller.listRunningRobot.elementAt(index);
          return TradingCell(
            padding: 0,
            model: model, onPressed: (){
            CurrencyModel currencyModel = CurrencyModel(symbol: model.symbol, priceChangePercent: '${model.priceChange}', lastPrice: '${model.price}', lastQty: '${model.quantity}', strategyMode: model.strategyMode);
            Get.to(RobotDeposit(model: currencyModel,));
          },);
        }, itemCount: controller.listRunningRobot.length,)))
      ],),
      // child: ListView(children: [
      //   Column(children: [
      //     SASearch(hintText: 'Search circle', onChange: (string) {},),
      //     SizedBox(height: 8,),
      //     SACarousel(width: double.infinity, height: 250,
      //         duration: 5,
      //         list: isExpertGain ? [
      //           Image.asset(
      //               'assets/images/slide03.jpg', fit: BoxFit.fitWidth),
      //           Image.asset(
      //               'assets/images/slide04.jpg', fit: BoxFit.fitWidth),
      //           Image.asset(
      //               'assets/images/slide05.jpg', fit: BoxFit.fitWidth),
      //         ] : [
      //           Image.asset(
      //               'assets/images/Circle_1.png', fit: BoxFit.fitWidth),
      //           Image.asset(
      //               'assets/images/Circle_2.png', fit: BoxFit.fitWidth),
      //           Image.asset(
      //               'assets/images/Circle_3.png', fit: BoxFit.fitWidth),
      //         ]),
      //     SizedBox(height: 8,),
      //     Container(
      //       height: 40,
      //       child: Row(
      //         crossAxisAlignment: CrossAxisAlignment.center,
      //         children: [
      //           Icon(Ionicons.people_circle_outline, color: Colors.blue[700],),
      //           SizedBox(width: 8,),
      //           Text('Discover_circle'.tr, style: textStyleHeading3(),),
      //           Expanded(child: Container()),
      //           IconButton(onPressed: () {}, icon: Icon(
      //             MaterialCommunityIcons.sort_descending,
      //             color: ColorConstants.white,)),
      //         ],
      //       ),
      //     ),
      //   ],),
      //   Controller.tradingProfitResponse.value!=null?
      //       Obx(() => Column(children: Controller.tradingProfitResponse.value!.map((tradingProfitResponse){
      //         return SACellContainer(child: Column(
      //           crossAxisAlignment: CrossAxisAlignment.start,
      //           children: [
      //             _createRowChild('Name_:'.tr, Text(tradingProfitResponse.name, style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),),
      //             _createRowChild('Username_:'.tr, tradingProfitResponse.username),
      //             _createRowChild('Country'.tr, tradingProfitResponse.countryname),
      //             _createRowChild('Invitation Code:', GestureDetector(
      //               child: Row(children: [
      //                 Expanded(child: Text(tradingProfitResponse.invitaionCode, style: textStyleLabel(),),),
      //                 Icon(Icons.copy, color: ColorConstants.white,)
      //               ],),
      //               onTap: (){
      //                 Clipboard.setData(ClipboardData(text: tradingProfitResponse.invitaionCode));
      //                 EasyLoading.showToast('Copied'.tr);
      //               },
      //             )),
      //           ],));
      //       }).toList(),))
      //       :NoRecord()
      // ],)

    //   child: Obx(() => Controller.tradingProfitResponse.value!.isEmpty?NoRecord()
    //     :ListView.builder(itemBuilder: (context, index){
    //   TradingProfitResponse tradingProfitResponse = Controller.tradingProfitResponse.value!.elementAt(index);
    //
    //       return SACellContainer(child: Column(
    //       crossAxisAlignment: CrossAxisAlignment.start,
    //       children: [
    //         _createRowChild('Name:', Text(tradingProfitResponse.name, style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),),
    //         _createRowChild('Username:', tradingProfitResponse.username),
    //         _createRowChild('Country:', tradingProfitResponse.countryname),
    //         _createRowChild('Invitation Code:', GestureDetector(
    //           child: Row(children: [
    //             Expanded(child: Text(tradingProfitResponse.invitaionCode),),
    //             Icon(Icons.copy, color: Colors.white,)
    //           ],),
    //           onTap: (){
    //             Clipboard.setData(ClipboardData(text: tradingProfitResponse.invitaionCode));
    //             EasyLoading.showToast('Copied');
    //           },
    //         )),
    //   ],));
    // }, itemCount: Controller.tradingProfitResponse.value!.length,)),
    );
    // return Container(
    //   padding: EdgeInsets.symmetric(horizontal: 16),
    //   child: ListView.builder(itemBuilder: (context, index){
    //     if(index==0){
    //       return Column(children: [
    //         SASearch(hintText: 'Search circle',),
    //         SizedBox(height: 8,),
    //         // SACarousel(width: double.infinity, height: 200, list: [
    //         //   _carouselItem('bg_circle_banner_en'),
    //         //   _carouselItem('bg_banner1_en'),
    //         //   _carouselItem('bg_circle_banner_en'),
    //         //   _carouselItem('bg_banner1_en'),
    //         // ]),
    //         SACarousel(width: double.infinity, height: 200,
    //             duration: 5,
    //             list: isExpertGain?[
    //               Image.asset('assets/images/slide03.jpg', fit: BoxFit.fitWidth),
    //               Image.asset('assets/images/slide04.jpg', fit: BoxFit.fitWidth),
    //               Image.asset('assets/images/slide05.jpg', fit: BoxFit.fitWidth),
    //             ]:[
    //               Image.asset('assets/images/slide1.jpg',fit: BoxFit.fitWidth),
    //               Image.asset('assets/images/slide2.jpg',fit: BoxFit.fitWidth),
    //               Image.asset('assets/images/slide03.jpg', fit: BoxFit.fitWidth),
    //               Image.asset('assets/images/slide04.jpg', fit: BoxFit.fitWidth),
    //               Image.asset('assets/images/slide05.jpg', fit: BoxFit.fitWidth),
    //             ]),
    //         SizedBox(height: 8,),
    //         Container(
    //           height: 40,
    //           child: Row(
    //             crossAxisAlignment: CrossAxisAlignment.center,
    //             children: [
    //               Icon(Ionicons.people_circle_outline, color: Colors.blue[700],),
    //               SizedBox(width: 8,),
    //               Text('Discover circle', style: textStyleHeading3(),),
    //               Expanded(child: Container()),
    //               IconButton(onPressed: (){}, icon: Icon(MaterialCommunityIcons.sort_descending, color: Colors.white,)),
    //             ],
    //           ),
    //         ),
    //       ],);
    //     }
    //
    //     index -= 1;
    //
    //     return CircleCell();
    //
    //   }),
    // );
  }

  Widget _createRowChild(child1, child2) => Row(children: [
    Expanded(child: Text(child1, style: textStyleLabel(),), flex: 35,),
    Expanded(child: child2 is String?Text(child2, style: textStyleLabel()):child2, flex: 65,),
  ],);

  Container _carouselItem(String image) => Container(
    margin: EdgeInsets.only(left: 4, right: 4),
    decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(8),
        image: DecorationImage(image: AssetImage('assets/images/$image.png'), fit: BoxFit.fitWidth)
    ),
  );
}
