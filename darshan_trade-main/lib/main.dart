import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/app_binding.dart';
import 'package:royal_q/launguage.dart';
import 'app/routes/app_pages.dart';
import 'app/shared/constants/constants.dart';

//Todo list

//1. isExpertGain Change this parameter
//2. Change icons from res in android
//3. Change name in manifest file android.//no need
//4. Add or remove banner from home and circle screen // no need
//5. Change Splash icon
//6. Change Splash Color
//7. Generate Splash screen again

// const bool isPlatformIOS = false;
// const bool isExpertGain = true;

void main() {
  runApp(
    GetMaterialApp(
      debugShowCheckedModeBanner: false,
      title: "Darshan",
      translations: Language(),

      locale: Locale('en', 'US'),
      fallbackLocale: Locale('en', 'UK'),
      theme: ThemeData(
        primarySwatch: appColor,
        scaffoldBackgroundColor: ColorConstants.APP_PRIMARY_COLOR,
        canvasColor: ColorConstants.canvasColor,
        indicatorColor: ColorConstants.black,
        brightness: Brightness.light,
    ),
      themeMode: ThemeMode.light,
      initialRoute: AppPages.INITIAL,
      getPages: AppPages.routes,
      initialBinding: AppBinding(),
      builder: EasyLoading.init(),
    ),
  );
}


// //What is your first and last name?
//     //   [Unknown]:  Darshan Trade
//     // What is the name of your organizational unit?
//     //   [Unknown]:  Darshan Trade
//     // What is the name of your organization?
//     //   [Unknown]:  Darshan Trade
//     // What is the name of your City or Locality?
//     //   [Unknown]:  New Delhi
//     // What is the name of your State or Province?
//     //   [Unknown]:  Delhi
//     // What is the two-letter country code for this unit?
//     //   [Unknown]:  91
//     // Is CN=Darshan Trade, OU=Darshan Trade, O=Darshan Trade, L=New Delhi, ST=Delhi, C=91 correct?
//     //   [no]: