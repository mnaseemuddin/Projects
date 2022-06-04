import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/res/constants.dart';

import 'api/apis.dart';


List<GraphModel>axisY = [];
List<Trade>trade = [];
GraphModel? lastGetValue;
double? lastValue;
GraphModel? lastFreezeValue;
bool isGraphInit = false;
bool _isAdded = false;

int addFactor = 60;
int bidValue = 1;
int countSameData =0;
int countUpdateData = 0;


int divideFactor = 2500;
int randomSec = 10;
int randomSign = -1;

StreamController<GraphModel> streamController = StreamController<GraphModel>.broadcast();
StreamController<List<Trade>> tradeStreamController = StreamController<List<Trade>>.broadcast();
StreamController<bool> bidCompleted = StreamController<bool>.broadcast();

initDataStreamer(){
  accessAPI();
  Timer.periodic(Duration(milliseconds: 200), (timer) {
    countUpdateData +=1;
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    if(lastGetValue!=null&&!_isAdded){
      lastGetValue!.at = timestamp;
      double diffVal = lastGetValue!.ticker.high - lastGetValue!.ticker.low;
      // double nextInt = diffVal/20;
      divideFactor = 1000 + Random().nextInt(2500);
      double nextInt = diffVal/divideFactor;
      // double nextInt = diffVal/(Random().nextDouble()*diffVal);
      double randomValue = Random().nextDouble()*nextInt;
      int modRandom = 1+Random().nextInt(4);
      lastGetValue!.ticker.last += (Random().nextInt(100)%modRandom==0?-1:1)*randomValue.toDouble();

      // graphMax = lastGetValue!.ticker.last>graphMax?lastGetValue!.ticker.last:graphMax;
      // graphMin = lastGetValue!.ticker.last>graphMin?lastGetValue!.ticker.last:graphMin;

      if(countUpdateData>20){
        countUpdateData = 0;
        GraphModel model = GraphModel(at: lastGetValue!.at,
            ticker: Ticker(buy: lastGetValue!.ticker.buy, sell: lastGetValue!.ticker.sell,
                low: lastGetValue!.ticker.low, high: lastGetValue!.ticker.high,
                last: lastGetValue!.ticker.last, vol: lastGetValue!.ticker.vol));
        lastGetValue = model;
        axisY.add(model);
        streamController.add(model);
      }else{
        axisY.add(lastGetValue!);
        streamController.add(lastGetValue!);
      }
    }
    _isAdded = false;

    trade.forEach((element) {
      if(element.isActive){
        if(element.duration + element.model.at - timestamp<=0){
          element.isActive = false;
          _getBetResult(element);
          Fluttertoast.showToast(msg: 'Bet completed');
          tradeStreamController.add(trade);
        }
      }
    });
    tradeStreamController.add(trade);
  });

  Timer.periodic(Duration(milliseconds: 10000), (timer) {

    accessAPI();

    // axisY.forEach((element) {
    //   double high = element.ticker.last>element.ticker.high?element.ticker.last:element.ticker.high;
    //   double low  = element.ticker.last<element.ticker.low ?element.ticker.last :element.ticker.low;
    //   // graphMax = graphMax<high?high:graphMax;
    //   // graphMin = graphMin> low? low:graphMin;
    // });

    // print('Timer minVal => $graphMin, graphMax => $graphMax');

  });
}

_getBetResult(Trade element){

  graphDataAPI(baseMarket: element.currency+'usdt').then((value){
    ApiResponse response = value;
    if(response.status){
      GraphModel model  = response.data;

      Map body = {
        'user_id': userModel!.data.first.userId,
        'unique_id': element.uuid,
        'bet_type': element.type==UpDownType.UP?'up':'down',
        'bet_price': element.bidValue,
        'currency_rate': model.ticker.last,
        'currency_value': element.currency,
      };

      betResultAPI(body).then((value){
        ApiResponse response = value;
        if(response.status){
          String message = response.data['message'];
          Color color = message.contains('win')?Colors.green:Colors.red;
          Fluttertoast.showToast(msg: message, gravity: ToastGravity.CENTER,
              backgroundColor: color);
          bidCompleted.add(message.contains('win'));
        }
      });
    }
  });
}

accessAPI(){
  graphDataAPI(baseMarket: baseMarket+'usdt').then((value){
    ApiResponse response = value;
    if(response.status){
      GraphModel model  = response.data;

      if(isMarketChanged){
        axisY.clear();
        isMarketChanged = false;
        // graphMax = model.ticker.high;
        // graphMin = model.ticker.low;

        axisY.add(model);
        streamController.add(model);
        lastGetValue = model;

      }else if(lastValue!=null){
        print('Last received value => ${model.ticker.last} => $lastValue');
        if(lastValue != model.ticker.last || countSameData>4){
          if(countSameData>4){
            double diffVal = lastGetValue!.ticker.high - lastGetValue!.ticker.low;
            double nextInt = diffVal/divideFactor;
            double randomValue = Random().nextDouble()*nextInt;
            model.ticker.last += (randomValue%2==0?-1:1)*randomValue.toDouble();
            double currentVal = (randomValue%2==0?-1:1)*randomValue.toDouble();
            model.ticker.last += currentVal;
          }
          countSameData = 0;

          // graphMax = model.ticker.last>graphMax?model.ticker.last:graphMax;
          // graphMin = model.ticker.last>graphMin?model.ticker.last:graphMin;

          axisY.add(model);
          streamController.add(model);
          lastGetValue = model;
        }else countSameData +=1;


      }else{
        // graphMax = model.ticker.last>graphMax?model.ticker.last:graphMax;
        // graphMin = model.ticker.last>graphMin?model.ticker.last:graphMin;

        axisY.add(model);
        streamController.add(model);
        lastGetValue = model;
      }

      lastValue = model.ticker.last;
      lastFreezeValue = model;
      _isAdded = true;
      if(!isGraphInit&&axisY.length>0){
        isGraphInit = true;
      }
    }
  });
}

Future<List<Trade>> getTrades() async{
  return trade;
}