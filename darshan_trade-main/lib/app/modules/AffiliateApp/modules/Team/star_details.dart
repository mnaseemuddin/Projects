import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/controllers/team_controller.dart';
import 'package:royal_q/app/shared/shared.dart';


class AffStarDetail extends StatefulWidget {
  const AffStarDetail({Key? key}) : super(key: key);

  @override
  State<AffStarDetail> createState() => _AffStarDetailState();
}

class _AffStarDetailState extends State<AffStarDetail> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Star1', style: textStyleHeading2(color: ColorConstants.white)),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.transparent,),

      body: Container(
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Column(children: [

              Row(children: [
                Expanded(child: Text('User ID: DAR458')),
                Text('Trade Profit:'),
                Text('14525 USDT', style: textStyleLabel(color: ColorConstants.blue),)
              ],),
              Row(children: [
                Expanded(child: Text('Mobile no: 985552556')),
                Text('Team Earning: 1000 USDT'),
              ],),
              Row(children: [
                Expanded(child: Text('Level: 2')),
                Text('D.O.J: 04/05/2022'),
              ],),
              // Row(children: [
              //   Expanded(child: Text('Total Members: 0')),
              //   SizedBox()
              // ],),
              // Divider()
            ],),
          )),

    );
  }
}

