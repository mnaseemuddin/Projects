import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/apis.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/currency_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/model/FBUSDCurrencyModel.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/model/UserFutureCurrencyResponse.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/trading_cell.dart';
import 'package:royal_q/app/modules/dashboard/tabs/quantitative/currency_details.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:transparent_image/transparent_image.dart';

import 'FCurrencyCell.dart';
import 'model/fCurrencyModel.dart';


class FutureTradeCell extends StatefulWidget {
  final List<FUSDTCurrencyModel> currencyModels;
  final int exchangeType;
  final Function()?onRefresh;
  const FutureTradeCell({Key? key, required this.currencyModels, this.onRefresh, required this.exchangeType}) : super(key: key);

  @override
  _FutureTradeCellCellState createState() => _FutureTradeCellCellState();
}

class _FutureTradeCellCellState extends State<FutureTradeCell> {
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final PageController _pageController = PageController();
  final CurrencyController _currencyController = Get.find<CurrencyController>();
  final DashboardController dashboardController=Get.find<DashboardController>();

  List<FUSDTCurrencyModel> _currencyModels = [];
  List<BoosterUserListResponse>? _boosterUserListResponse;
  int selectedPage=0;
  List<UserCurrencyResponse>? _currencyCircle=[];
  List<FUSDTCurrencyModel> _list = [];
   List<UserFutureCurrencyResponse>futureUserCurrencyList=[];

  @override
  void initState() {
    super.initState();
    _currencyModels = widget.currencyModels;
    _currencyCircle=_currencyController.listBinanceHome.value;
    _list=_currencyController.futureBUSDListBinance.value!;
    getBoosterUserListAPI().then((value){
      if(mounted) {
        setState(() {
          _boosterUserListResponse = value.data;
        });
      }
    });
    getUserFutureCurrencyChange();
  }

