import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/circle_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/quantitative_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/robot_controller.dart';
import '../controllers/dashboard_controller.dart';

class DashboardBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<DashboardController>(() => DashboardController(),);
    Get.put<QuantitativeController>(QuantitativeController(apiRepository: Get.find()),);
    Get.put<CircleController>(CircleController(),);
    Get.put<RobotController>(RobotController(),);
  }
}
