import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/reward_detail_report/reward_detail_item.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/reward_detail_report/reward_detail_report_controller.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/widgets/sa_tabview.dart';


class RewardDetailReportView extends  GetView<RewardDetailReportController>  {
  final List<String> _tabs = ['Full_details'.tr, 'Direct_qualifications'.tr, 'Team_qualifications'.tr,];

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(length: 3, child: Scaffold(
      appBar: AppBar(
        title: Text('Reward Detail Report', style: textStyleHeading2(color: ColorConstants.white),),
        elevation: 0,
        centerTitle: true,
        backgroundColor: Colors.transparent,
        iconTheme: IconThemeData(color: ColorConstants.white),
        bottom: TabBar(tabs: _tabs.map((e){
          return Tab(child: Text(e, ),);
        }).toList(), isScrollable: true, indicatorColor: ColorConstants.APP_SECONDARY_COLOR,
        unselectedLabelColor: ColorConstants.white54,
        labelColor: ColorConstants.white,),
      ),

      body: TabBarView(children: [
        Obx(() => controller.fullDetailList.isNotEmpty?RewardDetailItem(total: controller.fullDetailTotal.value, today: controller.fullDetailToday.value, list: controller.fullDetailList): controller.isFullListEmpty.value?NoRecord():Center(child: CircularProgressIndicator(),)),
        Obx(() => controller.directQualificationList.isNotEmpty?RewardDetailItem(total: controller.directQualificationTotal.value, today: controller.directQualificationToday.value, list: controller.directQualificationList): controller.isDirectQualificationEmpty.value?NoRecord():Center(child: CircularProgressIndicator(),)),
        Obx(() => controller.teamQualificationList.isNotEmpty?RewardDetailItem(total: controller.teamQualificationTotal.value, today: controller.teamQualificationToday.value, list: controller.teamQualificationList): controller.isTeamQualificationEmpty.value?NoRecord():Center(child: CircularProgressIndicator(),)),
      ]),
    ),
    );
  }
}

