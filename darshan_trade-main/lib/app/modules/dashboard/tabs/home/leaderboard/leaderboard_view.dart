import 'dart:async';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'leaderboard_controller.dart';

class LeaderboardView extends  GetView<LeaderboardController>  {
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final PageController _pageController = PageController();

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        title: Text('Leaderboard', style: textStyleHeading2(color: ColorConstants.whiteRev),),
        iconTheme: IconThemeData(color: ColorConstants.whiteRev),
        elevation: 0, centerTitle: true,
        backgroundColor: Colors.black,
      ),
      backgroundColor: ColorConstants.black,
      body: Container(
        child: Column(
          children: [
          Align(alignment: Alignment.center, child: Image.asset('assets/images/img_leaderboard.png', width: size.width/1.5,),),
            SizedBox(height: 16,),

            Container(
              padding: EdgeInsets.symmetric(vertical: 8),
              color: Color(0xFF2F2F2F),
              child: SATabview(titles: ['Today', 'Week', 'Month', 'All'],
              onPageChange: _onPageChange, onSelect: (page){
                _pageController.animateToPage(page, duration: Duration(milliseconds: 200), curve: Curves.ease);
                _onPageChange.add(page);
              }, expand: true, textColor: ColorConstants.whiteRev, unselectTextColor: ColorConstants.white54Rev,),),

            // SizedBox(height: 8,),
            // Divider(color: ColorConstants.white54Rev,),
            Container(

              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
              child: Row(children: [
                Expanded(child: Text('No', style: textStyleLabel(color: ColorConstants.whiteRev), ), flex: 20,),
                Expanded(child: Text('Nick name', style: textStyleLabel(color: ColorConstants.whiteRev),), flex: 55,),
                Expanded(child: Text('Profit(USDT)', style: textStyleLabel(color: ColorConstants.whiteRev),textAlign: TextAlign.right,), flex: 25,),
              ],),
            ),

            Divider(color: ColorConstants.white54Rev,),

            Expanded(child: PageView(
              controller: _pageController,
              onPageChanged: (page){
                _onPageChange.add(page);
              },
              children: [
              _showList(),
              _showList(),
              _showList(),
              _showList(),
            ],))
        ],),
      ),
    );
  }

  Widget _showList() => ListView.builder(itemBuilder: (context, index){
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      color: index%2==0?ColorConstants.white:ColorConstants.white10Rev,
      child: Row(children: [
        Expanded(child: Text('No. ${index+1}', style: textStyleLabel(color: ColorConstants.whiteRev), ), flex: 20,),
        ClipRRect(
          borderRadius: BorderRadius.circular(36.0),
          child: Image.asset('assets/expgain/icon_splash.png', width: 40, height: 40,),
        ),
        Expanded(child: Text('Nick name ', style: textStyleHeading3(color: ColorConstants.whiteRev),), flex: 60,),
        Expanded(child: Text('66.591623', style: textStyleLabel(color: ColorConstants.whiteRev),textAlign: TextAlign.right,), flex: 20,),
      ],),
    );
  });
}
  