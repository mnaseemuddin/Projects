import 'package:get/get.dart';

import 'get_booster_controller.dart';

class GetBoosterBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<GetBoosterController>(GetBoosterController());
  }
}
