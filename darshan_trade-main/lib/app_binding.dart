import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/currency_controller.dart';
import 'package:royal_q/app_controller.dart';
import 'api/api_provider.dart';
import 'api/api_repository.dart';
import 'app/shared/sawidgets/controllers/app_text_controller.dart';
import 'app/shared/sawidgets/controllers/text_field_controller.dart';

class AppBinding extends Bindings {
  @override
  void dependencies() async {
    Get.put(AppController(), permanent: true);
    Get.put(ApiProvider(), permanent: true);
    Get.put(ApiRepository(apiProvider: Get.find()), permanent: true);
    Get.put(AppTextFieldController(), permanent: true);
    Get.put(TextFieldIconController(), permanent: true);
    Get.put(CurrencyController(), permanent: true);
    Get.put(DashboardController(), permanent: true);

  }
}
