import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';

class WithdrawalHistoryController extends GetxController {

  final withdrawalHistoryResponse = RxList<WithdrawalHistoryResponse>();

  @override
  void onInit(){
   super.onInit();
   getwithdrwallist();
  }
  
  final count = 0.obs;
  increment() => count.value++;

  void getwithdrwallist() async{
    EasyLoading.show();
    ApiResponse response = await getwithdrwallistAPI();
    withdrawalHistoryResponse.value = response.data;
    EasyLoading.dismiss();
  }

}
