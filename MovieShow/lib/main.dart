import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:firebase_core/firebase_core.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
import 'package:adobe_xd/pinned.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/DashBoard.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';

import 'Authentication/SignInActivity.dart';


void main() async{
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'PlayBox',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'PlayBox'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> with SingleTickerProviderStateMixin{

  late Tween<double> _tweenSize;
  late Animation<double> _animationSize;
  late AnimationController _animationController;

@override
  void initState() {
    super.initState();
      _animationController =
          AnimationController(vsync: this, duration: Duration(milliseconds: 400));
      _tweenSize = Tween(begin: 30, end: 250);
      _animationSize = _tweenSize.animate(_animationController);
      _animationController.forward();
      Timer(
          Duration(seconds: 3),
              () {
            SharePreferenceManager.instance.IsSharePreferenceInitialized("Initilized").then((value){
              setState(() {
                String IsInitilized=value;
                if(IsInitilized=="Exits"){
                  Get.offAll(DashBoard());
                }else{
                Get.offAll(SignIn());
                }
              });
            });
          });
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
        backgroundColor: const Color(0xff000000),
      body:AnimatedBuilder(
        animation: _animationSize,
        builder: (context,child){
          return Center(
            child: Padding(
              padding: const EdgeInsets.only(top:15.0,bottom: 15,left: 0,right: 0),
              child: Container(
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: AssetImage("drawable/logo.png"),
                    fit: BoxFit.cover,
                  ),
                ),
                height: 200,
                width:300,
              ),
            ),
          );
        },

      )
    );
  }
}

// @UIApplicationMain
// class AppDelegate: UIResponder, UIApplicationDelegate {
//
// var window: UIWindow?
//
// func application(_ application: UIApplication,
// didFinishLaunchingWithOptions launchOptions:
// [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
// FirebaseApp.configure()
//
// return true
// }