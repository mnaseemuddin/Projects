
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/models/history_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'dart:math' as math;

class ClosedTrades extends StatefulWidget {
  const ClosedTrades({Key? key}) : super(key: key);

  @override
  _ClosedTradesState createState() => _ClosedTradesState();
}

class _ClosedTradesState extends State<ClosedTrades> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
        child: FutureBuilder(
            future: betHistoryAPI(userModel!.data.first.userId),
            builder: (context, snapshot){
              if(!snapshot.hasData)return Center(child: CircularProgressIndicator(),);
              ApiResponse response = snapshot.data as ApiResponse;
              if(!response.status) return Center(child:NoData(),);
              HistoryModel model = response.data;
              List<Histories> histories = model.data;
              histories.sort((a, b)=> b.betTime.compareTo(a.betTime));
              if(histories.length<=0)return Center(child: NoData(),);
          return ListView.builder(itemBuilder: (context, index){
            Histories history = histories.elementAt(index);
            bool isUp = history.betType=='up';
            bool isWin = history.betResult=='win';
            double currencyRate=double.parse(history.currencyRate);
            return Container(
              margin: EdgeInsets.only(top: 8),
              padding: EdgeInsets.only(left: 16, top: 16, right: 16, bottom: 8),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(children: [
                    Image.asset('assets/images/icon/${history.currencyValue}@2x.png',width: 36,),
                    SizedBox(width: 16,),
                    Expanded(child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                      Text('${history.currencyValue}'.toUpperCase(), style: textStyleHeading2(color: Colors.white),),

                      Row(children: [
                        Expanded(child: Text('FTT 90%', style: textStyleHeading3(color: Colors.white54,),)),

                        isUp?Transform.rotate(angle: 45*math.pi/180, child: Icon(Icons.arrow_upward, color: Colors.green, size: 14,),)
                            :Transform.rotate(angle: 135*math.pi/180, child: Icon(Icons.arrow_upward, color: Colors.red, size: 14,),),

                        Text('\$${history.betPrice}', style: textStyleHeading3(
                          color: Colors.white54,), textAlign: TextAlign.end,),
                      ],),
                    ],)),
                  ],),
                  Row(children: [
                    Expanded(child: Container()),
                    isWin?Icon(Icons.add, color: Colors.green,size: 18,):Icon(Icons.remove, color: Colors.red, size: 18,),
                    isWin?Text('\$${history.winningAmount}', style: textStyleHeading2(color: Colors.green),)
                    :Text('\$${history.betPrice}', style: textStyleHeading2(color: Colors.red),)

                  ],),
                  Row(children: [
                    Expanded(child: Text('Opening quote', style: textStyleLabel(color: Colors.white70),),),
                    Text('\$${currencyRate.toStringAsFixed(3)}',
                        style: textStyleLabel(color: Colors.white70))
                  ],),
                  Row(children: [
                    Expanded(child: Text('Opening time', style: textStyleLabel(color: Colors.white54),),),
                    Text('${DateFormat().format(DateTime.fromMillisecondsSinceEpoch(int.parse(history.betTime)))}',
                        style: textStyleLabel(color: Colors.white54))
                  ],),
                ],),
              decoration: BoxDecoration(
                  color: Colors.white.withOpacity(0.05),
                  borderRadius: BorderRadius.circular(4)
              ),
            );
          }, itemCount: histories.length,);
        }),
      ),
    );
  }
}


