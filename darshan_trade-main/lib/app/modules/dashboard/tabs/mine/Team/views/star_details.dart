import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/controllers/team_controller.dart';
import 'package:royal_q/app/shared/shared.dart';

class StarDetails extends GetView<TeamController> {
  final String title;
  const StarDetails(this.title, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(title, style: textStyleHeading2(color: ColorConstants.white)),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.transparent,),

      body: Container(
        child: Obx(() => controller.rankWiseResponse.isNotEmpty?ListView.builder(itemBuilder: (context, index){
          RankWiseResponse model = controller.rankWiseResponse.elementAt(index);
          return _invitationCell(index, model);
        }, itemCount: controller.rankWiseResponse.length,):controller.rankWiseListReceived.value?NoRecord():Center(child: CircularProgressIndicator(),)),
      ),
      
    );
  }

  Widget _invitationCell(index, RankWiseResponse model) => Container(
    padding: EdgeInsets.all(8),
    color: index%2!=0?ColorConstants.white10:null,
    child: Column(children: [

      Row(children: [
        Expanded(child: Text('User ID: ${model.userid}')),
        Text('Trade Profit:'),
        Text('${model.tradeProfit} USDT', style: textStyleLabel(color: ColorConstants.blue),)
      ],),
      Row(children: [
        Expanded(child: Text('Mobile no: ${model.mobileNo}')),
        Text('Team Earning: ${model.teamearning} USDT'),
      ],),
      Row(children: [
        Expanded(child: Text('Level: ${model.level}')),
        Text('D.O.J: ${model.doj}'),
      ],),
      // Row(children: [
      //   Expanded(child: Text('Total Members: 0')),
      //   SizedBox()
      // ],),
      // Divider()
    ],),
  );
}
