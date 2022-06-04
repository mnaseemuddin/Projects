import 'package:get/get.dart';

import '../controllers/reward_details_controller.dart';

class RewardDetailsBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<RewardDetailsController>(
      () => RewardDetailsController(),
    );
  }
}
