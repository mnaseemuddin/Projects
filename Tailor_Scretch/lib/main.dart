import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tailor/Screens/AllSketchActivity.dart';
import 'package:tailor/Auth/SignIn.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';

import 'Screens/ChooseSketchesActivity.dart';
import 'Screens/DashBoardActivity.dart';

void main() {
  SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
      statusBarColor: UISupport.APP_PRIMARY_COLOR));
  runApp(MyApp());
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitDown,
    DeviceOrientation.portraitUp,
  ]);
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(providers: [
      ChangeNotifierProvider(create: (context)=>ConnectivityProvider(),
      child: MyHomePage(title: 'Super Tailor'),)
    ],
    child: GetMaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Super Tailor',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Super Tailor'),
    ),);
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  var IsInitialized;

@override
  void initState() {
   Timer(Duration(seconds: 1),(){
   getEmailId().then((value) =>{
     if(value==null){
       Get.to(SignIn())
     }else{
       Get.to(DashBoard())
   }
   });
   });

  super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
    );
  }
}

