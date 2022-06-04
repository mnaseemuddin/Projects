
import 'package:flutter/material.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/utils/NoData.dart';
import '../../../../../api/apis.dart';
import '../../model/DirectTeamResponse.dart';


class AffDirectTeamView extends StatefulWidget {
  const AffDirectTeamView({Key? key}) : super(key: key);

  @override
  State<AffDirectTeamView> createState() => _AffDirectTeamViewState();
}

class _AffDirectTeamViewState extends State<AffDirectTeamView> {

  AffDirectTeamResponse? directTeamModel;

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
          margin: EdgeInsets.symmetric(horizontal: 4),
          child: directTeamModel==null?AFNoRecord():SingleChildScrollView(
            scrollDirection: Axis.vertical,
            child: Column(
              children: directTeamModel!.data.map((e) =>Container(
                  decoration: textBgGradient,
                  margin: EdgeInsets.all(4),
                  padding: EdgeInsets.all(8),
                  child: Container(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                    child: Row(children: [
                      ClipRRect(
                        borderRadius: BorderRadius.circular(36.0),
                        // child: Image.network(
                        //   'https://static.vecteezy.com/system/resources/previews/002/318/271/non_2x/user-profile-icon-free-vector.jpg',
                        //   height: 64.0,
                        //   width: 64.0,
                        // ),
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
                          // Text('Name: ${response.name}', style: textStyleHeading2(),),
                          Text('UUID- ${e.referalCode}', style: textStyleHeading2(color: Colors.white),),
                          Text('Contact- ${e.mobile}', style: textStyleLabel(color: Colors.white),),

                          // Text('Name'.trParams({'name': response.name}), style: textStyleHeading2(),),
                          SizedBox(height: 8,),
                          // Text('Username: ${response.username}', style: textStyleLabel()),
                          // Text('Username'.trParams({'name': response.username}), style: textStyleLabel()),
                        ],)),
                      SizedBox(width: 8,),
                      AFIconCell(title:e.amountStake.toString()=="1"?'Active':"In-Active",
                        image: 'assets/icons/ic_checked.png', active: e.amountStake.
                        toString()=="1"?true:false, size: 24, color: Color(0xFFFF5555),)
                    ],),)),).toList(),
            ),
          )),
    );
  }

  @override
  void initState() {
    getUser().then((value){
      print(value!.data.first.tblUserId.toString());
      getDirectTeamAPI(value.data.first.tblUserId.toString(),value.data.first.jwtToken,
          value.data.first.referalCode
      ).then((value1){
        setState(() {
          directTeamModel=value1.data;
        });
      });
    });

    super.initState();
  }
}
