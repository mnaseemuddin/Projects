


import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/response/billing_details_by_coin_model.dart';

class BillingDetailsByCoinController extends GetxController{

  final billingResponse=RxList<BillingDetailsByCoinModel>();



  @override
  void onInit() {
    super.onInit();
    billing();
  }

  void billing() async{
  //  ApiResponse response=await coinByBillingAPI();
  //  billingResponse.value=response.data;
  }
}