  void getUserFutureCurrencyChange()async{
    getFutureCurUserChngListAPI(exchangeValue).then((value){
      if(value.status){
        setState(() {
          List<UserFutureCurrencyResponse> list = value.data;
          futureUserCurrencyList = list;
        });
      }else{
        Fluttertoast.showToast(msg: value.message.toString());
      }
    });


  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(left: 16, right: 16),
      child: Column(children: [
        SizedBox(height: 16,),
        SASearch(
          onChange: (val){
            setState(() {
              if(selectedPage==1){
                _currencyCircle=_currencyController.listBinanceHome.value?.
                where((element) =>element.symbol.
                replaceAll("USDT", 'replace').toLowerCase().contains(val.toLowerCase())).toList();
              }else if(selectedPage==2){
                _currencyCircle=_currencyController.listBinanceHome.value?.where((element) =>element.symbol.
                replaceAll("USDT", 'replace').toLowerCase().contains(val.toLowerCase())).toList();
              }else if(selectedPage==3){
                _currencyCircle=_currencyController.listBinanceHome.value?.where((element) =>element.symbol.
                replaceAll("USDT", 'replace').toLowerCase().contains(val.toLowerCase())).toList();
              }else if(selectedPage==0) {
                _currencyModels = widget.currencyModels.where((element) =>
                    element.symbol.replaceAll('USDT', '').toLowerCase().contains(
                        val.
                        toLowerCase())).toList();
              }
            });
          },),
        SizedBox(height: 16,),
        Container(height: 40,
          alignment: Alignment.centerLeft,
          child: SATabview(titles: ['USDT'.tr, 'BUSD'.tr, 'Running'.tr],
            selectedColor: Colors.blueAccent, textColor: Colors.blueAccent, expand: true,
            onPageChange: _onPageChange, onSelect: (page){
              _pageController.animateToPage(page, duration: Duration(milliseconds: 200), curve: Curves.ease);
            }, isScroll: true,),),
        Container(height:1, color: ColorConstants.white10,),
        SizedBox(height: 16,),
        Expanded(child: PageView(
          controller: _pageController,
          onPageChanged: (page){
            setState(() {
              selectedPage=page;
              dashboardController.selectFutureTab(selectedPage);
              print(selectedPage);
            });
            _onPageChange.add(page);
          },
          physics: NeverScrollableScrollPhysics(),
          children: [
            ListView.builder(itemBuilder: (context, index){
              FUSDTCurrencyModel model = _currencyModels.elementAt(index);
              return FCurrencyCell(model: model,onPress: ()async{
                var chkHadgeMode=await storage.read("Hedge");
                if(chkHadgeMode!=null){
                  if(chkHadgeMode){
                    Get.toNamed(Routes.FTSETTING,
                        arguments: [{'model': model, 'exchangeType': exchangeValue}]);
                  }else {
                    checkHedgeModeActive(model);
                  }
                }else{
                  checkHedgeModeActive(model);
                }

              },);
            }, itemCount: _currencyModels.length,),

            _createBUSTMerketUI(),
              runningUI(),
            // _createFilterList((_currencyCircle??[]).where((element)
            // => element.marginlimit==0).toList()),

            _boosterUserListResponse!=null?_boosterUserListResponse!.isNotEmpty?
            ListView(children: _boosterUserListResponse!.map((model){
              String title = model.symbol.replaceAll('USDT', '');
              UserCurrencyResponse mModel = UserCurrencyResponse(symbol: model.symbol,
                  priceChange: model.priceChange, quantity: model.quantity,
                  price: model.price, profit: model.profit, returnprofit: model.returnprofit,
                  cyclemode: 1, marginlimit: 0, strategyMode: 0);
              return TradingCell(model: mModel, onPressed: (){
                FUSDTCurrencyModel currencyModel = FUSDTCurrencyModel(symbol: model.symbol,
                    priceChangePercent: '${model.priceChange}', lastPrice: '${model.price}',
                    lastQty: '${model.quantity}');
                Get.toNamed(Routes.TRADE_SETTINGS, arguments:
                [{'model': currencyModel, 'exchangeType': exchangeValue}]);
              },padding: 0,);
              /**
                  return GestureDetector(
                  child: Container(
                  margin: EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  padding: EdgeInsets.all(16),
                  child: Column(children: [
                  Row(children: [
                  FadeInImage.memoryNetwork(
                  placeholder: kTransparentImage,
                  image: 'https://xpertgain.io/symbol/${model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
                  width: 24, height: 24,
                  imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
                  ),

                  SizedBox(width: 8,),
                  Expanded(child: Text(title, style: textStyleHeading2(),)),

                  ],),
                  Divider(color: ColorConstants.white24,),
                  Row(children: [
                  Expanded(child: Text('Price', style: textStyleLabel()),),
                  Text(model.price.toStringAsFixed(5), style: textStyleLabel(),),
                  Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                  ],),

                  Row(children: [
                  Expanded(child: Text('Quantity', style: textStyleLabel()),),
                  Text(model.quantity.toStringAsFixed(5), style: textStyleLabel()),
                  Text(' $title', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                  ],),

                  Row(children: [
                  Expanded(child: Text('Price change', style: textStyleLabel()),),
                  Text(model.priceChange.toStringAsFixed(4), style: textStyleLabel()),
                  Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                  ],),

                  Row(children: [
                  Expanded(child: Text('Quantity', style: textStyleLabel()),),
                  Text(model.quantity.toStringAsFixed(4), style: textStyleLabel()),
                  Text(' $title', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                  ],),

                  Row(children: [
                  Expanded(child: Text('Profit', style: textStyleLabel()),),
                  Text(model.price.toStringAsFixed(4), style: textStyleLabel()),
                  Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                  ],),

                  Row(children: [
                  Expanded(child: Text('Change', style: textStyleLabel()),),
                  Text(model.returnprofit.toStringAsFixed(5), style: textStyleLabel()),
                  ],),
                  ],),
                  decoration: textBgWhite,
                  ),
                  onTap: (){
                  CurrencyModel mModel = CurrencyModel(symbol: model.symbol, priceChangePercent: '${model.priceChange}', lastPrice: '${model.price}', lastQty: '${model.quantity}', strategyMode: 0);
                  Get.toNamed(Routes.GET_BOOSTER, arguments: [{'model': mModel}]);
                  // controller.setBooster(model);
                  // _onPageChange.add(0);
                  // _pageController.animateToPage(0, duration: Duration(milliseconds: 200), curve: Curves.ease);
                  },
                  );
               **/
            }).toList(),)
                :NoRecord()
                :Center(child: CircularProgressIndicator(),)

          ],
        ))
      ],),
    );
  }

  Widget _createFilterList(List<UserCurrencyResponse> list){
    return list.isNotEmpty?ListView.builder(itemBuilder: (context, index){
      UserCurrencyResponse model = list.elementAt(index);
      return TradingCell(model: model, onPressed: (){
        CurrencyModel currencyModel = CurrencyModel(symbol: model.symbol, priceChangePercent:
        '${model.priceChange}', lastPrice: '${model.price}', lastQty: '${model.quantity}',
            strategyMode: model.strategyMode);
        Get.toNamed(Routes.TRADE_SETTINGS, arguments: [{'model': currencyModel,
          'exchangeType': exchangeValue}]);
      },padding: 0,);
    }, itemCount: list.length,):NoRecord();
  }


  Future<void> _pullRefresh() async {
    widget.onRefresh!();
  }

  _createBUSTMerketUI() =>_list.isNotEmpty?ListView.builder(itemBuilder: (context, index){
    FUSDTCurrencyModel model = _list.elementAt(index);
    String title = model.symbol.replaceAll('BUSD', '');
    return GestureDetector(
      child: Container(
        padding: EdgeInsets.all(16),
        margin: EdgeInsets.only(bottom: 8),

        child: Column(children: [

          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // FadeInImage.memoryNetwork(
              //   placeholder: kTransparentImage,
              //   imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon.png', width: 40, height: 40,),
              //   image: 'http://darshantrade.com/symbol/'
              //       '${widget.model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
              //   width: 40, height: 40,
              // ),

              SizedBox(
                  width: 40, height: 40,
                  child: CircleAvatar(
                    backgroundColor: Colors.grey,
                    backgroundImage: NetworkImage(
                        'http://darshantrade.com/symbol/${model.symbol.replaceAll('BUSD', '').toLowerCase()}@2x.png'),)),

              SizedBox(width: 8,),
              Text(title, style: textStyleHeading3(color: ColorConstants.white),),
              Text('/BUSD', style: textStyleLabel(color: ColorConstants.white54),),
              SizedBox(width: 8,),
              /* Container(
                padding: EdgeInsets.all(4),
                child: Text('Cycle'.tr, style: textStyleLabel(color: ColorConstants.blue),),
                decoration: BoxDecoration(
                    color: ColorConstants.blue.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(2)
                ),
              ),*/
              Expanded(child: Container()),
              Container(
                child: Text('0.00%', style: textStyleLabel(color: ColorConstants.white, fontSize: 14),),
                padding: EdgeInsets.all(8),
                alignment: Alignment.center,
                decoration: BoxDecoration(
                    color: ColorConstants.grey,
                    borderRadius: BorderRadius.circular(8)
                ),
              )
            ],),
          Divider(color: ColorConstants.white24,height: 32,),

          Row(children: [
            Text('Quantity'.tr, style: textStyleLabel(color: ColorConstants.white70),),
            SizedBox(width: 4,),
            Text(double.parse(model.lastQty).toStringAsFixed(3), style: textStyleLabel(color: ColorConstants.white70),),
            SizedBox(width: 16,),
            // Text('Floating profit\t\t+${widget.model.lastPrice}', style: textStyleLabel(color: isPlatformIOS?Colors.black54:Colors.white70),),
          ],),
          SizedBox(height: 8,),
          Row(children: [
            Text('Price'.tr, style: textStyleLabel(color: ColorConstants.white70),),
            Text(double.parse(model.lastPrice).toStringAsFixed(3), style: textStyleLabel(color: ColorConstants.white70),),
            SizedBox(width: 16,),
            Text('Increase'.tr, style: textStyleLabel(color: ColorConstants.white70),),
            Text('${model.priceChangePercent}%', style:
            textStyleLabel(color: double.parse(model.priceChangePercent)>0?
            ColorConstants.green:ColorConstants.redAccent),),

          ],)

        ],),

        decoration: BoxDecoration(
            color: ColorConstants.whiteRev,
            borderRadius: BorderRadius.circular(8)
        ),
      ),
      onTap: (){
        Get.toNamed(Routes.FTSETTING,
            arguments: [{'model': model, 'exchangeType': exchangeValue}]);
      },
    );
  }, itemCount: _list.length,):NoRecord();

  void checkHedgeModeActive(FUSDTCurrencyModel model) async{
      ApiResponse response=await checkIPPermissionAPI();
      if(response.status){
        await storage.write("Hedge", true);
        Get.toNamed(Routes.FTSETTING,
            arguments: [{'model': model, 'exchangeType': exchangeValue}]);
      }else{
        openHedgeModeDialog(model);
      }
  }
  void openHedgeModeDialog(FUSDTCurrencyModel model) {
    Get.dialog(
      AlertDialog(
        title: const Text('Confirm'),
        content: const Text('Are you sure to active Hedge Mode?'),
        actions: [
          TextButton(
            child: const Text("Cancel",style: TextStyle(color: Colors.orange),),
            onPressed: () => Get.back(),
          ),
          TextButton(
            child: const Text("Confirm",style: TextStyle(color: Colors.orange)),
            onPressed: ()async{
              var hashMap={
                "id":userInfo!.id, "exchangetype":exchangeValue
              };
              ApiResponse response=await grantedIPPermissionAPI(hashMap);
              if(response.status){
                Get.back();
                await storage.write("Hedge", true);
                Get.toNamed(Routes.FTSETTING,
                    arguments: [{'model': model, 'exchangeType': exchangeValue}]);
              }else{
                Fluttertoast.showToast(msg: response.message.toString());
              }
            },
          ),
        ],
      ),
    );
  }

  runningUI() =>futureUserCurrencyList.isEmpty?NoRecord():ListView.builder(itemBuilder: (context, index){
    UserFutureCurrencyResponse model = futureUserCurrencyList.elementAt(index);
    double returnProfit=double.parse(model.returnprofit);
      double priceChange=double.parse(model.priceChange);
      double quantity=double.parse(model.quantity);
    return SACellContainerGradient(
      child: GestureDetector(
        onTap: (){
          FUSDTCurrencyModel currencyModel = FUSDTCurrencyModel(symbol: model.symbol, priceChangePercent:
          '${model.priceChange}', lastPrice: '${model.price}', lastQty: '${model.quantity}');
          Get.toNamed(Routes.FTSETTING, arguments: [{'model': currencyModel,
            'exchangeType': exchangeValue}]);
        },
        child: Column(children: [
          Row(children: [
            ClipRRect(
              borderRadius: BorderRadius.all(Radius.circular(24)),
              child: Image.network('http://darshantrade.com/symbol/${model.symbol.
              replaceAll('USDT', '').toLowerCase()}@2x.png', width: 40, height: 40,
                errorBuilder: (context, url, error) => Image.asset('assets/images/icon.png', width: 40, height: 40,),),
            ),
            SizedBox(width: 8,),
            Text(model.symbol.replaceAll('USDT', ''), style: textStyleHeading2(color: ColorConstants.black),),
            Text('/USDT', style: textStyleLabel(color: ColorConstants.black),),
            SizedBox(width: 16,),
            // Chip(label: Text('Cycle'), backgroundColor: Colors.white10,),
            Expanded(child: SizedBox(),),
            Container(
              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
              alignment: Alignment.center,
              child: Text('${returnProfit.toString()}%', style: textStyleHeading2(),),
              decoration: BoxDecoration(
                  color: returnProfit>0.0?Colors.green:Colors.red,
                  borderRadius: BorderRadius.circular(8)
              ),
            )
          ],),
          Divider(),
          Row(children: [
            Expanded(child: Text('Quantity_:'.tr, style: textStyleLabel(
                color: ColorConstants.black),), flex: 20,),
            Expanded(child: Text('${quantity.toStringAsFixed(4)}', style:
            textStyleLabel(color: ColorConstants.black)), flex: 30,),
            Expanded(child: Text('Profit_:'.tr, style:
            textStyleLabel(color: ColorConstants.black)), flex: 20,),
            Expanded(child: Text('${model.profit.toStringAsFixed(4)}', style:
            textStyleHeading3(color:model.profit>0.0?Colors.green:Colors.red),),flex: 30,)
          ],),

          SizedBox(height: 8,),

          Row(children: [
            Expanded(child: Text('Price_:'.tr, style: textStyleLabel(color: ColorConstants.black)), flex: 20,),
            Expanded(child: Text('${model.price}', style: textStyleLabel(color: ColorConstants.black)), flex: 30,),
            Expanded(child: Text('Change_:'.tr, style: textStyleLabel(color: ColorConstants.black)),
              flex: 20,), Expanded(child: Text('${priceChange.toString()}%', style: textStyleHeading3(
                color: priceChange>0.0?Colors.green:Colors.red),),flex: 30,)
          ],),
        ],),
      ),
    );
  }, itemCount: futureUserCurrencyList.length,);
}



