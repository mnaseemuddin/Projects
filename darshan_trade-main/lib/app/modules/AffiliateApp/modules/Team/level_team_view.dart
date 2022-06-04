import 'dart:math';

import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/api/apis.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/AffiliateApp/Controller/AffiliateTeamController.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/Team/levelwise_view.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/levelwise_view.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/utils/NoData.dart';
import 'package:royal_q/main.dart';

import '../../model/AffLevelTeamResponse.dart';



class AffLevelTeamView extends StatefulWidget {
  const AffLevelTeamView({Key? key}) : super(key: key);

  @override
  State<AffLevelTeamView> createState() => _AffLevelTeamViewState();
}

class _AffLevelTeamViewState extends State<AffLevelTeamView> {


  AffLevelTeamResponse? levelTeamModel;

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
      backgroundColor: Colors.transparent,
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 4),
        child:
      levelTeamModel==null?AFNoRecord():ListView(
          children:levelTeamModel!.data.map((e) => Container(
          decoration: textBgGradient,
          margin: EdgeInsets.all(4),
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
                    Text('Level'.trParams({'level': e.level.toString()}), style: textStyleHeading2(color: Colors.white),),
                    SizedBox(height: 8,),
                    // Text('Total: ${response.total}', style: textStyleLabel()),
                    Text('Total'.trParams({'total': e.total}), style: textStyleLabel(color: Colors.white)),
                  ],)),
                SizedBox(width: 8,),
                int.parse(e.total.trim())>0?TextButton(onPressed: (){
                  // controller.getLevelWise(response.level);
                  Get.to(AffLevelWiseTeam(tLevel:e.level.toString()));
                }, child: Text('View'.tr, style: textStyleHeading2(color: Colors.white),)):SizedBox(),
                Icon(Icons.chevron_right, color: Colors.white54,)
                // IconCell(title: status?'Active':'In-Active', image: 'assets/icons/ic_checked.png', active: status, size: 24,)
              ],),),
            onTap: (){
              // controller.getLevelWise(response.level);
              if(int.parse(e.total.trim())>0){
                Get.to(AffLevelWiseTeam(tLevel:e.level.toString()));
              }else{
                toastMessage('There is no member in team');
              }
              // Get.to(AffLevelWiseTeam(tLevel:e.level.toString()));
            },
          )),).toList()
      ),),
    );
  }

  @override
  void initState() {
    getUser().then((value){
      getLevelTeamAPI(value!.data.first.tblUserId.toString(), value!.data.first.jwtToken,
          value!.data.first.referalCode).then((value){
        setState(() {
          levelTeamModel=value.data;
          levelTeamModel!.data.sort((a,b){
            return a.level.compareTo(b.level);
          });
        });
      });
    });

    super.initState();
  }


}

