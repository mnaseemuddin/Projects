import 'dart:math';

import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/star_details.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/main.dart';

import '../controllers/team_controller.dart';

class RankTeamView extends GetView<TeamController> {
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
        child: Obx(() => controller.rankTeam.value.isNotEmpty?ListView.builder(itemBuilder: (context, index){
          RankTeamResponse response = controller.rankTeam.value.elementAt(index);
          // bool status = response.status=='In-Active'?false:true;
          return GestureDetector(
            child: SACellContainer(
    padding: EdgeInsets.all(8),
    child: Container(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      child: Row(children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(36.0),
          child: Image.asset('assets/expgain/icon_splash.png', width: 40, height: 40,),
          // child: Image.network(
          //   'https://static.vecteezy.com/system/resources/previews/002/318/271/non_2x/user-profile-icon-free-vector.jpg',
          //   height: 64.0,
          //   width: 64.0,
          // ),
        ),
        SizedBox(width: 8,),
        Expanded(child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Text('Rank: ${response.ranks}', style: textStyleHeading2(),),
            Text('Star'.trParams({'star': response.ranks}), style: textStyleHeading2(),),
            SizedBox(height: 8,),
            // Text('Total Team: ${response.totalteam}', style: textStyleLabel()),
            Text('Total_Team'.trParams({'team': '${response.totalteam}'}), style: textStyleLabel()),
          ],)),
        SizedBox(width: 8,),
        Icon(Icons.chevron_right)
        // IconCell(title: status?'Active':'In-Active', image: 'assets/icons/ic_checked.png', active: status, size: 24,)
      ],),)),
            onTap: (){
              controller.rankwiselist(response.ranks);
              Get.to(() => StarDetails('Star'.trParams({'star': response.ranks})));
            },
          );
        }, itemCount: controller.rankTeam.value.length,)
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
