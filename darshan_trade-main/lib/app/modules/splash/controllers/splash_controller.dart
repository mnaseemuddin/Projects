
import 'dart:async';
import 'dart:math';

import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/AffiliateApp/AffiliateSplashScreen.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/AffiliateLoginView.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffilateDashBoardView.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/utils/navigator_helper.dart';
import 'package:video_player/video_player.dart';

import '../../AffiliateApp/PackageView.dart';

class SplashController extends GetxController {
  final DashboardController dashboardController = Get.find<DashboardController>();
  // final VideoPlayerController videoPlayerController = VideoPlayerController.asset('assets/video/jacboad_adv.mp4');
  final isInitialized = false.obs;

  final count = 0.obs;
  final box = GetStorage();
  PackageInfo _packageInfo = PackageInfo(
    appName: 'Unknown',
    packageName: 'Unknown',
    version: 'Unknown',
    buildNumber: 'Unknown',
    buildSignature: 'Unknown',
  );

  Timer? _timer;

  @override
  void onInit() {
    super.onInit();
    _initPackageInfo();
    // videoPlayerController.initialize().then((value){
    //   isInitialized.value = true;
    //   videoPlayerController.play();
    // });
    //
    // videoPlayerController.addListener(() {
    //   Duration duration = videoPlayerController.value.duration;
    //   Duration position = videoPlayerController.value.position;
    //   if (duration.compareTo(position) != 1) {
    //     print('.......... Completed');
    //     _launchScreen();
    //   }
    // });

    _timer = Timer(Duration(seconds: 2), () => _aFLaunch());

  }

  Future<void> _initPackageInfo() async {
    final info = await PackageInfo.fromPlatform();
      _packageInfo = info;
      print(_packageInfo.buildNumber);
  }

  void _aFLaunch() async{
    getAccountType().then((value1){
      if(value1!=""){
        if(value1!="Bot"){
          getUser().then((value){
            if(value!=null){
              if(value.data.first.amountStack==1){
                Get.offAll(() => AffiliateDashBoardView());
              }else {
                Get.offAll(AffiliatePackageView());
              }
            }else{
              // Get.off(() => AffiliateLoginView());
              Get.offAll(() => AffiliateSplashScreen());
            }
          });
        }else{
          _launchScreen();
        }
      }else{
        Get.offAll(() => AffiliateSplashScreen());
      }
    });

  }

  _launchScreen(){
    var user = box.read('user_info');
    if(user!=null) {
      if (user is UserInfo) {
        userInfo = user;
      } else {
        userInfo = UserInfo.fromJson(user);
      }
    }
    if(userInfo!=null){
      dashboardController.updateUserInfo();
    }
    Get.offAllNamed(userInfo == null?Routes.LOGIN:Routes.DASHBOARD);
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    _timer?.cancel();
    // videoPlayerController.removeListener(() { });
    // videoPlayerController.dispose();
  }
  // void increment() => count.value++;


}
