import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:ionicons/ionicons.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/popup_dialog.dart';
import 'package:trading_apps/data_streamer.dart';
import 'package:trading_apps/graph_widgets/graph_plotter.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/wallet_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/currency_selection.dart';
import 'package:trading_apps/screens/profile.dart';
import 'package:trading_apps/screens/trades/deposit_currency.dart';
import 'package:trading_apps/screens/trades/withdrawl.dart';
import 'package:trading_apps/utility/common_methods.dart';


class Home extends StatefulWidget {

  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  StreamController<TradeModel> _onMarketChange = StreamController<TradeModel>.broadcast();
  double _amountOpacity = 1;

  String userName='';

  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light);
    bidCompleted.stream.listen((event) {
      print('object => $event  => 😀');
      setState(() => _amountOpacity = 0);
      Timer(Duration(milliseconds: 500), (){
        setState(() => _amountOpacity = 1);
      });
    });
    userDetailsAPI(userModel!.data.first.userId).then((value){
      setState(() {
       userName= userModel!.data.first.username;
        debugPrint("Name  :"+userName);
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: true,
      child: Container(
        // padding: EdgeInsets.only(left: 8, right: 8),
        child: Column(
          children: [
            _topView(),
            SizedBox(height: 8,),
            Expanded(child: GraphPlotter(onMarketchanged: _onMarketChange,)),
            SizedBox(height: 8,),
            _bottomView()
          ],
        ),
      ),
    );
  }

  Widget _topView() => Padding(padding: EdgeInsets.only(left: 8, right: 8,top: 0),
  child: Column(children: [
    _profileView(),
    SizedBox(height: 2,),
    _currencyType()
  ],),);

  Widget _currencyType() => Row(children: [
    Expanded(child: GestureDetector(
      child: ContainerBg(child: Row(children: [
        Image.asset('assets/images/icon/$baseMarket@2x.png', width: 36,),
        SizedBox(width: 4,),
        Text('${baseMarket.toUpperCase()}', style: textStyleHeading2(color: Colors.white),)
      ],)),
      onTap: () async {
        var res = await navPush(context, CurrencyDialog(currencies: TRADE_CURRENCIES,));
        if(res!=null){
          print(res);
          if(baseMarket!=res['val']){
            baseMarket = res['val'];
            setCurrency(baseMarket);
            isMarketChanged = true;
            isGraphInit = false;
            axisY.clear();

            mMaxVal =

            mMinVal = double.maxFinite;
            mMaxVal = double.negativeInfinity;

            graphMin = double.maxFinite;
            graphMax = double.negativeInfinity;


            accessAPI();
            _onMarketChange.add(TradeModel(marketChange: true));
            setState(() {});
          }
        }

      },
    )),

    //-{at: 1642676828, ticker: {buy: 369.8, sell: 373.5, low: 367.5, high: 375.0, last: 369.8, vol: 2.8489}}

    GestureDetector(child: ContainerBg(child: Text('$mainDuration\m',
      style: textStyleHeading2(color: Colors.white),), height: 44,),
    onTap: (){
      List<String> list = ['5m', '10m', '30m', '1h', '6h', '12h'];
      Navigator.of(context).push(SelectCategoryDialog(title: SELECT_CURRENCY, list: list, onSelect: (string) {
        setState(() {
          switch(string){
            case '5m':
              updateTimeDuration(5);
              mainDuration = 5;
              durationFactor = mainDuration*60*1000;
              timeDurationFactor = mainDuration*60;
              break;
            case '10m':
              updateTimeDuration(10);
              mainDuration = 10;
              durationFactor = mainDuration*60*1000;
              timeDurationFactor = mainDuration*60;
              break;
            case '30m':
              updateTimeDuration(30);
              mainDuration = 30;
              durationFactor = mainDuration*60*1000;
              timeDurationFactor = mainDuration*60;
              break;
            case '1h':
              updateTimeDuration(60);
              mainDuration = 60;
              durationFactor = mainDuration*60*1000;
              timeDurationFactor = mainDuration*60;
              break;
            case '6h':
              updateTimeDuration(360);
              mainDuration = 360;
              durationFactor = mainDuration*60*1000;
              timeDurationFactor = mainDuration*60;
              break;
            case '12h':
              updateTimeDuration(720);
              mainDuration = 720;
              durationFactor = mainDuration*60*1000;
              timeDurationFactor = mainDuration*60;
              break;
            default:
              updateTimeDuration(5);
              mainDuration = 5;
              durationFactor = mainDuration*60*1000;
              timeDurationFactor = mainDuration*60;
              break;
          }
        });
      }));

    },),
  ],);

  Widget _profileView() => Container(
    child: Row(children: [
      GestureDetector(child: Row(children: [
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('${userModel!.data.first.username}', style: textStyleLabel(),),
            // Text('D10,000.00', style: textStyleHeading2(color: Colors.white),),
            FutureBuilder(
                future: userWalletAPI(userModel!.data.first.userId),
                builder: (context, AsyncSnapshot<ApiResponse>snapshot){
                if(!snapshot.hasData)return Text('--:--', style: textStyleHeading2(color: Colors.white),);
                ApiResponse? resp = snapshot.data;
                if(!resp!.status)return Text('--:--', style: textStyleHeading2(color: Colors.white),);
                WalletModel model = resp?.data;
                walletAmount = double.parse(model.data.first.walletAmount);
                return AnimatedOpacity(opacity: _amountOpacity, duration: Duration(milliseconds: 500),
                child: Text('\$'+walletAmount.toStringAsFixed(2), style: textStyleHeading2(color: Colors.white)),);
            })
          ],),
      ],), onTap: (){},),
    //  Icon(Icons.keyboard_arrow_down_outlined, color: Colors.white,),
      GestureDetector(
        child: Container(
          margin: EdgeInsets.only(left: 16),
          padding: EdgeInsets.all(8),
          child: Text('Deposit', style: textStyleHeading(color: APP_PRIMARY_COLOR),),
          decoration: BoxDecoration(
            color: APP_SECONDARY_COLOR,
            borderRadius: BorderRadius.circular(8)
          ),
        ),
        onTap: (){
          navPush(context, DepositCurrency());
        },
      ),

      // GestureDetector(
      //   child: Container(
      //     margin: EdgeInsets.only(left: 16),
      //     padding: EdgeInsets.all(8),
      //     child: Text('Withdraw', style: textStyleHeading(color: APP_PRIMARY_COLOR),),
      //     decoration: BoxDecoration(
      //         color: APP_SECONDARY_COLOR,
      //         borderRadius: BorderRadius.circular(8)
      //     ),
      //   ),
      //   onTap: (){
      //     navPush(context, Withdrawal());
      //   },
      // ),
      //
      Expanded(child: Container()),
      IconButton(onPressed: (){
        navPush(context, Profile());
      }, icon: Icon(Ionicons.person_circle_outline, color: Colors.white,))
    ],),
  );

  Widget _bottomView() => Padding(padding: EdgeInsets.only(left: 8, right: 8),
    child: Column(children: [
      Container(
        child: Row(children: [
          Expanded(child: FutureBuilder(
              future: getDuration(),
              builder: (context, AsyncSnapshot<int>snapshot){
                if(!snapshot.hasData)return Text('--');
            return InrDcrButton(time: snapshot.data!, unit: 'T', onPressed: (time){
              print('Time => $time');
              _onMarketChange.add(TradeModel(cond: 2, marketChange: true, addFactor: time*60));
            },);
          }),),
          SizedBox(width: 8,),
          Expanded(child: FutureBuilder(
              future: getAmount(),
              builder: (context, AsyncSnapshot<int>snapshot){
                if(!snapshot.hasData)return Container(width: 0,);
                selectAmount = snapshot.data!;
            return InrDcrButton(time: snapshot.data!, unit: '\$', onPressed: (time){
              print('Time => $time');
              _onMarketChange.add(TradeModel(cond: 3, marketChange: true, addFactor: time));
            },);
          }),),
        ],),
      ),
      SizedBox(height: 4,),
      Container(
        child: Row(children: [
          Expanded(child: TradeButton(color: Colors.red, onPressed: (){
            // _onMarketChange.add(TradeModel(cond: 1, marketChange: true, tradeType: UpDownType.DOWN));
            if(selectAmount < walletAmount){
              _onMarketChange.add(TradeModel(cond: 1, marketChange: true, tradeType: UpDownType.DOWN));
            }else{
              Fluttertoast.showToast(msg: 'your balance is not enough');
            }
          },)),
          Padding(padding: EdgeInsets.only(left: 4, right: 4),
            child: ContainerBg(child: Icon(Icons.watch_later_outlined,
              color: Colors.white,), height: 48, width: 48,),),
          Expanded(child: TradeButton(color: Colors.green, isTradeUp: true, onPressed: (){
            if(selectAmount < walletAmount){
              _onMarketChange.add(TradeModel(cond: 1,marketChange: true, tradeType: UpDownType.UP));
            }else{
              Fluttertoast.showToast(msg: 'your balance is not enough');
            }

          },)),
        ],),
      ),
    ],),);
}






