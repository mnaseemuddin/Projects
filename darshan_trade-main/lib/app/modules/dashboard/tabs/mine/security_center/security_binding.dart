import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/security_center/security_center_controller.dart';

class SecurityBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<SecurityCenterController>(() => SecurityCenterController(),);
  }
}
