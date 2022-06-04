



import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/apis.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffLevelWiseTeamResponse.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';

import '../../../../shared/constants/colors.dart';
import '../../../../shared/constants/common.dart';
import '../../../../shared/sawidgets/common_widget.dart';
import '../../../TradeSettings/views/currency_detail_view.dart';

class AffLevelWiseTeam extends StatefulWidget {

  String tLevel;

   AffLevelWiseTeam({Key? key,required this.tLevel}) : super(key: key);

  @override
  State<AffLevelWiseTeam> createState() => _AffLevelWiseTeamState();
}

class _AffLevelWiseTeamState extends State<AffLevelWiseTeam> {

  AffLevelWiseTeamResponse? levelWiseModel;



  @override
  Widget build(BuildContext context) {
    return AFBg(child: Scaffold(
      backgroundColor: Colors.transparent,
      appBar: AppBar(
        // title: Text('Level: ${levelTeamResponse.level}'),
        title: Text('Level'.trParams({'level': '${widget.tLevel}'}), style: textStyleHeading2(color: Colors.white),),
        iconTheme: IconThemeData(color: Colors.white),
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.transparent,
        brightness: Brightness.dark,
      ),
      body: Container(
          margin: EdgeInsets.symmetric(horizontal: 8),
          child: levelWiseModel==null?NoRecord():Column(
              children: levelWiseModel!.data.map((e) => Container(
                  decoration: textBgGradient,
                  padding: EdgeInsets.all(8),
                  child: Container(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                    child: Row(children: [
                      ClipRRect(
                        borderRadius: BorderRadius.circular(36.0),
                        child: Image.asset(
                          'assets/expgain/icon_splash.png',
                          height: 64.0,
                          width: 64.0,
                        ),
                      ),
                      SizedBox(width: 8,),
                      Expanded(child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          // Text('Name'.trParams({'name': response.name}), style: textStyleHeading2(),),
                          // Text(response.name, style: textStyleHeading2(),),
                          Text('UUID- ${e.referalCode}', style: textStyleHeading2(color: Colors.white),),
                          Text('Contact- ${e.mobile}', style: textStyleLabel(color: Colors.white),),

                          SizedBox(height: 8,),
                          // Text('Username'.trParams({'name': response.username}), style: textStyleLabel()),
                        ],)),
                      SizedBox(width: 8,),
                      AFIconCell(title:e.amountStake.toString()=="1"?'Active':"In-Active",
                        image: 'assets/icons/ic_checked.png', active: e.amountStake.
                        toString()=="1"?true:false, size: 24, color: Colors.green[200],)
                    ],),)),).toList()
          )),
    ));
  }

  @override
  void initState() {
    getUser().then((value){
      var hashMap={
        "referal_code":value!.data.first.referalCode,
        "level":widget.tLevel
      };
      print(hashMap);
      print(value!.data.first.jwtToken);
      getLevelWiseTeamAPI(value!.data.first.tblUserId.toString(), value!.data.first.jwtToken,
          value!.data.first.referalCode,hashMap).then((value){
        setState(() {
          levelWiseModel=value.data;
        });
      });
    });


    super.initState();
  }
}
