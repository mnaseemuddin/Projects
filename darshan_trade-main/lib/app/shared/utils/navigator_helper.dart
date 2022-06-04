import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';

class NavigatorHelper {
  static void popLastScreens({
    required int popCount,
  }) {
    int count = 0;
    while (count < popCount) {
      Get.back();
      count++;
    }
  }
}

navPush(context, screen) => Navigator.of(context).push(MaterialPageRoute(builder: (context) => screen));
navPushReplace(context, screen) => Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => screen));
toastMessage(msg)=>Fluttertoast.showToast(msg: msg);