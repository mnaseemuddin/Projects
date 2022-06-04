



import 'dart:io';

import 'package:flutter/material.dart';
import 'package:movieshow/Activity/FavoriteActivity.dart';
import 'package:movieshow/Activity/SettingActivity.dart';
import 'package:movieshow/SAUI/SA_Home.dart';
import 'HomeActivity.dart';

class DashBoard extends StatefulWidget {

  @override
  _DashBoardState createState() => _DashBoardState();
}

class _DashBoardState extends State<DashBoard> {
  var _index=0;
  final List<Widget> _children = [
    // Home(),
    SAHome(),
    FavoriteActivity(),
    Setting()];

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
         showDialog(
            context: context,
            builder: (BuildContext context) {
              // return object of type Dialog
              return AlertDialog(
                title: Text("Exit?"),
                content: new Text("Are you sure you want to exit?"),
                actions: <Widget>[
                  new FlatButton(
                    child: new Text("CANCEL",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                      fontSize: 14.899999618530273,),),
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                  ),

                  new FlatButton(
                    child: new Text("OK",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                      fontSize: 14.899999618530273,),),
                    onPressed: () {
                     exit(0);
                    },
                  ),
                  // usually buttons at the bottom of the dialog

                ],
              );
            },
          );
         return Future.value(false);
      },
      child: Scaffold(
        backgroundColor: Colors.black,
        appBar: AppBar(
         // leading: Container(),
          backgroundColor: Colors.black,
          title: Image.asset('drawable/showmovie.png',width: 180,),
          actions: [
            Padding(
              padding: const EdgeInsets.only(right:8.0),
              child: Image.asset("drawable/notifi.png",height: 30,width: 30,),
            ),
          ],
        ),
        body: _children[_index],
        bottomNavigationBar: BottomNavigationBar(
          currentIndex: _index,
          onTap: onTabTapped,
          backgroundColor: Colors.black,
          selectedItemColor: Colors.red,
          unselectedItemColor: Colors.white,
          items: [
            BottomNavigationBarItem(
              icon: new Icon(Icons.home),
            ),
            BottomNavigationBarItem(
              icon: new Icon(Icons.favorite),
            ),
            BottomNavigationBarItem(
                icon: Icon(Icons.settings),
            )
        ],

        ),
      ),
    );
  }
  void onTabTapped(int index1) {
    setState(() {
      _index = index1;
    });
  }
}
/**/
//keytool -genkey -v -keystore c:\Users\LogiClump\key.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias key
//keytool -genkey -v -keystore ~/resultkeystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
