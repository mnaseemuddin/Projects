import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffiliateHomeView.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffiliateIncomeDetailView.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:transparent_image/transparent_image.dart';

import '../../../shared/sawidgets/SACarousel.dart';
import 'AffiliateProfileDetailView.dart';
import 'Team/AffiliateTeamDashBoardView.dart';

class AffiliateDashBoardView extends StatefulWidget {
  const AffiliateDashBoardView({Key? key}) : super(key: key);

  @override
  _AffiliateDashBoardViewState createState() => _AffiliateDashBoardViewState();
}

class _AffiliateDashBoardViewState extends State<AffiliateDashBoardView> {

  final PageController _pageController = PageController();
  var bottomIndex = 0;
  Size? _size;
  List banners=[
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcOwSLtWZF0ZHXrZDpbT3ozjH2tgta2dn9iQ&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcOwSLtWZF0ZHXrZDpbT3ozjH2tgta2dn9iQ&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcOwSLtWZF0ZHXrZDpbT3ozjH2tgta2dn9iQ&usqp=CAU"
  ];
  @override
  Widget build(BuildContext context) {
    _size = MediaQuery.of(context).size;
    return AFBg(child: WillPopScope(
      onWillPop: () => _onBackPressed(),
      child: Scaffold(
        backgroundColor: Colors.transparent,
        bottomNavigationBar: _createBottomNavigationBar(),
        body: PageView(
          controller: _pageController,
          physics: NeverScrollableScrollPhysics(),
          children: [
            AffiliateHomeView(movePage: (val){
              _pageController.animateToPage(val, duration: Duration(milliseconds: 200), curve: Curves.ease);
            }),
            AffiliateTeamView(),
            AffiliateIncomeDetailView(),
            AffilateProfileView()
          ],),
      ),
    ));
  }

  _createBottomNavigationBar() {
    return Container(
      decoration: BoxDecoration(
          gradient: LinearGradient(colors: [Color(0xFF222C35),Color(0xFF0A1319), ])

          // gradient: LinearGradient(
          //     begin: Alignment.topLeft,
          //     end: Alignment.bottomRight,
          //     colors: [
          //       Color(0xFFFF5400),
          //       Color(0xFF8b009e), Color(0xff1f0fa1)
          //     ])
      ),
      child: BottomNavigationBar(
          showUnselectedLabels: false,
          backgroundColor: Color(0xFF),
          fixedColor: Color(0xFFFF5555),
          // fixedColor: ColorConstants.iconTheme,
          // fixedColor: isPlatformIOS?Colors.blue:Colors.white,
          showSelectedLabels: true,
          onTap: (index) {
            print('Index => $index');
            updateBottomIndex(index);
            _pageController.animateToPage(
                index, duration: Duration(milliseconds: 500,),
                curve: Curves.ease);
          },
          currentIndex: bottomIndex,
          type: BottomNavigationBarType.fixed,
          unselectedItemColor: Colors.white60,
          items: [
            BottomNavigationBarItem(
                icon: Icon(Ionicons.home), label: 'Home'),
            BottomNavigationBarItem(
                icon: Icon(Ionicons.people_circle), label: 'Team'),
            BottomNavigationBarItem(
                icon: Icon(Ionicons.wallet), label: 'Income'),
            // BottomNavigationBarItem(icon: Icon(AntDesign.bells), label: 'News'.tr),
            BottomNavigationBarItem(icon: Icon(Feather.user), label: 'Profile'),
          ]),);
  }

  void updateBottomIndex(int index) {
    setState(() {
      bottomIndex = index;
      print('current => $index');
    });
  }

  _onBackPressed() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          backgroundColor: Colors.white,
          title: Text("Exit?", style: TextStyle(
              color: Colors.black, fontSize: 15.0)),
          content: new Text("Are you sure you want to exit?", style: TextStyle(
              color: Colors.black, fontSize: 15.0)),
          actions: <Widget>[
            TextButton(
              child: new Text(
                "CANCEL",
                style: TextStyle(
                  color: Colors.black,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),

            TextButton(
              child: new Text(
                "OK",
                style: TextStyle(
                  color: Colors.black,
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
