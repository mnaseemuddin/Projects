import 'dart:async';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';

class CircleController extends GetxController{

  final tradingProfitResponse = Rxn<List<TradingProfitResponse>>();
  final listRunningRobot      = RxList<UserCurrencyResponse>();

  @override
  void onInit() {
    super.onInit();
    getTradeProfit();
    Timer.periodic(Duration(seconds: 1), (timer) {
      getRobotUserList();
    });
  }

  @override
  void onClose(){
    print('OnResume called');
  }

  void onResumed(){
    print('OnResume called');
  }

  void getTradeProfit() async{
    ApiResponse apiResponse = await tradingProfitAPI();
    // if(apiResponse.status){
      tradingProfitResponse.value = apiResponse.data;
    // }
  }

  void getRobotUserList()async{
      ApiResponse response = await getRobotUserListAPI(1);
      if(response.status) {
        if (response.data is List<UserCurrencyResponse>) {
          List<UserCurrencyResponse> list = response.data;
          listRunningRobot.value = list;
        }
      }
    }


}
