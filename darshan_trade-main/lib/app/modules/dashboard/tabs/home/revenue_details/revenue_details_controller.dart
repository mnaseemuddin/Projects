import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';

class RevenueDetailsController extends GetxController {

  final RxList<RevenueResponse> revenueResponse = <RevenueResponse>[].obs;
  final totalProfit = 0.0.obs;
  final todayTotalProfit = 0.0.obs;

  @override
  void onInit(){
    super.onInit();
    getRevenueList();
  }
  
  final count = 0.obs;
  increment() => count.value++;

  void getRevenueList() async{
    EasyLoading.show();
    ApiResponse response = await revnueListAPI();
    if(response.status){
      revenueResponse.value = response.data;
      for (var element in revenueResponse) {
        // element.transdate
        if(isToday(element.transdate)){
          todayTotalProfit.value += element.credit;
        }
        totalProfit.value += element.credit;
      }
    }
    EasyLoading.dismiss();
  }

  bool isToday(time){
    final now = DateTime.now();
    DateFormat dateFormat = DateFormat("MM-dd-yyyy");
    DateTime dateToCheck = dateFormat.parse(time);
    final today = DateTime(now.year, now.month, now.day);
    final aDate = DateTime(dateToCheck.year, dateToCheck.month, dateToCheck.day);
    return aDate == today;
  }
}
