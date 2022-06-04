import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/shared.dart';

import '../controllers/reward_details_controller.dart';

class RewardDetailsView extends GetView<RewardDetailsController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Reward_List'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        backgroundColor: Colors.transparent,
        elevation: 0,
        brightness: Brightness.dark,

        actions: [Obx(() => Row(children: [
          Text('${controller.totalBalance}', style: textStyleHeading(color: ColorConstants.APP_SECONDARY_COLOR)),
          Text(' USDT', style: textStyleCaption(color: ColorConstants.APP_SECONDARY_COLOR)),
        ],))],
      ),
      body: Container(
          margin: EdgeInsets.symmetric(horizontal: 8),
          child: Obx(() => controller.rewardResponse.isNotEmpty?ListView.builder(itemBuilder: (context, index){
            RewardResponse response = controller.rewardResponse.elementAt(index);
            return SACellContainer(
                padding: EdgeInsets.all(8),
                child: Container(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                  child: Row(children: [
                    ClipRRect(
                      borderRadius: BorderRadius.circular(36.0),
                      child: Image.network(
                        'https://static.vecteezy.com/system/resources/previews/002/318/271/non_2x/user-profile-icon-free-vector.jpg',
                        height: 64.0,
                        width: 64.0,
                      ),
                    ),
                    SizedBox(width: 8,),
                    Expanded(child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // Text('From: ${response.fromUser}', style: textStyleHeading2(),),
                        Text('From'.trParams({'user': response.fromUser}), style: textStyleHeading2(),),
                        SizedBox(height: 8,),
                        // Text('Remark: ${response.remark}', style: textStyleLabel()),
                        Text('Remark'.trParams({'remark': response.remark}), style: textStyleLabel()),
                        SizedBox(height: 8,),
                        // Text('Date: ${response.transdate}', style: textStyleCaption()),
                        Text('Date'.trParams({'date': response.transdate}), style: textStyleCaption()),
                      ],)),
                    SizedBox(width: 8,),
                    Text('${response.credit}', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
                    Text(' USDT', style: textStyleCaption(color: ColorConstants.APP_SECONDARY_COLOR),)
                    // IconCell(title: status?'Active':'In-Active', image: 'assets/icons/ic_checked.png', active: status, size: 24,)
                  ],),));
          }, itemCount: controller.rewardResponse.length,)
              :Center(child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Image.asset('assets/icons/ic_default_head.png', width: 100,),
              SizedBox(height: 32,),
              Text('no_member_in_team'.tr, style: textStyleHeading2(),),
              SizedBox(height: 100,)
            ],),),)),
    );
  }
}
