import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/data_streamer.dart';
import 'package:trading_apps/graph_widgets/main_graph.dart';
import 'package:intl/intl.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:uuid/uuid.dart';

class GraphPlotter extends StatefulWidget {
  final StreamController<TradeModel> onMarketchanged;
  const GraphPlotter({Key? key, required this.onMarketchanged}) : super(key: key);

  @override
  _GraphPlotterState createState() => _GraphPlotterState();
}

class _GraphPlotterState extends State<GraphPlotter> {
  Offset _onOffsetChange = Offset(0, 0);
  ScrollController _scrollController = ScrollController();
  Timer? _timerValue;
  int addFactor = 60;
  int bidValue = 1;

  @override
  void initState() {

    getAmount().then((value){
      bidValue = value;
    });

    getDuration().then((value){
      addFactor = value*60;
    });

    widget.onMarketchanged.stream.listen((event) {
      TradeModel model = event;
      switch(event.cond){
        case 0:
          isGraphInit = false;
          axisY.clear();
          break;

        case 1:
          if(lastGetValue!=null){
            int time = lastGetValue!.at;
            double last = lastGetValue!.ticker.last;
            String uuid = Uuid().v1();
            print('Base Market value ==================${model.tradeType} time=> $time, last => $last');

            Map body = {
              'user_id': userModel!.data.first.userId,
              'unique_id': uuid,
              'bet_type': model.tradeType==UpDownType.UP?'up':'down',
              'bet_time': time,
              'bet_price': bidValue,
              'currency_rate': lastGetValue!.ticker.last,
              'currency_value': baseMarket,
            };

            addBetAPI(body).then((value){
              ApiResponse response = value;
              if(response.status){
                // axisY.sort((a, b)=> b.at.compareTo(a.at));

                GraphModel graphModel = lastGetValue!;
                GraphModel modelTemp = GraphModel(at: lastGetValue!.at,
                    ticker: Ticker(buy: graphModel.ticker.buy, sell: graphModel.ticker.sell,
                        low: graphModel.ticker.low, high: graphModel.ticker.high,
                        last: graphModel.ticker.last, vol: graphModel.ticker.vol));

                Trade tradeTemp = Trade(model: modelTemp, type: model.tradeType==UpDownType.UP?UpDownType.UP:UpDownType.DOWN,
                    duration: addFactor*1000, bidValue: bidValue, isActive: true, currency: baseMarket, uuid: uuid);


                // trade.add(Trade(model: lastGetValue!, type: model.tradeType==UpDownType.UP?UpDownType.UP:UpDownType.DOWN,
                //     duration: addFactor*1000, bidValue: bidValue, isActive: true, currency: baseMarket, uuid: uuid));
                trade.add(tradeTemp);
                // tradeStreamController.add(trade);
                tradeStreamController.add(trade);
                axisY.add(modelTemp);
                Fluttertoast.showToast(msg: response.data['message']);
              }else{
                Fluttertoast.showToast(msg: 'Something went wrong');
              }
            });
          }
          break;

        case 2:
          // setState(() => addFactor = event.addFactor);
          addFactor = event.addFactor;
          break;

        case 3:
          // setState(() => bidValue = event.addFactor);
          bidValue = event.addFactor;
          break;

        default:

          isGraphInit = false;
          axisY.clear();
          break;
      }
    });

    _scrollController.addListener(() {
      setState(() {
        _onOffsetChange = Offset(_scrollController.offset, 0);
      });
    });
    
    super.initState();
  }

  @override
  void dispose() {
    if(_timerValue!=null)
      _timerValue!.cancel();
    super.dispose();
  }


  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return SingleChildScrollView(
      controller: _scrollController,

      child: StreamBuilder(
        stream: streamController.stream,
        builder: (BuildContext context, AsyncSnapshot<GraphModel> snapshot){
        if(!snapshot.hasData || !isGraphInit)return Center(child: CircularProgressIndicator(
          color: APP_SECONDARY_COLOR,
        ),);
        return CustomPaint(
          size: Size(4*size.width, size.height),
          painter: MainGraph(onOffsetChange: _onOffsetChange,
              difference: addFactor*1000, bidValue: bidValue, onCmpleted: (item){}),
        );
      },),

      scrollDirection: Axis.horizontal,
      reverse: true,
    );
  }
}
