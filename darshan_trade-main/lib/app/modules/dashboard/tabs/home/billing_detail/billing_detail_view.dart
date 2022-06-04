import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/response/allfees_response.dart';
import 'package:royal_q/app/models/response/totalbilling_resposne.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/billing_detail/billing_detail_by_coin.dart';
import 'package:royal_q/app/shared/shared.dart';
import '../../../../../models/response/TotalFeesAssetResposne.dart';
import '../../../../../models/response/fee_response.dart';
import '../../../../../routes/app_pages.dart';
import 'billing_detail_controller.dart';


class BillingDetailView extends  GetView<BillingDetailController>  {

  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final PageController _pageController = PageController();
  final totalBillingResponse=RxList<TotalBillingResponse>();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Billing Detail', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        elevation: 0, centerTitle: true,
        backgroundColor: Colors.transparent,
      ), 
      body: Container(
        // padding: EdgeInsets.symmetric(horizontal: 8),
        child: Column(children: [
          _headerView(),
          SizedBox(height: 8,),
          // Row(children: [
          //   SizedBox(width: 8,),
          //   Expanded(child: Text('Billing details', style: textStyleHeading2(color: ColorConstants.white),)),
          //   Text('Total profit', style: textStyleHeading2(color: ColorConstants.white),),
          //   SizedBox(width: 8,),
          // ],),

          SizedBox(height: 8,),

          // Container(
          //   height: 40,
          //   alignment: Alignment.centerLeft,
          //   child: SATabview(titles: ["Total Billing",'Billing details',
          //     'Total profit',"Total Loss"],
          //     onPageChange: _onPageChange, onSelect: (page){
          //       _pageController.animateToPage(page, duration: Duration(milliseconds: 200), curve: Curves.ease);
          //       _onPageChange.add(page);
          //     }, expand: true,isScroll: true,),
          // ),

          Container(height: 40,
            alignment: Alignment.centerLeft,
            child: SATabview(titles: ['All Billing'.tr, 'Billing details'.tr, 'All Profit'.tr,
              'All Loss'.tr ,'Total Loss'.tr,'Fees'.tr,'All Fees'.tr,'Total Fees'.tr],
              selectedColor: Colors.orange, textColor: Colors.black, expand: true,
              onPageChange: _onPageChange, onSelect: (page){
                _pageController.animateToPage(page, duration: Duration(milliseconds: 200), curve: Curves.ease);
              }, isScroll: true,),),

          SizedBox(height: 8,),

          Expanded(child: PageView(
            controller: _pageController,
            onPageChanged: (page){
              _onPageChange.add(page);
            },
            children: [
              Obx(() => controller.totalBillingResponse.isNotEmpty? _pageTotalBilling():SizedBox()),
              Obx(() => controller.billingResponse.isNotEmpty?_pageBillingDetails():SizedBox()),
              Obx(() => controller.totalprofitResponse.isNotEmpty?_pageTotalProfits():SizedBox(),),
              Obx(() => controller.allLossResponse.isNotEmpty?_pageAllLoss():SizedBox()),
              Obx(() => controller.totalLossResponse.isNotEmpty?_pageTotalLOss():SizedBox()),
               Obx(() => controller.feesResposne.isNotEmpty?_pageFees():SizedBox()),
              Obx(() => controller.allFeesResponse.isNotEmpty?_pageAllFess():SizedBox()),
              Obx(() => controller.allTotalFeesAsset.isNotEmpty?_pageTotalAssetFess():SizedBox()),
            ],
          ))

          // ListView(children: [
          // ],)
        ],),
      ),
    );
  }

  Widget _headerView() => Container(
    color: ColorConstants.orange,
    padding: EdgeInsets.all(16),
    child: Column(children: [
      Row(children: [
        Expanded(child: Text('Today\'s Profit', style: textStyleHeading2(color: ColorConstants.whiteRev),)),
        Obx(() => Text(controller.todayProfit.value.toStringAsFixed(5), style: textStyleHeading2(color: ColorConstants.whiteRev),)),
        SizedBox(width: 4,),
        Text(' USDT', style: textStyleLabel(color: ColorConstants.whiteRev),)
      ],),
      SizedBox(height: 8,),
      Row(children: [
        Expanded(child: Text('Today\'s Loss', style: textStyleHeading2(color: ColorConstants.whiteRev),)),
        Obx(() => Text(controller.todayLoss.value.toStringAsFixed(5), style: textStyleHeading2(color: ColorConstants.whiteRev),)),
        SizedBox(width: 4,),
        Text(' USDT', style: textStyleLabel(color: ColorConstants.whiteRev),)
      ],),
      SizedBox(height: 8,),
      Row(children: [
        Expanded(child: Text('Total Profit', style: textStyleHeading2(color: ColorConstants.whiteRev),)),
        Obx(() => Text(controller.totalProfit.value.toStringAsFixed(5), style: textStyleHeading2(color: ColorConstants.whiteRev),),),
        SizedBox(width: 4,),
        Text(' USDT', style: textStyleLabel(color: ColorConstants.whiteRev),)
      ],),
      SizedBox(height: 8,),
      Row(children: [
        Expanded(child: Text('Total Loss', style: textStyleHeading2(color: ColorConstants.whiteRev),)),
        Obx(() => Text(controller.totalLoss2.value.toStringAsFixed(5), style: textStyleHeading2(color: ColorConstants.whiteRev),),),
        SizedBox(width: 4,),
        Text(' USDT', style: textStyleLabel(color: ColorConstants.whiteRev),)
      ],),

      SizedBox(height: 8,),
      Row(children: [
        Expanded(child: Text('Total Fees', style: textStyleHeading2(color: ColorConstants.whiteRev),)),
        Obx(() => Text(controller.totalFess.value.toStringAsFixed(5), style: textStyleHeading2(color: ColorConstants.whiteRev),),),
        SizedBox(width: 4,),
        Text(' USDT', style: textStyleLabel(color: ColorConstants.whiteRev),)
      ],),
    ],),
  );

  Widget _pageBillingDetails() => ListView.builder(itemBuilder: (context, index){
    print(controller.billingResponse.length);
    BillingResponse model = controller.billingResponse.elementAt(index);
    String title = model.symbol.replaceAll('USDT', '');
    return GestureDetector(
      onTap: (){
        Get.to(BillingDetailByViewScreen(symbol: title));
      },
      child: SACellContainer(
          margin: EdgeInsets.symmetric(vertical: 2, horizontal: 4),
          child: Row(children: [
            Expanded(child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
              Text('$title/USDT', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
              Text('Platform: ${model.platform}')
          ],)), Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(children: [
              Text('Profit: ${model.profit.toStringAsFixed(5)}', style: textStyleLabel(),),
              Text(' USDT', style: textStyleLabel(),),
            ],),
            SizedBox(height: 4,),
           // Text(model.transdate, style: textStyleFootNote(),)
          ],)
      ],)),
    );
  }, itemCount: controller.billingResponse.length,);

  Widget _pageTotalBilling() => ListView.builder(itemBuilder: (context, index){
    print(controller.billingResponse.length);
    TotalBillingResponse model = controller.totalBillingResponse.elementAt(index);
    String title = model.symbol.replaceAll('USDT', '');
    return SACellContainer(
        margin: EdgeInsets.symmetric(vertical: 2, horizontal: 4),
        child: Row(children: [
          Expanded(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('$title/USDT', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
              Text('Platform: ${model.platform}')
            ],)), Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(children: [
                Text('Profit: ${model.profit}', style: textStyleLabel(),),
                Text(' USDT', style: textStyleLabel(),),
              ],),
              SizedBox(height: 4,),
              // Text(model.transdate, style: textStyleFootNote(),)
            ],)
        ],));
  }, itemCount: controller.totalBillingResponse.length,);


  Widget _pageAllFess() => ListView.builder(itemBuilder: (context, index){
    print(controller.billingResponse.length);
    AllFeesResponse model = controller.allFeesResponse.elementAt(index);
    String title = model.symbol.replaceAll('USDT', '');
    return SACellContainer(
        margin: EdgeInsets.symmetric(vertical: 2, horizontal: 4),
        child: Row(children: [
          Expanded(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('$title/USDT', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
              Text('Platform: ${model.platform}')
            ],)), Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(children: [
                Text('${model.commission}', style: textStyleLabel(),),
                Text(' ${model.commissionAsset}', style: textStyleLabel(),),
              ],),
              SizedBox(height: 4,),
              // Text(model.transdate, style: textStyleFootNote(),)
            ],)
        ],));
  }, itemCount: controller.allFeesResponse.length,);

  Widget _pageTotalProfits() => ListView.builder(itemBuilder: (context, index){
    TotalprofitResponse model = controller.totalprofitResponse.elementAt(index);
    return ListTile(title: Text(model.transdate), trailing: Text('${model.profit} USDT'),
      tileColor: index%2==0?ColorConstants.white10.withOpacity(0.05):null,);
  }, itemCount: controller.totalprofitResponse.length,);


 Widget _pageTotalLOss() => ListView.builder(itemBuilder: (context, index){
    TotalprofitResponse model = controller.totalLossResponse.elementAt(index);
    return ListTile(title: Text(model.transdate), trailing: Text('${model.profit} USDT'),
      tileColor: index%2==0?ColorConstants.white10.withOpacity(0.05):null,);
  }, itemCount: controller.totalLossResponse.length,);



 Widget _pageFees() => ListView.builder(itemBuilder: (context, index){
    FeeResponse model = controller.feesResposne.elementAt(index);
    return ListTile(title: Text(model.transdate), trailing: Text('${model.fee} ${model.commissionAsset}'),
      tileColor: index%2==0?ColorConstants.white10.withOpacity(0.05):null,);
  }, itemCount: controller.feesResposne.length,);


  Widget _pageAllLoss() =>ListView.builder(itemBuilder: (context, index){
    print("Total Loss : "+controller.allLossResponse.length.toString());
    TotalBillingResponse model = controller.allLossResponse.elementAt(index);
    String title = model.symbol.replaceAll('USDT', '');
    return SACellContainer(
        margin: EdgeInsets.symmetric(vertical: 2, horizontal: 4),
        child: Row(children: [
          Expanded(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('$title/USDT', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
              Text('Platform: ${model.platform}')
            ],)), Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(children: [
                Text('${model.profit}', style: textStyleLabel(color: Colors.red),),
                Text(' USDT', style: textStyleLabel(),),
              ],),
              SizedBox(height: 4,),
              // Text(model.transdate, style: textStyleFootNote(),)
            ],)
        ],));
  }, itemCount: controller.allLossResponse.length,);

  _pageTotalAssetFess()  =>ListView.builder(itemBuilder: (context, index){
    TotoalFeesResponse model = controller.allTotalFeesAsset.elementAt(index);
    return SACellContainer(
        margin: EdgeInsets.symmetric(vertical: 2, horizontal: 4),
        child: Row(children: [
          Expanded(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(model.commissionAsset, style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
              Text('Platform: ${model.platform}')
            ],)), Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(children: [
                Text('${model.commission}', style: textStyleLabel(),),
              ],),
              SizedBox(height: 4,),
              // Text(model.transdate, style: textStyleFootNote(),)
            ],)
        ],));
  }, itemCount: controller.allTotalFeesAsset.length,);

}
  