import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/futureTradeView.dart';
import 'package:royal_q/app/modules/dashboard/tabs/tabs.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/main.dart';

import '../controllers/dashboard_controller.dart';

class DashboardView extends GetView<DashboardController> {
  final PageController _pageController = PageController();
  @override
  Widget build(BuildContext context) {
    return WillPopScope(child: Scaffold(

      backgroundColor: Colors.transparent,
      body: Column(children: [
        Expanded(child: PageView(
          controller: _pageController,
          physics: NeverScrollableScrollPhysics(),
          children: [
            Home(),
            Quantitative(),
            FutureTradeView(),
            CircleDashboard(),
            MineDashboard()
          ],),),

        Obx(() => Container(
          padding: EdgeInsets.all(8),
          alignment: Alignment.bottomLeft,
          color: ColorConstants.APP_PRIMARY_COLOR,
          child: controller.bottomIndex==2?binanceUSDTMbalance():controller.balance.value!=0 ||
              controller.isBinanceReceived.value? Row(children: [
            // Image.asset('assets/images/icon/${controller.exchnagetype.value==1?'bnb':'coinxplus'}@2x.png', width: 24),
            // Text('${controller.exchnagetype.value==1?' Binance':'Huobi'} balance: ',
            Text('${exchangeValue==1?' Binance':'Huobi'} balance: ',
              style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),),
            // ignore: unrelated_type_equality_checks
            Text(controller.encryptionBalance==true?controller.balance.value.toStringAsFixed(3):
            controller.balance.value.toStringAsFixed(3).replaceAll(RegExp(r"."), "*"),
              style: textStyleHeading(color: ColorConstants.APP_SECONDARY_COLOR),),
            // ignore: unrelated_type_equality_checks
            Text(controller.encryptionBalance==true?' USDT':' USDT'.replaceAll(RegExp(r"."), "*"),
              style: textStyleCaption(color: ColorConstants.APP_SECONDARY_COLOR),),

            IconButton(onPressed: (){
              controller.encryptionWalletBalance();
              print(controller.encryptionBalance);
              // ignore: unrelated_type_equality_checks
            }, icon: Icon(controller.encryptionBalance==true?Ionicons.eye:Ionicons.eye_off,
              size: 25,)),
            IconButton(onPressed: (){
              controller.getXpertgainBalance();
            }, icon: Icon(Icons.refresh))

          ],)
              :Container(width: 50, height: 1, child: LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,),),
        )),
      ],),

      bottomNavigationBar: Obx(() => _createBottomNavigationBar()),
    ), onWillPop:  _backPressed,);
  }
  int counter = 0;
  Future<bool> _backPressed() async{
    counter +=1;
    Future.delayed(Duration(seconds: 1), (){
      counter = 0;
    });
    if(counter==1){
      EasyLoading.showToast('Press two times to Exit', toastPosition: EasyLoadingToastPosition.bottom);
    }
    return counter >=2;
  }

  _createBottomNavigationBar() {
    return Container(
      decoration: BoxDecoration(
          gradient: LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [
                Color(0xFFFF5400),
                Color(0xFF8b009e),Color(0xff1f0fa1)
              ])),
      child: BottomNavigationBar(
          showUnselectedLabels: false,
          backgroundColor: Colors.transparent,
          fixedColor: Colors.white,
          // fixedColor: ColorConstants.iconTheme,
          // fixedColor: isPlatformIOS?Colors.blue:Colors.white,
          showSelectedLabels: true,
          onTap: (index){
            print('Index => $index');
            controller.exchnagetype.value = 1;
            controller.getXpertgainBalance();
            controller.updateBottomIndex(index);
            _pageController.animateToPage(index, duration: Duration(milliseconds: 500,),
                curve: Curves.ease);
          },
          currentIndex: controller.bottomIndex.value!,
          type: BottomNavigationBarType.fixed,
          unselectedItemColor:Colors.white60,
          items: [
            BottomNavigationBarItem(icon: Icon(Ionicons.home_outline), label: 'Home'.tr),
            BottomNavigationBarItem(icon: Icon(Ionicons.repeat_outline), label: 'AIStrategy'.tr),
            BottomNavigationBarItem(icon: Icon(Icons.rocket_launch), label: 'Future'.tr),
            BottomNavigationBarItem(icon: Icon(Ionicons.people_circle_outline), label: 'ATM_Mode'.tr),
            // BottomNavigationBarItem(icon: Icon(AntDesign.bells), label: 'News'.tr),
            BottomNavigationBarItem(icon: Icon(Feather.user), label: 'User'.tr),
          ]),);
  }

  binanceUSDTMbalance() =>Obx(() => Row(children: [
    // Image.asset('assets/images/icon/${controller.exchnagetype.value==1?'bnb':'coinxplus'}@2x.png', width: 24),
    // Text('${controller.exchnagetype.value==1?' Binance':'Huobi'} balance: ',
    Expanded(
      flex: 60,
      child: Text('Binance ${controller.futureTabSelected.value==0||controller.futureTabSelected.value==2
          ?"USDT":"BUSD"}-M balance: ',overflow: TextOverflow.ellipsis,
        style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),),
    ),
    // ignore: unrelated_type_equality_checks
    Expanded(
      flex: 20,
      child: Text(controller.encryptionFUSDTBalance==true?controller.futureTabSelected.value==0||
          controller.futureTabSelected.value==2?controller.USDTBalance.value.toStringAsFixed(3):"0.00":
      controller.USDTBalance.value.toStringAsFixed(3).replaceAll(RegExp(r"."), "*"),maxLines: 1,
        style: textStyleHeading(color: ColorConstants.APP_SECONDARY_COLOR),),
    ),
    // ignore: unrelated_type_equality_checks
    Text(controller.encryptionFUSDTBalance==true?' ${controller.futureTabSelected.value==0||
        controller.futureTabSelected.value==2?"USDT":"BUSD"}':' ${controller.futureTabSelected.value==0||
        controller.futureTabSelected.value==2?"USDT":"BUSD"}'.replaceAll(RegExp(r"."), "*"),
      style: textStyleCaption(color: ColorConstants.APP_SECONDARY_COLOR),),

    Expanded(
      flex: 15,
      child: IconButton(onPressed: (){
        controller.FUSDTWalletBalance();
        // ignore: unrelated_type_equality_checks
      }, icon: Icon(controller.encryptionFUSDTBalance==true?Ionicons.eye:Ionicons.eye_off,
        size: 25,)),
    ),
    IconButton(onPressed: (){
      controller.getUSDTBalance();
    }, icon: Icon(Icons.refresh))

  ],));
}
