



import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/Team/direct_team_view.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/Team/rank_team_view.dart';
import '../../../../shared/constants/colors.dart';
import '../../../../shared/constants/common.dart';
import '../../../../shared/sawidgets/sa_tabview.dart';
import 'level_team_view.dart';


class AffiliateTeamView extends StatefulWidget {
  const AffiliateTeamView({Key? key}) : super(key: key);

  @override
  _AffiliateTeamViewState createState() => _AffiliateTeamViewState();
}

class _AffiliateTeamViewState extends State<AffiliateTeamView> {
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  PageController pageController=PageController();
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      backgroundColor: Colors.transparent,
      appBar: AppBar(
        leading: Container(),
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Text('Teams', style: textStyleHeading2(color: Colors.white),),
        iconTheme: IconThemeData(color: Colors.white),
      ),
      body: Column(children: [
        Container(height: 40,
          alignment: Alignment.centerLeft,
          child: SATabview(titles: ['Direct_Team'.tr, 'Level_Team'.tr],
            selectedColor: Color(0xFFFF5555), textColor: Color(0xFFFF5555), expand: true,
            unselectTextColor: Colors.white54,
            onPageChange: _onPageChange, onSelect: (page){
              pageController.animateToPage(page, duration: Duration(milliseconds: 800), curve: Curves.ease);
            },),),

        Expanded(child: PageView(
          controller: pageController,
          onPageChanged: (page){
            _onPageChange.add(page);
          },
          children: [
              AffDirectTeamView(),
             AffLevelTeamView(),
            // RankTeamView(),
          ],))

      ],),
    );
  }

}
