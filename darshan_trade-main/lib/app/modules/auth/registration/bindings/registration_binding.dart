import 'package:get/get.dart';

import '../controllers/registration_controller.dart';

class RegistrationBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<RegistrationController>(RegistrationController(apiRepository: Get.find()));

    // Get.lazyPut<RegistrationController>(
    //   () => RegistrationController(),
    // );
  }
}
