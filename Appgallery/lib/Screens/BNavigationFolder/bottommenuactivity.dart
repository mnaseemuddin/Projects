

import 'dart:io';

import 'package:appgallery/Screens/Auth/login.dart';
import 'package:appgallery/Screens/Console/csplash.dart';
import 'package:appgallery/Screens/FeaturedFolder/tabcontrolleractivity.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:appgallery/main.dart';
import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_icons/flutter_icons.dart';
import 'package:get/get.dart';
import '../../Resources/constants.dart';
import 'appsactivity.dart';
import 'featuredactivity.dart';
import 'gameactivity.dart';
import 'meactivity.dart';

class BottomMenuActivity extends StatefulWidget {
  const BottomMenuActivity({Key? key}) : super(key: key);

  @override
  _BottomMenuActivityState createState() => _BottomMenuActivityState();
}

class _BottomMenuActivityState extends State<BottomMenuActivity> {
  int index=0;
  final GlobalKey _bottomNavigationKey = GlobalKey();
  final List<Widget>_children=[
   const TabControllerActivity(),
    const GameActivity(),
    const AppsActivity(),
    const MeActivity(),
    const CSplash()
  ];

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
        onBackPressed();
        return Future.value(false);
      },
      child: Scaffold(
        body: _children[index],
        bottomNavigationBar: CurvedNavigationBar(
          key: _bottomNavigationKey,
          color: Colors.transparent,
          buttonBackgroundColor: Colors.grey[200],
          backgroundColor: Colors.transparent,
          items: const [
            Icon(Icons.widgets_rounded,size: 30,color: Colors.black87),
            Icon(Ionicons.logo_game_controller_b,size: 30,color: Colors.black87),
            Icon(Icons.shopping_bag,size: 30,color: Colors.black87),
            Icon(Icons.account_circle_rounded,size: 30,color: Colors.black87,),
            Icon(Ionicons.ios_code_working,size: 30,color: Colors.black87,)
          ],
          onTap: (_index) {
            setState(() {
              index = _index;
            });
          },
        ),
      ),
    );
  }
  void onBackPressed() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          backgroundColor: Color(0xff262424),
          title: Text("Exit?",style: textStyleTitle(
              color: Colors.white,fontSize: 15.0)),
          content: Text("Are you sure you want to exit?",style: textStyleTitle(
              color: Colors.white,fontSize: 15.0)),
          actions: <Widget>[
            FlatButton(
              child: const Text(
                "CANCEL",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),

            FlatButton(
              child: const Text(
                "OK",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                exit(0);
              },
            ),
            // usually buttons at the bottom of the dialog
          ],
        );
      },
    );
  }
}
//https://i.pinimg.com/originals/3e/e5/d3/3ee5d3b0d6a965a60e45937199080419.png
