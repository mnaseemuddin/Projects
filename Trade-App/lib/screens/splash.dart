import 'dart:async';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/data_streamer.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/auth/login_ui.dart';
import 'package:trading_apps/screens/profile.dart';
import 'package:trading_apps/screens/dashboard.dart';
import 'package:trading_apps/screens/trades.dart';
import 'package:trading_apps/utility/common_methods.dart';

import 'faq.dart';

class Splash extends StatefulWidget {
  const Splash({Key? key}) : super(key: key);

  @override
  _SplashState createState() => _SplashState();
}

class _SplashState extends State<Splash> {

  @override
  void initState() {

    getCurrency().then((value) => baseMarket=value);
    getUser().then((value){
      if(value==null){
        navPushReplace(context, LoginUI());
      }else{
        navPushReplace(context, Dashboard(from: ''));
        userModel = value;
      }
      initDataStreamer();
    });

    // Timer(Duration(seconds: 1), (){
    //   navPushReplace(context, LoginUI());
    //   // navPushReplace(context, Profile());
    //   // navPushReplace(context, Trades());
    //   // navPushReplace(context, FAQuestion());
    //   // navPushReplace(context, Dashboard());
    // });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      backgroundColor: APP_PRIMARY_COLOR,
      body: Center(
        child: Image.asset(playStoreIcon, width: size.width/2,),
      ),
    );
  }
}
