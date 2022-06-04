import 'dart:math';

import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/response/levelwise_response.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

import '../controllers/team_controller.dart';

class LevelWiseView extends GetView<TeamController> {
  final LevelTeamResponse levelTeamResponse;

  LevelWiseView({required this.levelTeamResponse});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        // title: Text('Level: ${levelTeamResponse.level}'),
        title: Text('Level'.trParams({'level': '${levelTeamResponse.level}'}), style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.transparent,
        brightness: Brightness.dark,
      ),
      body: Container(
        margin: EdgeInsets.symmetric(horizontal: 8),
        child: Obx(() => controller.levelWiseTeam.value.isNotEmpty?ListView.builder(itemBuilder: (context, index){
          LevelWiseResponse response = controller.levelWiseTeam.value.elementAt(index);
          bool status = response.status=='In-Active'?false:true;
          return SACellContainer(
              padding: EdgeInsets.all(8),
              child: Container(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                child: Row(children: [
                  ClipRRect(
                    borderRadius: BorderRadius.circular(36.0),
                    child: Image.asset(
                      'assets/expgain/icon_splash.png',
                      height: 64.0,
                      width: 64.0,
                    ),
                  ),
                  SizedBox(width: 8,),
                  Expanded(child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Text('Name'.trParams({'name': response.name}), style: textStyleHeading2(),),
                      // Text(response.name, style: textStyleHeading2(),),
                       Text('UUID- ${response.uuid}', style: textStyleHeading2(),),
                      Text('Contact- ${response.name}', style: textStyleLabel(),),

                      SizedBox(height: 8,),
                      // Text('Username'.trParams({'name': response.username}), style: textStyleLabel()),
                    ],)),
                  SizedBox(width: 8,),
                  IconCell(title: status?'Active':'In-Active', image: 'assets/icons/ic_checked.png', active: status, size: 24,)
                ],),));
        }, itemCount: controller.levelWiseTeam.value.length,)
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
