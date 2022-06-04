import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/shared/sawidgets/app_logo.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/main.dart';
import 'package:video_player/video_player.dart';

import '../controllers/splash_controller.dart';

class SplashView extends GetView<SplashController> {



  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    Size size = MediaQuery.of(context).size;
    return AFBg(child: Stack(children: [

      // Container(
      //   decoration: BoxDecoration(
      //       gradient: LinearGradient(
      //           colors: [ColorConstants.whiteRev, ColorConstants.whiteRev,],
      //           begin: Alignment.topCenter,
      //           end: Alignment.bottomCenter
      //       )
      //   ),
      // ),

      Scaffold(
        backgroundColor: Colors.transparent,
        body: Center(child: AFAppLogo(),),),
    ],));
  }

}
