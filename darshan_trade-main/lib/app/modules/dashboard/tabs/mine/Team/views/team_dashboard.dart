import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/controllers/team_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/direct_team_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/level_team_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/rank_team_view.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

class TeamDashboard extends GetView<TeamController> {
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        centerTitle: true,
        title: Text('Teams', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
      ),
      body: Column(children: [
        Container(height: 40,
          alignment: Alignment.centerLeft,
          child: SATabview(titles: ['Direct_Team'.tr, 'Level_Team'.tr, 'Rank_Team'.tr],
            selectedColor: Colors.blueAccent, textColor: Colors.blueAccent, expand: true,
            onPageChange: _onPageChange, onSelect: (page){
              controller.pageController.animateToPage(page, duration: Duration(milliseconds: 800), curve: Curves.ease);
            },),),

        Expanded(child: PageView(
          controller: controller.pageController,
          onPageChanged: (page){
            _onPageChange.add(page);
          },
          children: [
            DirectTeamView(),
            LevelTeamView(),
            RankTeamView(),
        ],))

      ],),
    );
  }
}
