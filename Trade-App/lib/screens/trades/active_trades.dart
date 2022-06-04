import 'dart:async';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/data_streamer.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:sprintf/sprintf.dart';
import 'dart:math' as math;

class ActiveTrades extends StatefulWidget {
  // final List<Trade>trade;
  const ActiveTrades({Key? key}) : super(key: key);

  @override
  State<ActiveTrades> createState() => _ActiveTradesState();
}

class _ActiveTradesState extends State<ActiveTrades> {
  bool isProfit=true;

  @override
  Widget build(BuildContext context) {

    return StreamBuilder(
        stream: tradeStreamController.stream,
        builder: (BuildContext context, AsyncSnapshot<List<Trade>> snapshot){
          if(!snapshot.hasData)return NoData();
          List<Trade>trades = snapshot.data!;
          List<Trade>trade = trades.where((element) => element.isActive).toList();
          return trade.length>0?ListView.builder(itemBuilder: (context, index){
            Trade _trade = trade.elementAt(index);
            return Column(children: [
              Container(
                margin: EdgeInsets.only(top: 8),
                padding: EdgeInsets.only(left: 16, top: 16, right: 16, bottom: 8),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('Active Trades', style: textStyleHeading(color: Colors.white),),
                    SizedBox(height: 24,),
                    Row(children: [
                      Expanded(child: Text('\$${_trade.bidValue}', style: textStyleHeading2(
                        color: Colors.white54,),)),
                      // Expanded(child: Text('${axisY.first.ticker.last}', style: textStyleHeading2(color: Colors.white54,), textAlign: TextAlign.end,)),
                      Expanded(child: StreamBuilder(
                          stream: streamController.stream,
                          builder: (BuildContext context, AsyncSnapshot<GraphModel> snapshot){
                            if(!snapshot.hasData)return Text('---', style:
                            textStyleHeading(color: APP_SECONDARY_COLOR),textAlign: TextAlign.left,);
                             isProfit = true;
                            if (_trade.type=='up') {
                              isProfit = snapshot.data!.ticker.last - _trade.model.ticker.last>0;
                            } else {
                              isProfit = (snapshot.data!.ticker.last - _trade.model.ticker.last<0);
                            }
                            return Row(children: [
                              Expanded(child: Container()),
                              isProfit?Text('+ \$${_trade.bidValue*0.90}', style: textStyleHeading2(color: Colors.green),)
                                  :Text('- \$${_trade.bidValue}', style: textStyleHeading2(color: Colors.red),)
                            ],);              })),
                    ],),
                    SizedBox(height: 4,),
                    Row(children: [
                      Expanded(child: Text('Invested', style: textStyleLabel(color: Colors.white54,),)),
                      Expanded(child: isProfit?Text('Profit', style: textStyleLabel
                        (color: Colors.white54,), textAlign: TextAlign.end,):Text('Loss', style: textStyleLabel
                        (color: Colors.white54,), textAlign: TextAlign.end,)),
                    ],),
                  ],),
                decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.05),
                    borderRadius: BorderRadius.circular(4)
                ),
              ),

              Container(
                margin: EdgeInsets.only(top: 8),
                padding: EdgeInsets.only(left: 16, top: 16, right: 16, bottom: 8),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(children: [
                      Image.asset('assets/images/icon/${_trade.currency}@2x.png',width: 36,),
                      SizedBox(width: 16,),
                      Expanded(child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('${_trade.currency}'.toUpperCase(), style: textStyleHeading2(color: Colors.white),),
                          Row(children: [
                            Expanded(child: Text('FTT 90%', style: textStyleHeading3(color: Colors.white54,),)),

                            _trade.type==UpDownType.UP?Transform.rotate(angle: 45*math.pi/180, child: Icon(Icons.arrow_upward, color: Colors.green, size: 14,),)
                                :Transform.rotate(angle: 135*math.pi/180, child: Icon(Icons.arrow_upward, color: Colors.red, size: 14,),),
                            Text('\$${_trade.bidValue}', style: textStyleHeading3(color: Colors.white54,), textAlign: TextAlign.end,),
                          ],),
                        ],)),
                    ],),
                    SizedBox(height: 8,),
                    Row(children: [
                      Expanded(child: DurationTimer(bidTime: _trade.model.at, bidDuration: _trade.duration),),

                      StreamBuilder(
                          stream: streamController.stream,
                          builder: (BuildContext context, AsyncSnapshot<GraphModel> snapshot){
                            if(!snapshot.hasData)return Text('---', style: textStyleHeading(color: APP_SECONDARY_COLOR),textAlign: TextAlign.left,);
                            bool isProfit = true;
                            if (_trade.type=='up') {
                              isProfit = snapshot.data!.ticker.last - _trade.model.ticker.last>0;
                            } else {
                              isProfit = (snapshot.data!.ticker.last - _trade.model.ticker.last<0);
                            }
                            return
                              isProfit?Text('+ \$${_trade.bidValue*0.9}', style:
                              textStyleHeading2(color: Colors.green),)
                                  :Text('- \$${_trade.bidValue}', style:
                              textStyleHeading2(color: Colors.red),);              })
                    ],),
                  ],),
                decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.05),
                    borderRadius: BorderRadius.circular(4)
                ),
              )

            ],);
          }, itemCount: trade.length,)
              :NoData();
        });
  }
}

class DurationTimer extends StatefulWidget {
  final int bidTime;
  final int bidDuration;
  const DurationTimer({Key? key, required this.bidTime, required this.bidDuration}) : super(key: key);

  @override
  _DurationTimerState createState() => _DurationTimerState();
}

class _DurationTimerState extends State<DurationTimer> {

  late Timer _timer;
  String _time = '01:00';
  int remain = 0;
  @override
  void initState() {

    _timer = Timer.periodic(Duration(seconds: 1), (timer) {
      int timeStamp = DateTime.now().millisecondsSinceEpoch;
       remain = widget.bidTime + widget.bidDuration - timeStamp;
       if(remain>0){
         if(mounted)setState(() {});
       }else{
         _timer.cancel();
       }

    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Text('${convertTime(remain)}', style: textStyleHeading2(color: Colors.white),),
    );
  }

  String convertTime(int timeInMilliseconds) {
    Duration timeDuration = Duration(milliseconds: timeInMilliseconds);
    int centiseconds = timeDuration.inMilliseconds ~/ 10;
    int seconds = timeDuration.inSeconds;
    int minutes = timeDuration.inMinutes;
    int hours = timeDuration.inHours;

    int min = seconds~/60;

    if (hours > 0){
      return sprintf('%i:%02i:%02i.%02i', [hours, minutes, seconds, centiseconds]);
    }else if(minutes > 0){
      return sprintf('%02i:%02i', [min, seconds%60]);
    }else {
      return sprintf('00:%02i', [seconds, centiseconds]);
    }
  }
}
