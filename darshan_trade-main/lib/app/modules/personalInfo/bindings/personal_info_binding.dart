import 'package:get/get.dart';

import '../controllers/personal_info_controller.dart';

class PersonalInfoBinding extends Bindings {
  @override
  void dependencies() {
    // Get.lazyPut<PersonalInfoController>(
    //   () => PersonalInfoController(),
    // );
    Get.put<PersonalInfoController>(PersonalInfoController(apiRepository: Get.find()));
  }
}
