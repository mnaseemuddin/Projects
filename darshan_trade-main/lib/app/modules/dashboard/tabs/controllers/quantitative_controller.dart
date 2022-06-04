
import 'dart:async';

import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';

class QuantitativeController extends GetxController{
  final ApiRepository apiRepository;
  // StreamController<int> onPageChange = StreamController<int>.broadcast();
  StreamController<List<CurrencyModel>> streamController = StreamController<List<CurrencyModel>>.broadcast();
  var currencyList = Rxn<List<CurrencyModel>>();



  QuantitativeController({required this.apiRepository});

  @override
  void onInit() {
    super.onInit();
    print('QuantitativeController => working');
    getCurChangList();

    // Timer.periodic(Duration(seconds: 1), (timer) {
    //   getCurrencyChanged();
    // });
  }

  @override
  void onClose(){
    print('OnResume called');
  }

  void onResumed(){
    print('OnResume called');
  }

  void getCurrencyChanged()async{
    ApiResponse response = await getCurChngListAPI(2);
    if(response.status){
      if(response.data is List<CurrencyModel>){
        streamController.add(response.data);
      }
    }
  }

  Future<List<CurrencyModel>?> getCurChangList() async{
    // final resp = await apiRepository.getCurrency();
    ApiResponse response = await getCurChngListAPI(2);
    var val =  (response.data??currencyModelFromJson('[]')).where((element) => !(element.symbol.contains('UP')|| element.symbol.contains('DOWN'))).toList();
    currencyList.value =  val;//response.data??currencyModelFromJson('[]').where((element) => (element.symbol.contains('UP')|| element.symbol.contains('DOWN')));
    // print(resp?.first.symbol);
    // currencyList.value = resp??[];
    return currencyList.value;
  }


}