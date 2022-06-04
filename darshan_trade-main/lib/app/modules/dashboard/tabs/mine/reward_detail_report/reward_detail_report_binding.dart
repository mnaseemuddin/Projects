import 'package:get/get.dart';

import 'reward_detail_report_controller.dart';

class RewardDetailReportBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<RewardDetailReportController>(() => RewardDetailReportController());
  }
}
