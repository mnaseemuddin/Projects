
import 'dart:async';


import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../../apis/userdata.dart';
import 'Auth/clogin.dart';
import 'Core/developerconsoleactivity.dart';


class CSplash extends StatefulWidget {
  const CSplash({Key? key}) : super(key: key);

  @override
  _CSplashState createState() => _CSplashState();
}

class _CSplashState extends State<CSplash> {

  @override
  void initState() {
    Timer(const Duration(milliseconds: 1),()async{
      getUser().then((value){
        if(value!=null){
          Get.off(const DeveloperConsoleActivity());
        }else{
          Get.off( const CLoginActivity());
        }
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return const Scaffold();
  }
}
