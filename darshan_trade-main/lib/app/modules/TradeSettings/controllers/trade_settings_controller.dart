import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/response/trade_response.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';

class TradeSettingsController extends GetxController {
  //TODO: Implement TradeSettingsController

  final topPosition = 0.0.obs;
  final ScrollController scrollController = ScrollController();

  final count = 0.obs;
  final switchValue = false.obs;
  final exchangeType = exchangeValue.obs;
  final validAmount = true.obs;
  final buttonActive = true.obs;

  final Rx<TradeResponse> tradeResponse = TradeResponse(marginList: [MarginListItem(id: 1, calls: 10.0,
      times: 64, iscall: 0)], id: 1, firstBuy: 15.0, openPosition: 0,
      maginCallLimit: 0, wholePRatio: 1.20, wholePCallback: 0.3,
    buyCallback: 0.50,presetBuyprice: 0.0, stopCloseprice: 0.0,selllosspositin: 0.0,lossCallBackTrigger: 0.0,
      status: 'status', symbol: 'INCHUSDT', exchangeType: exchangeValue,excessUpTrigger: 0.0).obs;
  List<MarginListItem> marginList = <MarginListItem>[].obs;

  final Rx<TradeResponse> defaultTradeResponse = TradeResponse(id: 1, firstBuy: 15.0, openPosition: 0,
    maginCallLimit: 0, wholePRatio: 1.20, wholePCallback: 0.30, buyCallback: 0.50,excessUpTrigger: 0.0,
     status: 'succeed', presetBuyprice: 0.0, stopCloseprice: 0.0,selllosspositin: 0.0,lossCallBackTrigger: 0.0,
    marginList: [
      MarginListItem(id: 1, calls: 3.50, times: 20, iscall: 0),
      MarginListItem(id: 2, calls: 4.0,  times: 40, iscall: 0),
      MarginListItem(id: 3, calls: 4.50, times: 80, iscall: 0),
      MarginListItem(id: 4, calls: 5.20, times: 160, iscall: 0),
      MarginListItem(id: 5, calls: 8.0,  times: 320, iscall: 0),
      MarginListItem(id: 6, calls: 10.0, times: 640, iscall: 0),
      MarginListItem(id: 7, calls: 12.0, times: 1280, iscall: 0),
    ], symbol: 'INCHUSDT', exchangeType: exchangeValue, ).obs;

  final dynamic argumentData = Get.arguments;
  final Rx<CurrencyModel> model = CurrencyModel(symbol: "1INCHDOWNUSDT",
      priceChangePercent: "0.0",
      lastPrice: "0.0",
      lastQty: "0.0", strategyMode: 0).obs;

  final tradeStatusResponse = TradeStatusResponse(sell: 0, buy: 0, cycleShotMode: 0, strategyMode: 0,
      trademode: 0, marginLimit: 0, status: 'succeed').obs;
  final curOneChnangeResponse = CurOneChngListResponse(pair: 'XEC', positionamt: 0.0, avgprice: 0.0, margincall: 0.0, positionquantity: 0.0, currentprice: 0.0, returnrate: 0.0).obs;

  final getBuyModeResponse = GetBuyModeResponse(id: 1, positionamt: 0.0, avgprice: 0.0, positionquantity: 0.0, currentprice: 0.0, positionprofitloss: 0.0, estimatedavgprice: 0.0, estimatedholdprofitloss: 0.0, status: 'succeed').obs;
  final getSellModeResponse = GetSellModeResponse(id: 1, positionamt: 0.0, avgprice: 0.0, positionquantity: 0.0, currentprice: 0.0, positionprofitloss: 0.0, remainpositionamt: 0.0, status: 'succeed').obs;

  // final TextEditingController triggerPriceController   = TextEditingController();
  // final TextEditingController marketPriceController    = TextEditingController();
  final TextEditingController purchaseAmountController = TextEditingController();

