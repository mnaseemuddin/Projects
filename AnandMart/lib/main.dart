import 'dart:async';
import 'package:anandmart/Support/SharePreferenceManager.dart';
//import 'package:firebase_core/firebase_core.dart';
//import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
//import 'package:package_info_plus/package_info_plus.dart';
import 'Activity/DashBoard.dart';
import 'Activity/SelectCityAndCityAreaActivity.dart';
import 'Login.dart';
import 'PushNotification.dart';

void main() async{

  WidgetsFlutterBinding.ensureInitialized();
 // await Firebase.initializeApp();
  SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.dark);
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {

    return GetMaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Anand Mart',
      theme: ThemeData(
        appBarTheme: Theme.of(context).appBarTheme.copyWith(brightness:
        Brightness.light,color: Color(0xfff4811e)),
      ),
      home: MyHomePage(title: 'Anand Mart'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);



  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

/*  late  FirebaseMessaging _firebaseMessaging;
  late PushNotification _notificationInfo;
  PackageInfo _packageInfo = PackageInfo(
    appName: 'Unknown',
    packageName: 'Unknown',
    version: 'Unknown',
    buildNumber: 'Unknown',
    buildSignature: 'Unknown',
  );*/
  @override
  void initState() {
    Timer(
        Duration(seconds: 3),
            () {
          SharePreferenceManager.instance.IsSharePreferenceInitialized("Initialize").then((value){
            setState(() {
              InitializeCitysAndUser(value);
            });
          });

        });
  /*  _initPackageInfo();

        _firebaseMessaging = FirebaseMessaging.instance;
        _firebaseMessaging.requestPermission();
        _firebaseMessaging.getToken().then((value) =>
        print(value));
        _firebaseMessaging.subscribeToTopic('newtopic');
    FirebaseMessaging.onMessage.listen((RemoteMessage event) async {
        NotificationSettings settings = await _firebaseMessaging.requestPermission(
    alert: true,
    badge: true,
    provisional: false,
    sound: true,
  );
  if (settings.authorizationStatus == AuthorizationStatus.authorized) {
  checkForInitialMessage();
  }


    });*/
    super.initState();
  }

//   Future<void> _initPackageInfo() async {
//     final info = await PackageInfo.fromPlatform();
//     _packageInfo = info;
//     print(_packageInfo.buildNumber);
//   }
//
//   checkForInitialMessage() async {
//   await Firebase.initializeApp();
//   RemoteMessage? initialMessage =
//       await FirebaseMessaging.instance.getInitialMessage();
//
//   if (initialMessage != null) {
//     PushNotification notification = PushNotification(
//       title: initialMessage.notification?.title,
//       body: initialMessage.notification?.body,
//     );
//     setState(() {
//       _notificationInfo = notification;
//       //_totalNotifications++;
//     });
//   }
// }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(child: Image.asset("drawable/logo.png",height: 250,width: 250,)),
      // This trailing comma makes auto-formatting nicer for build methods.
    );
  }

  void InitializeCitysAndUser(String IsInitilized) {
    SharePreferenceManager.instance.getCity("City").then((value){
      setState(() {
        if(IsInitilized=="Yes"){
          if(value==""){
            Get.to(SelctCityAndCityArea());
          }else {
            Get.offAll(DashBoard());
          }
        }else{
          Get.offAll(Login());
        }
      });
    });
  }
}

