import 'package:get/get.dart';

import 'community_controller.dart';

class CommunityBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<CommunityController>(CommunityController());
    // Get.lazyPut<CommunityController>(() => CommunityController());
  }
}
