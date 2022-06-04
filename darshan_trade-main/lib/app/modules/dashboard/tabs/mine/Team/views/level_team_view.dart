import 'dart:math';

import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/levelwise_view.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/main.dart';

import '../controllers/team_controller.dart';

class LevelTeamView extends GetView<TeamController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // appBar: AppBar(
      //   title: Text('Direct Team'),
      //   centerTitle: true,
      //   elevation: 0,
      //   backgroundColor: Colors.transparent,
      //   brightness: Brightness.dark,
      // ),
      body: Container(
        margin: EdgeInsets.symmetric(horizontal: 8),
        child: Obx(() => controller.levelTeam.value.isNotEmpty?ListView.builder(itemBuilder: (context, index){
          LevelTeamResponse response = controller.levelTeam.value.elementAt(index);
          // bool status = response.status=='In-Active'?false:true;
          return SACellContainer(
              padding: EdgeInsets.all(8),
              child: GestureDetector(
                child: Container(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                  child: Row(children: [
                    ClipRRect(
                      borderRadius: BorderRadius.circular(36.0),
                      child: Image.asset('assets/expgain/icon_splash.png', width: 40, height: 40,),
                    ),
                    SizedBox(width: 8,),
                    Expanded(child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // Text('Level: ${response.level}', style: textStyleHeading2(),),
                        Text('Level'.trParams({'level': '${response.level}'}), style: textStyleHeading2(),),
                        SizedBox(height: 8,),
                        // Text('Total: ${response.total}', style: textStyleLabel()),
                        Text('Total'.trParams({'total': '${response.total}'}), style: textStyleLabel()),
                      ],)),
                    SizedBox(width: 8,),
                    TextButton(onPressed: (){
                      controller.getLevelWise(response.level);
                      Get.to(LevelWiseView(levelTeamResponse: response));
                    }, child: Text('View'.tr, style: textStyleHeading2(),)),
                    Icon(Icons.chevron_right, color: ColorConstants.white54,)
                    // IconCell(title: status?'Active':'In-Active', image: 'assets/icons/ic_checked.png', active: status, size: 24,)
                  ],),),
                onTap: (){
                  controller.getLevelWise(response.level);
                  Get.to(LevelWiseView(levelTeamResponse: response));
                },
              ));
        }, itemCount: controller.levelTeam.value.length,)
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
