



import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/ftrade_setting_controller.dart';

class FTradeSettingsBinding extends Bindings{
  @override
  void dependencies() {
    Get.lazyPut<FTradeSettingsController>(() => FTradeSettingsController(),);
  }
}