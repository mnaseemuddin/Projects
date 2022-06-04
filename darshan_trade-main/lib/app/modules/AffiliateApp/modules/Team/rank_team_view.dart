import 'dart:math';

import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/AffiliateApp/Controller/AffiliateTeamController.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/Team/star_details.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/star_details.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/main.dart';

import '../../../../shared/constants/colors.dart';

class AffRankTeamView extends StatefulWidget {
  const AffRankTeamView({Key? key}) : super(key: key);

  @override
  State<AffRankTeamView> createState() => _AffRankTeamViewState();
}

class _AffRankTeamViewState extends State<AffRankTeamView> {
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
      body: Container(child:
      Column(
        children: [
          SACellContainer(
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
                        Text('Level'.trParams({'level': '3'}), style: textStyleHeading2(),),
                        SizedBox(height: 8,),
                        // Text('Total: ${response.total}', style: textStyleLabel()),
                        Text('Total'.trParams({'total': '7'}), style: textStyleLabel()),
                      ],)),
                    SizedBox(width: 8,),
                    TextButton(onPressed: (){
                      // controller.getLevelWise(response.level);
                      Get.to(AffStarDetail());
                    }, child: Text('View'.tr, style: textStyleHeading2(),)),
                    Icon(Icons.chevron_right, color: ColorConstants.white54,)
                    // IconCell(title: status?'Active':'In-Active', image: 'assets/icons/ic_checked.png', active: status, size: 24,)
                  ],),),
                onTap: (){
               Get.to(AffStarDetail());
                },
              )),
          Container(
              decoration: textBgGradient,
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
                        Text('Level'.trParams({'level': '3'}), style: textStyleHeading2(color: Colors.white),),
                        SizedBox(height: 8,),
                        // Text('Total: ${response.total}', style: textStyleLabel()),
                        Text('Total'.trParams({'total': '7'}), style: textStyleLabel(color: Colors.white)),
                      ],)),
                    SizedBox(width: 8,),
                    TextButton(onPressed: (){
                      // controller.getLevelWise(response.level);
                      // Get.to(LevelWiseView(levelTeamResponse: response));
                    }, child: Text('View'.tr, style: textStyleHeading2(color: Colors.white),)),
                    Icon(Icons.chevron_right, color: Colors.white54,)
                    // IconCell(title: status?'Active':'In-Active', image: 'assets/icons/ic_checked.png', active: status, size: 24,)
                  ],),),
                onTap: (){
                  // controller.getLevelWise(response.level);
                  // Get.to(LevelWiseView(levelTeamResponse: response));
                },
              )),
        ],
      ),),
    );
  }
}
