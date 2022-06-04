import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/screens/dashboard.dart';
import 'package:skillcoin/screens/market_curreny_page.dart';

void main() {
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitDown,
    DeviceOrientation.portraitUp,
  ]);
  SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
    statusBarColor: Colors.transparent,
    statusBarBrightness: Brightness.dark
  ));
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.dark);
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'SkillCoin',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        appBarTheme: const AppBarTheme(
        color: PRIMARY_APP_COLOR,
        systemOverlayStyle: SystemUiOverlayStyle.dark,
      )
      ),
      home: const DashBoard(),
    );
  }
}



