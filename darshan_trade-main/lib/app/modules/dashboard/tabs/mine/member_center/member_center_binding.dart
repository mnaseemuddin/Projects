import 'package:get/get.dart';

import 'member_center_controller.dart';

class MemberCenterBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<MemberCenterController>(MemberCenterController());
  }
}
