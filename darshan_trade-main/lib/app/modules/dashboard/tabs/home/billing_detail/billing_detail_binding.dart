import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/billing_detail/billing_detailby_coin_controller.dart';

import 'billing_detail_controller.dart';

class BillingDetailBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<BillingDetailController>(BillingDetailController());
    // Get.lazyPut<BillingDetailController>(() => BillingDetailController());
  }
}




class BillingDetailByCoinBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<BillingDetailsByCoinController>(BillingDetailsByCoinController());
    // Get.lazyPut<BillingDetailController>(() => BillingDetailController());
  }
}