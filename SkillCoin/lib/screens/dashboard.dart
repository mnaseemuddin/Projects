
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_icons/flutter_icons.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:skillcoin/api/user_data.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:skillcoin/customui/textview.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/constants.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/market_curreny_page.dart';
import 'package:skillcoin/screens/transaction.dart';

import 'buysellcurrency.dart';

class DashBoard extends StatefulWidget {

  const DashBoard({Key? key}) : super(key: key);

  @override
  _BottomNavigatoinBarState createState() => _BottomNavigatoinBarState();
}

class _BottomNavigatoinBarState extends State<DashBoard> {

  final _kTabPages=<Widget>[

     const MarketPageActivity(),
    const MarketPageActivity(),
     const TransactionActivity(),


  ];
  int index=0;

  bool selected=true,selected1=false,selected2=false;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: ()=>onBackPressDialogAlert(),
      child: Scaffold(
        body: _kTabPages[index],
        bottomNavigationBar: BottomNavigationBar(
          onTap: onTabTapped,
          elevation: 10,
          selectedItemColor: PRIMARYBLACKCOLOR,
          showSelectedLabels: false,
          showUnselectedLabels: false,
          unselectedItemColor: PRIMARYLIGHTGREYCOLOR,
          backgroundColor: PRIMARYWHITECOLOR,
          currentIndex: index,
          items: [
             BottomNavigationBarItem(
              label: '',
              icon: Image.asset(imgGraph,height: 19,width: 19,color: selected?
              Colors.black:PRIMARYLIGHTGREYCOLOR,),
            ),
             BottomNavigationBarItem(
              label: '',
              icon: Container(decoration:  const BoxDecoration(
                shape: BoxShape.rectangle,
                borderRadius: BorderRadius.only(topLeft: Radius.circular(10),
                    topRight: Radius.circular(10),bottomLeft:Radius.circular(10),
                    bottomRight: Radius.circular(10)),
                color: Color(0xffFFBF00),
              ),child: Padding(
                padding: const EdgeInsets.only(left:10.0,right: 10),
                child: Image.asset(imgBuySell,color: PRIMARYBLACKCOLOR,),
              ),height: 42,width: 42,
              )
            ),
              BottomNavigationBarItem(
              label: '',
              icon: Image.asset(imgTransaction,height: 27,width: 27,color: selected2?
              Colors.black:PRIMARYLIGHTGREYCOLOR,),
            ),
          ],
        ),
      ),
    );
  }


  void onTabTapped(int index1) {
    _selectedUnselected(index1);
  }

  void buySellBottomSheet(context) {
    showModalBottomSheet(
        context: context,
        backgroundColor: Colors.transparent,
        builder: (context) => MarketContainerBg(width: double.infinity, color: PRIMARYWHITECOLOR,
            circular: 10, child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [

          Padding(
            padding: const EdgeInsets.only(left:12.0,top:20),
            child: GestureDetector(
              onTap: (){
                navPush(context,const BuySellCurrencyActivity(type: "Buy Crypto",));
              },
              child: Container(
                color: Colors.white,
                width: double.infinity,
                height: 70,
                child: Row(children: [
                  Expanded(
                      flex: 20,
                      child: Image.asset(imgBuyCurrent,height: 30,)),
                  Expanded(
                    flex: 75,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: const [
                        Align(
                            alignment: Alignment.centerLeft,
                            child: Padding(
                              padding: EdgeInsets.only(bottom:4.0),
                              child: NormalHeadingText(title: "Buy", fontSize: 16, color: PRIMARYBLACKCOLOR),
                            )),
                        Align(
                          alignment: Alignment.centerLeft,
                          child: NormalHeadingText(title: "Buy crypto with your local currency",
                              fontSize: 13.4, color: PRIMARYLIGHTGREYCOLOR),
                        )

                      ],),
                  )

                ],),
              ),
            ),
          ),
          const SizedBox(height:15),
          const Divider(height: 1.5,),
          Padding(
            padding: const EdgeInsets.only(left:10.0,top:12),
            child: GestureDetector(
              onTap: (){
                navPush(context,const BuySellCurrencyActivity(type: "Sell Crypto",));
              },
              child: Container(
                color: Colors.white,
                width: double.infinity,
                height: 70,
                child: Row(children: [
                  Expanded(
                      flex: 20,
                      child: Image.asset(imgSell,height: 30,)),
                  Expanded(
                    flex: 75,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: const [
                        Align(
                            alignment: Alignment.centerLeft,
                            child: Padding(
                              padding: EdgeInsets.only(bottom:4.0),
                              child: NormalHeadingText(title: "Sell", fontSize: 16, color: PRIMARYBLACKCOLOR),
                            )),
                        Align(
                          alignment: Alignment.centerLeft,
                          child: NormalHeadingText(title: "Sell crypto to your local currency",
                              fontSize: 13.4, color: PRIMARYLIGHTGREYCOLOR),
                        )

                      ],),
                  )

                ],),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,bottom: 20),
            child: GestureDetector(
              onTap: (){
                navPushOnBackPressed(context);
              },
              child: const ContainerBgRCircle(width: 40, height: 40, color: Color(0xffFFBF00),
                  circular: 7, child: Icon(Icons.cancel_rounded,color: PRIMARYWHITECOLOR,)),
            ),
          )

        ],)));
  }


  void _selectedUnselected(int index1) {
    setState(() {
      index = index1;
      selected=false;
      selected1=false;
      selected2=false;
      if(index==0){
        if(selected==true){
          selected=false;
        }else{
          selected=true;
        }
      }else if(index==1){
        if(selected1==true){
          selected1=false;
        }else{
          selected1=true;
        }
      }else if(index==2){
        if(selected2==true){
          selected2=false;
        }else{
          selected2=true;
        }
      }
      if(index==1){
        getUser().then((value){
          if(value!=null){
            buySellBottomSheet(context);
          }else{
            DialogUtils.instance.edgeAlerts(context,"Please Login First For Buy & Sell .");
          }
        });
      }
    });
  }
  onBackPressDialogAlert(){
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          backgroundColor: PRIMARYWHITECOLOR,
          title: Text("Exit?",style: TextStyle(
              color: Colors.black,fontSize: 15.0)),
          content: new Text("Are you sure you want to exit?",style: TextStyle(
              color: Colors.black,fontSize: 15.0)),
          actions: <Widget>[
            new FlatButton(
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

            new FlatButton(
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