  @override
  void onInit() {
    super.onInit();
    model.value         = argumentData[0]['model'];
    exchangeType.value  = exchangeValue;//argumentData[0]['exchangeType'];
    getTradeValues();
    getTradeModeStatus();
    curOneChange();
    scrollController.addListener(() {
      topPosition.value = -scrollController.offset/2;
    });
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {}

  void getDefaultTradeValues()async{
    ApiResponse response = await getTradeAPI(defUser: true);
    print(tradeResponse.value.status);
    if(response.status){
      defaultTradeResponse.value = response.data;
      defaultTradeResponse.value.id = userInfo!.id;
      defaultTradeResponse.value.exchangeType = exchangeValue;
      defaultTradeResponse.value.symbol = model.value.symbol;
      // defaultTradeResponse.value.marginList.sort((a, b) => a.id>b.id?1:-1);
    }
  }

  void getTradeValues({defUser = false})async{
    EasyLoading.show();
    ApiResponse response = await getTradeAPI(defUser: defUser, symbol: model.value.symbol,);
    EasyLoading.dismiss();
    print(tradeResponse.value.status);
    if(response.status){
      tradeResponse.value = response.data;
      tradeResponse.value.exchangeType = exchangeValue;
      tradeResponse.value.symbol = model.value.symbol;
      switchValue.value = tradeResponse.value.openPosition==1;
      marginList = [];
      for (var element in tradeResponse.value.marginList) {
        marginList.add(MarginListItem(id: element.id, calls: element.calls, times: element.times, iscall: element.iscall));
      }
      print('marginList.length => ${marginList.length}');
    }
  }

  Future<bool> saveTradeValue() async{

    if(tradeResponse.value.firstBuy< defaultTradeResponse.value.firstBuy){
      EasyLoading.showToast('First buy amount should not less than ${
          defaultTradeResponse.value.firstBuy}');
      return false;
    }

    EasyLoading.show();

    if(tradeResponse.value.marginList.isEmpty){
   //   tradeResponse.value = defaultTradeResponse.value;
      tradeResponse.value.id = userInfo!.id;
      tradeResponse.value.marginList = marginList;
    }else{
      tradeResponse.value.id = userInfo!.id;
      tradeResponse.value.marginList = marginList;
    }


    // tradeResponse.value.symbol = model.value.symbol;
    // tradeResponse.value.exchangeType = exchangeType.value;
    //
    // defaultTradeResponse.value.symbol = model.value.symbol;
    // defaultTradeResponse.value.exchangeType = exchangeType.value;

    //Old Code
    // ApiResponse response = await updateTradeSettingAPI(tradeResponse.value.
    // marginList.isNotEmpty?tradeResponse.value.toJson()
    //     :defaultTradeResponse.value.toJson());

    //New Code
    ApiResponse response = await updateTradeSettingAPI(
        tradeResponse.value.toJson());

    EasyLoading.showToast(response.status?'Saved':response.data['message']);
    return response.status;
  }

  void startTrading(isStart)async{
    if(userInfo!.ispaid ==0){
      EasyLoading.showToast('Please activate the robot first');
      return;
    }

    buttonActive.value = false;
    EasyLoading.show();

    ApiResponse respSecret = await getApiSecretKeyAPI({
      'id': userInfo!.id,
      'exchanetype': exchangeValue
    });
    if(respSecret.status){
      ApiResponse response = await chkBalanceAPI();
      if(response.status){
        double balance = double.parse(response.data);
        print('Balance => $balance');
        if(balance>=tradeResponse.value.firstBuy){
          ApiResponse settingResponse = await updateTradeSettingAPI(tradeResponse.value.toJson());
          if(settingResponse.status){
            Map map = {
              'id': userInfo?.id,
              'Symbol': model.value.symbol,
              'ExchangeType': exchangeValue,
            };
            print(map.toString());
            if(model.value.symbol==null || exchangeType.value==0){
              EasyLoading.showToast('${model.value.symbol} && $exchangeValue');
              return;
            }
            ApiResponse bindSpotResponse =  await bindSpotTradeAPI({
              'id': userInfo?.id,
              'Symbol': model.value.symbol,
              'Exchangetype': exchangeValue,
              'Tradeamount': tradeResponse.value.firstBuy
            });
            if(bindSpotResponse.status) {
              EasyLoading.showToast(isStart?'Trade started':'Trading paused');
              Get.back();
            } else {
              EasyLoading.dismiss();
              EasyLoading.showToast(bindSpotResponse.data!=null?bindSpotResponse.data['message']:'Something went wrong');
            }
          }
        }else{
          EasyLoading.showToast('You have insufficient balance');
        }
      }
    }else{
      EasyLoading.showToast('API not bounded');
    }
    EasyLoading.dismiss();
    buttonActive.value = true;
  }

  void getTradeModeStatus()async{
    ApiResponse response = await getTradeModeStatusAPI({'id': userInfo!.id,
      'symbol': model.value.symbol, 'ExchangeType': exchangeValue});
    if(response.status){
      tradeStatusResponse.value = response.data;
    }
  }

  void updateModesConfirm(context, path, status, title)async{
    showDialog(context: context, builder: (context) => Dialog(
      backgroundColor: ColorConstants.whiteRev,
      child: Column(mainAxisSize: MainAxisSize.min,children: [
        TextButton.icon(onPressed: (){}, icon: Icon(Icons.info), label: Text('Confirm')),
        Padding(padding: EdgeInsets.symmetric(vertical: 16),
        child: Text('Are you sure want to ${status==0?'Start': 'Stop'} $title',
          style: textStyleLabel(color: ColorConstants.black),),),
        Divider(color: Colors.black12,),
        Row(
          children: [
            Expanded(child: SizedBox()),
            TextButton(onPressed: () => Get.back(), child: Text('Cancel')),
            TextButton(onPressed: (){
              Get.back();
              updateModes(path, status);
            }, child: Text('Confirm')),
          ],
        ),
      ],),
    ));
  }

  void updateModes(path, status)async{

    EasyLoading.show();
    ApiResponse response = await commonPostAPI(path, {
      'id': userInfo!.id,
      'Active': status,
      'Symbol': model.value.symbol,
      'ExchangeType': exchangeValue,
    });
    EasyLoading.showToast(response.data['message']);
    EasyLoading.dismiss();
    getTradeModeStatus();
  }

  void curOneChange()async{
    ApiResponse apiResponse = await getCurOneChngListAPI(model.value.symbol, exchangeType.value);
    if(apiResponse.status){
      curOneChnangeResponse.value = apiResponse.data;
    }else{

    }
  }

  void getBuyMode(amount) async{
    ApiResponse apiResponse = await getBuyModeAPI(model.value.symbol, exchangeValue, amount);
    if(apiResponse.status){
      getBuyModeResponse.value = apiResponse.data;
    }
  }

  void getSellMode()async{
    ApiResponse apiResponse = await getSellModeAPI(model.value.symbol, exchangeType.value);
    if(apiResponse.status){
      getSellModeResponse.value = apiResponse.data;
    }
  }

  void updateBuySellMode({required bool isBuy, required double amount})async{
    if(amount<=0){
      EasyLoading.showToast('Enter correct amount');
      return;
    }
    EasyLoading.show();
    Map map = {
      'id': userInfo!.id,
      'Symbol': model.value.symbol,
      'ExchangeType': exchangeValue,
      'amt': amount
    };

    ApiResponse apiResponse = await updateBuySellModeAPI(isBuy?'UpdateBuyMode':'UpdateSellMode', map);
    if(apiResponse.status){
      Get.back();
    }
    EasyLoading.showToast(apiResponse.data['message'], );
  }

  void binanceQunatityPercent(percent) async{
    EasyLoading.show();
    ApiResponse apiResponse = await binanceQunatityPercentAPI(model.value.symbol, exchangeType.value, percent);
    if(apiResponse.status){
      purchaseAmountController.text = apiResponse.data;
    }
    EasyLoading.dismiss();
  }
  //
  // int id
  // string Symbol
  // int ExchangeType
  void updateClearMode(symbol) async{
    EasyLoading.show();
    ApiResponse response = await updateClearModeAPI({
      'id': userInfo!.id,
      'Symbol': '${symbol}USDT',
      'ExchangeType': exchangeValue
    });
    if(response.status){
      EasyLoading.showToast(response.data['message']);
      Get.back();
    }
    EasyLoading.dismiss();
  }

  void updatePositionModestatus(active) async{
    EasyLoading.show();
    ApiResponse apiResponse = await updatePositionModestatusAPI({
      'id': userInfo!.id,
      'Active': active?1:0,
      'Symbol': model.value.symbol,
      'ExchangeType': exchangeValue
    });
    if(apiResponse.status){
      switchValue.value = active;
      tradeResponse.value.openPosition = active?1:0;
      getTradeValues();
    }
    EasyLoading.showToast(apiResponse.data['message']);
  }
}
