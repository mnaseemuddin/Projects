import 'dart:async';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/model/UserFutureCurrencyResponse.dart';

import '../../../../routes/app_pages.dart';
import '../future/model/FBUSDCurrencyModel.dart';
import '../future/model/fCurrencyModel.dart';

class CurrencyController extends GetxController{

  final listBinance         = Rxn<List<CurrencyModel>>();
  final listBinanceHome     = Rxn<List<UserCurrencyResponse>>();
  final listCoinxPlus       = Rxn<List<CurrencyModel>>();
  final listCoinxPlusHome   = Rxn<List<UserCurrencyResponse>>();

  final listCurrencySearch = Rxn<List<CurrencyModel>>();

  final isListBinanceHome     = false.obs;
  final isListCoinxPlusHome     = false.obs;

  Timer? _timer;
  // Timer? _timerHome;
  final exchangetype = exchangeValue.obs;
  final futureUSDTListBinance=Rxn<List<FUSDTCurrencyModel>>();
  final futureBUSDListBinance=Rxn<List<FUSDTCurrencyModel>>();

  final fListCoinxPlus   = Rxn<List<FUSDTCurrencyModel>>();


  @override
  void onInit() {
    super.onInit();
    print('QuantitativeController => working');
    getCurrencyChanged();
    getFUSDTCurrencyChanged();
    getFBUSDCurrencyChanged();
    _timer = Timer.periodic(Duration(seconds: 1), (timer) {
      getCurrencyChanged();
      getFUSDTCurrencyChanged();
      getFBUSDCurrencyChanged();
      getCurrencyChangedUser();
    });

    // _timerHome = Timer.periodic(Duration(seconds: 1), (timer) {
    //   getCurrencyChangedUser();
    // });
  }

  @override
  void onClose(){
    print('OnResume called');
    // _timer?.cancel();
    // _timerHome?.cancel();
  }

  void onResumed(){
    print('OnResume called');
  }

  final List<String>_filterList = [
    'BTCUSDT',  'ETHUSDT', 'TRXUSDT', 'XRPUSDT',   'ALGOUSDT',
    'AAVEUSDT', 'ADAUSDT', 'ATMUSDT', 'BLZUSDT',   'DOTUSDT',
    'GNOUSDT',  'ICXUSDT', 'OMGUSDT', 'STRATUSDT', 'STXUSDT',
  ];

  void getCurrencyChanged()async{

    if(exchangetype.value==1){
      ApiResponse response = await getCurChngListAPI(exchangeValue);
      if(response.status) {
        isListBinanceHome.value = true;
        if (response.data is List<CurrencyModel>) {
          List<CurrencyModel> list = response.data;
            listBinance.value = list;
            // listBinanceHome.value = listBinance.value!.where((element) =>
            //     _filterList.contains(element.symbol)).toList();
        }
      }
    }else{
      ApiResponse response = await getCurChngListAPI(exchangetype.value);
      if(response.status) {
        isListCoinxPlusHome.value = true;
        if (response.data is List<CurrencyModel>) {
          List<CurrencyModel> list = response.data;
            listCoinxPlus.value = list;
        }
      }
    }
  }

  void getFBUSDCurrencyChanged()async{

    if(exchangetype.value==1){
      ApiResponse response = await getFUSDTCurChngListAPI(exchangeValue);
      if(response.status) {
        isListBinanceHome.value = true;

        if (response.data is List<FUSDTCurrencyModel>) {
          List<FUSDTCurrencyModel> list=response.data;
          futureBUSDListBinance.value=list;
          futureBUSDListBinance.value?.removeWhere((element) =>
          element.symbol.substring(element.symbol.length - 4)=="USDT");
          // for (FCurrencyModel element in list) {
          //   var newString = element.symbol.substring(element.symbol.length - 4);
          //   if(newString=="BUSD"){
          //     print(element.symbol);
          //   }else{
          //     FCurrencyModel list=element;
          //     futureUSDTListBinance.value=list;
          //   }
          // }


          // listBinanceHome.value = listBinance.value!.where((element) =>
          //     _filterList.contains(element.symbol)).toList();
        }
      }
    }else{
      ApiResponse response = await getCurChngListAPI(exchangetype.value);
      if(response.status) {
        isListCoinxPlusHome.value = true;
        if (response.data is List<FUSDTCurrencyModel>) {
          List<FUSDTCurrencyModel> list = response.data;
          fListCoinxPlus.value = list;
        }
      }
    }
  }

  void getFUSDTCurrencyChanged()async{

    if(exchangetype.value==1){
      ApiResponse response = await getFUSDTCurChngListAPI(exchangeValue);
      if(response.status) {
        isListBinanceHome.value = true;

        if (response.data is List<FUSDTCurrencyModel>) {
          List<FUSDTCurrencyModel> list=response.data;
            futureUSDTListBinance.value=list;
              futureUSDTListBinance.value?.removeWhere((element) =>
                  element.symbol.substring(element.symbol.length - 4)=="BUSD");
          // for (FCurrencyModel element in list) {
          //   var newString = element.symbol.substring(element.symbol.length - 4);
          //   if(newString=="BUSD"){
          //     print(element.symbol);
          //   }else{
          //     FCurrencyModel list=element;
          //     futureUSDTListBinance.value=list;
          //   }
          // }


          // listBinanceHome.value = listBinance.value!.where((element) =>
          //     _filterList.contains(element.symbol)).toList();
        }
      }
    }else{
      ApiResponse response = await getCurChngListAPI(exchangetype.value);
      if(response.status) {
        isListCoinxPlusHome.value = true;
        if (response.data is List<FUSDTCurrencyModel>) {
          List<FUSDTCurrencyModel> list = response.data;
          fListCoinxPlus.value = list;
        }
      }
    }
  }



  void getCurrencyChangedUser()async{

    if(exchangetype.value==1){
      ApiResponse response = await getCurUserChngListAPI(exchangetype.value);
      if(response.status) {
        if (response.data is List<UserCurrencyResponse>) {
          List<UserCurrencyResponse> list = response.data;
          listBinanceHome.value = list;//listBinance.value!.where((element) =>
              // _filterList.contains(element.symbol)).toList();
        }
      }
    }else{
      ApiResponse response = await getCurUserChngListAPI(exchangetype.value);
      if(response.status) {
        if (response.data is List<UserCurrencyResponse>) {
          List<UserCurrencyResponse> list = response.data;
          listCoinxPlusHome.value = list;
        }
      }
    }
  }
}
