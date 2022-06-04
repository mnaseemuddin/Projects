import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/sawidgets/sa_search.dart';
import 'package:sticky_headers/sticky_headers.dart';
import 'package:transparent_image/transparent_image.dart';

import 'community_controller.dart';

class CommunityView extends  GetView<CommunityController>  {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
      elevation: 0,
      centerTitle: true,
      title: Text('Community', style: textStyleHeading(color: ColorConstants.whiteRev),),
      backgroundColor: const Color(0xFFF87E02),
    ), 
      body: Stack(children: [
        Obx(() => Positioned(
          top: controller.topPosition.value,
          left: 0, right: 0,
          child: Container(height: 100, color: const Color(0xFFF87E02),),)),

          ListView(
            controller: controller.scrollController,
            children: [
              Obx(() => controller.communityResponse.value!=null?Container(
                margin: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                padding: EdgeInsets.symmetric(horizontal: 16, vertical: 16),

                child: Column(children: [
                  Text('Cumulative Invitation Registration Bonus', style: textStyleHeading2(),),
                  _customBox(Text('${controller.communityResponse.value!.regitrationBonus} USDT',
                    style: textStyleHeading(color: ColorConstants.whiteRev), textAlign: TextAlign.center,)),
                  Text('Number of friends inviting ${controller.communityResponse.value!.totalinviting}', style: textStyleHeading3(),),
                  SizedBox(height: 16,),
                  Padding(padding: EdgeInsets.symmetric(vertical: 16, horizontal: 8),
                    child: Container(height: 1, color: ColorConstants.white54, width: double.infinity,),),

                  _cell2D('First level members', '${controller.communityResponse.value!.firstlevelmem}'),
                  _cell2D('Second level members', '${controller.communityResponse.value!.secondlevelmem}'),
                  _cell2D('Active users', '${controller.communityResponse.value!.activeuser}'),
                  _cell2D('Team Active Trade Volume', '${controller.communityResponse.value!.teamactivetradevol} USDT'),
                  _cell2D('Inactive users', '${controller.communityResponse.value!.inactiveusers}'),
                ],),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(16),
                    color: ColorConstants.whiteRev
                ),
              ): Center(child: CircularProgressIndicator(),)),
              Obx(() => controller.communityResponse.value!=null?Row(children: [
                SizedBox(width: 8,),
                Expanded(child: SACellContainer(child: Column(children: [
                  Text('Agent Identity'),
                  SizedBox(height: 8,),
                  Text(controller.communityResponse.value!.agentidentity, style: textStyleLabel(color: ColorConstants.white54),),
                ],), color: ColorConstants.whiteRev)),
                SizedBox(width: 8,),
                Expanded(child: SACellContainer(child: Column(children: [
                  Text('Dividend ratio'),
                  SizedBox(height: 8,),
                  Text('${controller.communityResponse.value!.ratio}%', style: textStyleLabel(color: ColorConstants.white54),),
                ],),color: ColorConstants.whiteRev)),
                SizedBox(width: 8,),
              ],):SizedBox()),

              StickyHeader(header: Container(
                color: ColorConstants.whiteRev,
                padding: EdgeInsets.all(8),
                margin: EdgeInsets.only(bottom: 8),
                child: Row(children: [
                  Text('All Members', style: textStyleHeading2(),),
                  SizedBox(width: 40,),
                  Expanded(child: SASearch(onChange: (val){
                    controller.communityListResponseSearch.value = controller.communityListResponse.where((p) => p.userid.contains(val)).toList();
                  }, isPrefix: false, hintText: 'Search user',))
                  // SASearch(onChange: onChange)
                ],),
              ), content: SACellContainer(
                  margin: EdgeInsets.symmetric(horizontal: 8),
                  padding: EdgeInsets.only(top: 16),
                  child: Obx(() => Column(
                    // children: List.generate(20, (index) => _invitationCell(index)),
                    children: controller.communityListResponseSearch.map((data){
                      return _invitationCell(data);
                    }).toList(),
                  )))),

              // Padding(padding: EdgeInsets.only(left: 8, top: 16, bottom: 8),child: Row(children: [
              //   Text('All Members', style: textStyleHeading2(),),
              //   // SASearch(onChange: onChange)
              // ],),),

              // SACellContainer(
              //     margin: EdgeInsets.symmetric(horizontal: 8),
              //     padding: EdgeInsets.only(top: 16),
              //     child: Obx(() => Column(
              //       // children: List.generate(20, (index) => _invitationCell(index)),
              //       children: controller.communityListResponse.map((data){
              //         return _invitationCell(data);
              //       }).toList(),
              //     )))
          ],)

      ],),
    );
  }

  Widget _cell2D(title, value) => _customBox(Row(children: [
    Expanded(child: Text(title, style: textStyleLabel(color: ColorConstants.whiteRev),)),
    Text(value, style: textStyleLabel(color: ColorConstants.whiteRev))
  ],));

  Widget _customBox(child) => SACellContainer(child: child,
      width: double.infinity, padding: EdgeInsets.all(8),
  color: Color(0xFFF87E02));


  Widget _invitationCell(CommunityListResponse model) => Container(
    padding: EdgeInsets.all(8),
    // color: index%2!=0?ColorConstants.white10:null,
    child: Column(children: [

      Row(children: [
        Expanded(child: Text('User ID: ${model.userid}')),
        Text('Trade Profit:'),
        Text('${model.tradeProfit} USDT', style: textStyleLabel(color: ColorConstants.blue),)
      ],),
      Row(children: [
        Expanded(child: Text('Mobile No: ${model.mobileNo}')),
        Text('Team Earning: ${model.teamearning} USDT'),
      ],),
      Row(children: [
        Expanded(child: Text('Level: ${model.level}')),
        Text('D.O.J: ${model.doj}'),
      ],),
      // Row(children: [
      //   Expanded(child: Text('Total Members: ${model.}')),
      //   SizedBox()
      // ],),
      Divider()
    ],),
  );
}
  