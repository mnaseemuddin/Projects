import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';

class RewardDetailsController extends GetxController {
  //TODO: Implement RewardDetailsController

  final count = 0.obs;
  final RxList<RewardResponse> rewardResponse = <RewardResponse>[].obs;
  final totalBalance = 0.0.obs;

  @override
  void onInit() {
    super.onInit();
    getRewardList();
  }

  @override
  void onReady() {
    super.onReady();
  }

  void getRewardList() async{
    EasyLoading.show();
    ApiResponse response = await rewardListAPI();
    if(response.status){
      rewardResponse.value = response.data;
      for (var element in rewardResponse) {
        totalBalance.value += element.credit;
      }
    }
    EasyLoading.dismiss();
  }

  @override
  void onClose() {}
  void increment() => count.value++;
}