// Feather.trending_up



//[{"symbol":"DENTUSDT","priceChange":17.5170,"quantity":278448.00000000,
// "price":0.00345500,"profit":-37.96,"returnprofit":-3.7872,"cyclemode":1},
// {"symbol":"EOSUSDT","priceChange":19.9450,"quantity":316.40000000,"price":3.06100000,
// "profit":-31.32,"returnprofit":-3.1329,"cyclemode":1},{"symbol":"XLMUSDT","priceChange":6.1480,
// "quantity":4253.00000000,"price":0.23310000,"profit":-8.51,"returnprofit":-0.8507,"cyclemode":1},
// {"symbol":"FTMUSDT","priceChange":5.1670,"quantity":684.00000000,"price":1.45730000,"profit":-3.21,
// "returnprofit":-0.3147,"cyclemode":1},{"symbol":"GALAUSDT","priceChange":6.4180,
// "quantity":3665.00000000,"price":0.26926000,"profit":-12.94,"returnprofit":-1.2940,"cyclemode":1},
// {"symbol":"MANAUSDT","priceChange":4.5720,"quantity":361.00000000,"price":2.74490000,
// "profit":-6.71,"returnprofit":-0.6731,"cyclemode":1},{"symbol":"GRTUSDT","priceChange":12.6220,
// "quantity":2118.00000000,"price":0.47200000,"profit":0.07,"returnprofit":0.0212,"cyclemode":1},{
// "symbol":"SHIBUSDT","priceChange":7.3860,"quantity":37257824.00000000,"price":0.00002646,
// "profit":-14.16,"returnprofit":-1.4158,"cyclemode":1},{"symbol":"ADAUSDT","priceChange":3.8530,
// "quantity":836.10000000,"price":1.18600000,"profit":-8.37,"returnprofit":-0.8361,"cyclemode":1},
// {"symbol":"SLPUSDT","priceChange":3.9410,"quantity":46082.00000000,"price":0.02110000,
// "profit":-27.65,"returnprofit":-2.7650,"cyclemode":1},{"symbol":"IOTAUSDT","priceChange":5.1400,
// "quantity":1105.00000000,"price":0.89590000,"profit":-9.65,"returnprofit":-0.9508,"cyclemode":1},
// {"symbol":"ENJUSDT","priceChange":8.5820,"quantity":546.10000000,"price":1.82200000,"profit":-4.92,
// "returnprofit":-0.4915,"cyclemode":1},{"symbol":"TRXUSDT","priceChange":6.1520,
// "quantity":14096.40000000,"price":0.07092000,"profit":-0.28,"returnprofit":-0.0282,"cyclemode":1},
// {"symbol":"SCUSDT","priceChange":4.0170,"quantity":80645.00000000,"price":0.01217000,
// "profit":-18.55,"returnprofit":-1.8548,"cyclemode":1},{"symbol":"ETHUSDT",
// "priceChange":5.4130,"quantity":0.30000000,"price":3324.07000000,"profit":-2.68,
// "returnprofit":-0.2679,"cyclemode":1}]

