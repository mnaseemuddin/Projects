import 'package:get/get.dart';

import '../controllers/trade_settings_controller.dart';

class TradeSettingsBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<TradeSettingsController>(TradeSettingsController(),);
  }
}
