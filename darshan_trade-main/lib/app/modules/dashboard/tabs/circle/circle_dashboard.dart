import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/circle/robot.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/circle_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/robot_controller.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'circle.dart';

class CircleDashboard extends GetView<CircleController> {
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final PageController _pageController = PageController();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: Column(children: [
        Container(
          width: double.infinity,
          padding: EdgeInsets.only(top: 44),
          color: ColorConstants.APP_PRIMARY_COLOR,
        child: Container(height: 40,
          child: Row(
            mainAxisSize: MainAxisSize.max,
            children: [

            Expanded(child: SATabview(titles: ['Robot'.tr, 'Running Robot'.tr,
             /* 'Copy trade'.tr, 'Pro Traders'.tr,  'Future Trading'.tr*/],
              onPageChange: _onPageChange, onSelect: (page){
                _pageController.animateToPage(page, duration: Duration(milliseconds: 200), curve: Curves.ease);
                _onPageChange.add(page);
              }, expand: true,  isScroll: true,)),

          ],),),),
        Expanded(child: PageView(
          controller: _pageController,
          onPageChanged: (page){
            _onPageChange.add(page);
          },
          children: [
            Robot(),
            Circles(),
            Container(child: Center(child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('😇', style: textStyleTitle(fontSize: 80.0),),
                SizedBox(height: 8,),
                Text('Coming soon...')
            ],),),),
            Container(child: Center(child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('😇', style: textStyleTitle(fontSize: 80.0),),
                SizedBox(height: 8,),
                Text('Coming soon...')
            ],),),),
            Container(child: Center(child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('😇', style: textStyleTitle(fontSize: 80.0),),
                SizedBox(height: 8,),
                Text('Coming soon...')
            ],),),),
          ],
        ))
      ],),
    );
  }
}
