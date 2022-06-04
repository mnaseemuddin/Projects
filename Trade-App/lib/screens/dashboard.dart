import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:ionicons/ionicons.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/data_streamer.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/screens/profile.dart';
import 'package:trading_apps/screens/faq.dart';
import 'package:trading_apps/screens/home.dart';
import 'package:trading_apps/screens/trades.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

import 'Shopping/ShoppingActivity.dart';

class Dashboard extends StatefulWidget {
  final String from;
  const Dashboard({required this.from,Key? key}) : super(key: key);

  @override
  _DashboardState createState() => _DashboardState();
}

class _DashboardState extends State<Dashboard> {


  PageController _controller = PageController();
  int _page = 0;
  int index=0;
  final List<Widget> _children = [
    // Home(),
    Home(),
    Trades(),
    ShoppingActivity(),
    FAQuestion(), Profile(),];
  @override
  void initState() {
    super.initState();
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    if(widget.from=="Shopping") {
      setState(() {
        index = 2;
      });
    }else if(widget.from=="Profile"){
      setState(() {
        index = 4;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light);
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?WillPopScope(
          onWillPop: (){
            showDialog(
              context: context,
              builder: (BuildContext context) {
                return AlertDialog(
                  backgroundColor: Color(0xff262424),
                  title: Text("Exit?",style: textStyleTitle(
                      color: Colors.white,fontSize: 15.0)),
                  content: new Text("Are you sure you want to exit?",style: textStyleTitle(
                      color: Colors.white,fontSize: 15.0)),
                  actions: <Widget>[
                    new TextButton(
                      child: new Text(
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

                    new TextButton(
                      child: new Text(
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
            return Future.value(false);
          },
          child: Scaffold(
            bottomNavigationBar:BottomNavigationBar(
              onTap: onTabTapped,
              type: BottomNavigationBarType.fixed,
              selectedItemColor: APP_SECONDARY_COLOR,
              backgroundColor: APP_PRIMARY_COLOR,
              showSelectedLabels: true,
              unselectedItemColor: Colors.grey,
              showUnselectedLabels: true,
              currentIndex: index,
              items: [
                BottomNavigationBarItem(
                  label: 'Terminal',
                  icon: Icon(Ionicons.analytics),
                ),
                BottomNavigationBarItem(
                  label: 'Trades',
                  icon: Icon(Ionicons.swap_horizontal),
                ),
                BottomNavigationBarItem(
                  label: 'Shopping',
                  icon: Icon(FontAwesomeIcons.shoppingBag),
                ),
                BottomNavigationBarItem(
                  label: 'FAQ',
                  icon: Icon(FontAwesomeIcons.questionCircle),
                ),
                //TextStyle(fontWeight: FontWeight.w500,
                   //   fontSize: 15)
                BottomNavigationBarItem(
                  label:'Profile',
                  icon: Icon(Ionicons.ellipsis_horizontal_circle_outline),
                ),
              ],

            ),
            backgroundColor: APP_PRIMARY_COLOR,
            body: /*PageView.builder(
          controller: _controller,
          onPageChanged: (page){
            setState(() => _page = page);
          },
          itemBuilder: (context, index){
            print('Selected Index ==> $index');
          switch(index){
            case 0:
              return Home();
            case 1:
              return Trades();
            case 2:
              return ShoppingActivity();
            case 3:
              return FAQuestion();
            case 4:
              return Profile();
            default:
              return Home();
          }
        }, itemCount: 5,
        physics: NeverScrollableScrollPhysics(),)*/_children[index],
          ),
        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

 

  void onTabTapped(int index1) {
    setState(() {
      index = index1;
    });
  }

  // _onBottomMenuSelected(int index){
  //   // if(index>0)
  //   // navPush(context, _pages[index]);
  //   setState(() {
  //     _page = index;
  //     _controller.animateToPage(_page, duration: Duration(milliseconds: 200),
  //         curve: Curves.easeIn);
  //   });
  // }
}


