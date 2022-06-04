import 'dart:async';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Screens/BNavigationFolder/bottommenuactivity.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'Screens/Auth/login.dart';
import 'apis/userdata.dart';


void main() {
  runApp(const MyApp());
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitDown,
    DeviceOrientation.portraitUp,
  ]);
  SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
    statusBarColor: Colors.transparent,
  ));
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.dark);
    return GetMaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'App Gallery',
      theme:  ThemeData(
          appBarTheme: const AppBarTheme(
            color: appBarColor,
            systemOverlayStyle: SystemUiOverlayStyle.dark,
          )),
      home: const Splash(),
    );
  }
}
//systemOverlayStyle: SystemUiOverlayStyle.dark,


class Splash extends StatefulWidget {
  const Splash({Key? key}) : super(key: key);

  @override
  _SplashState createState() => _SplashState();
}

class _SplashState extends State<Splash> {

  final prefs=GetStorage();

  @override
  void initState() {
   Timer(const Duration(seconds: 1),(){
     navPush(context, const Login());
   });
   initialize();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return  Scaffold();
  }



  void initialize() async{
    Timer(const Duration(seconds: 1),()async{
      getUserLoginDetails().then((value){
        if(value!=null){
          Get.to(const BottomMenuActivity());
        }else{
          Get.to(const Login());
        }
      });
    });
  }
 /* void initialize() async{
    Timer(const Duration(seconds: 1),()async{
      String email=await prefs.read('emailId');
      if(email!=null){
        Get.offAll(const BottomMenuActivity());
      }else{
        Get.offAll(const Login());
      }
    });
  }*/
}
