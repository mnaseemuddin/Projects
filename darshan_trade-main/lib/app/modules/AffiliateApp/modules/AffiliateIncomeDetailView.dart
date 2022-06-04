

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AfiliateLevelIncomeResposne.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/ROIResponse.dart';
import 'package:royal_q/app/shared/shared.dart';

import '../../../shared/constants/colors.dart';
import '../../../shared/constants/common.dart';
import '../../../shared/sawidgets/sa_tabview.dart';
import '../../../shared/utils/NoData.dart';
import '../../../shared/utils/navigator_helper.dart';

class AffiliateIncomeDetailView extends StatefulWidget {
  const AffiliateIncomeDetailView({Key? key}) : super(key: key);

  @override
  _AffiliateIncomeDetailViewState createState() => _AffiliateIncomeDetailViewState();
}

class _AffiliateIncomeDetailViewState extends State<AffiliateIncomeDetailView> {

  int inddex=0;
  PageController pageController=PageController();
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final storage=GetStorage();
  RoiResposne? roiModel;
  AffiliateLevelIncomeResponse? levelIncomeModel;

  @override
  void initState() {
    getUser().then((value){
      getROIListAPI(value!.data.first.tblUserId.toString(),
          value!.data.first.jwtToken).then((res){
        if(value.status){
          setState(() {
            roiModel=res.data;
          });
        }else{
          toastMessage(value.message.toString());
        }
      });
    });
getUser().then((value){
  getLevelIncomeList(value!.data.first.tblUserId.toString(),
      value!.data.first.jwtToken,value!.data.first.referalCode);
});

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      appBar: AppBar(
        leading: Container(),
        title: Text('Income Detail', style: textStyleHeading2(color: Colors.white),),
        iconTheme: IconThemeData(color: Colors.white),
        elevation: 0,
        // brightness: Brightness.dark,
        // systemOverlayStyle: SystemUiOverlayStyle(
        //   systemNavigationBarColor: ColorConstants.APP_PRIMARY_COLOR, // Navigation bar
        //   statusBarColor: ColorConstants.APP_PRIMARY_COLOR, // Status bar
        // ),
        backgroundColor: Colors.transparent,
        actions: [
          // IconButton(onPressed: (){
          //   setState(() => _isVisible = !_isVisible);
          // }, icon: Icon(AntDesign.filter))
        ],
      ),
      body: SafeArea(
        top: true,

        child: Container(
          width: double.infinity,
          child: Column(children: [
            SizedBox(height: 8,),
            // Container(height: 40,
            //   child: Row(children: [
            //     Expanded(
            //         child: GestureDetector(child: _createItem(inddex==0, 'Return Of Investment'), onTap: (){
            //           setState(() {
            //             inddex = 0;
            //             pageController.jumpToPage(inddex);
            //           });
            //         },)),
            //     Expanded(child: GestureDetector(child: _createItem(inddex==1, 'Level'), onTap: (){
            //       setState(() {
            //         inddex = 1;
            //         pageController.jumpToPage(inddex);
            //       });
            //     },)),
            //   ],),
            // ),


            Container(height: 40,
              alignment: Alignment.centerLeft,
              child: SATabview(titles: ['Return Of Investment', 'Level'],
                selectedColor: Color(0xFFFF5555), textColor: Color(0xFFFF5555), expand: true,
                unselectTextColor: Colors.white,
                onPageChange: _onPageChange, onSelect: (page){
                  pageController.animateToPage(page, duration: Duration(milliseconds: 800), curve: Curves.ease);
                },),),


            SizedBox(height: 8,),
            Expanded(child: PageView.builder(

              controller: pageController,
              allowImplicitScrolling: false,
              //  physics: NeverScrollableScrollPhysics(),
              onPageChanged: (page){
                setState(() {
                  inddex = page;
                  _onPageChange.add(page);
                });
              },
              itemBuilder: (context, index){
                return inddex==0?Container(
                  child: roiModel==null?AFNoRecord():_pageROIIncome(),):
                levelIncomeModel==null?AFNoRecord():_pageLevelIncome();
              }, itemCount: 2,))
          ],),
        ),
      ),
    );
  }

  Widget _createItem(bool selected, String title){
    return Padding(
      padding: const EdgeInsets.only(left: 8.0),
      child: Container(
        child: Padding(
          padding: const EdgeInsets.only(left:8.0),
          child: Text(title, style: textStyleLabel(color: selected?Colors.yellow[800]:Colors.white54),),
        ),
        alignment: Alignment.centerLeft,
        decoration: BoxDecoration(
            border: Border(bottom: BorderSide(color: selected?Colors.yellow[800]!:Colors.transparent, width: 2))
        ),
      ),
    );
  }

Widget _pageROIIncome() =>ListView(children: [

  minSpacing(8),
  Container(
    color: Colors.green[800],
    height: 80,
    child: Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: [
              Text("Today ROI Income",style: textStyleLabel(color: Colors.white),),
              Spacer(),
              Text(roiModel!.todayRoiIncome.toStringAsFixed(4),style: textStyleHeading(fontSize: 16.0,color: Colors.white),),
              Text(" USDT",style: textStyleLabel(fontSize: 10,color: Colors.white),),

            ],
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: [
              Text("Total ROI Income",style: textStyleLabel(color: Colors.white),),
              Spacer(),
              Text(roiModel!.totalRoiIncome.toStringAsFixed(4),style: textStyleHeading(fontSize: 16.0,color: Colors.white),),
              Text(" USDT",style: textStyleLabel(fontSize: 10,color: Colors.white),),

            ],
          ),
        ),
      ],),
  ),

  // Padding(
  //   padding: const EdgeInsets.only(left:15.0,right: 15,top: 8,bottom: 8),
  //   child: Row(children: [
  //     Text("Date",style: textStyleTitle(fontSize: 15.0),),Spacer(),
  //     Text("Amount",style: textStyleTitle(fontSize: 15.0),)
  //   ],),
  // ),


  Padding(
    padding: const EdgeInsets.only(top:4.0),
    child: Column(children: roiModel!.data.map((e) => Column(
      children: [
        Container(
          margin: EdgeInsets.only(top: 1,bottom: 1,right: 5,left: 5),
          decoration: textBgGradient,
          child: Padding(
            padding: const EdgeInsets.only(left:15.0,right: 15,top: 5),
            child: Column(children: [
              Row(
                children: [
                  Text("ROI",style: textStyleLabel(fontSize: 15.0,color: Colors.white),),
                  Spacer(),
                  Text("${e.roiAmount} USDT",style: TextStyle(color: Colors.white,
                      fontSize: 14.0,fontWeight: FontWeight.w500),),
                ],
              ),
              Padding(
                padding: const EdgeInsets.only(top:4.0,bottom: 4),
                child: Divider(height: 2,),
              ),
              Row(
                children: [
                  Text("Package ",style: textStyleLabel(fontSize: 15.0,color: Colors.white),),
                  Spacer(),
                  Text(e.packageName,style: TextStyle(color: Colors.white,
                      fontSize: 14.0,fontWeight: FontWeight.w500),),
                ],
              ), Padding(
                padding: const EdgeInsets.only(top:4.0,bottom: 4),
                child: Divider(height: 2,),
              ),

              Row(
                children: [
                  Text("Created Date",style: textStyleLabel(fontSize: 15.0,color: Colors.white),),
                  Spacer(),
                  Text(e.createdDate,style: TextStyle(color: Colors.white,
                      fontSize: 14.0,fontWeight: FontWeight.w500),),
                ],
              ),
              SizedBox(height: 7,)
            ],)
          ),
        ),
        SizedBox(height: 5,),
      ],
    )).toList(),),
  )





],);

  Widget _pageLevelIncome() =>Column(children: [

    minSpacing(8),
    Container(
      color: Colors.green[800],
      height: 80,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: [
              Text("Today Level Income",style: textStyleLabel(color: Colors.white),),
              Spacer(),
              Text(levelIncomeModel!.todayLevelIncome.toStringAsFixed(4),style: textStyleHeading(fontSize: 16.0,color: Colors.white),),
              Text(" USDT",style: textStyleLabel(fontSize: 10,color: Colors.white),),

            ],
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: [
              Text("Total Level Income",style: textStyleLabel(color: Colors.white),),
              Spacer(),
              Text(levelIncomeModel!.totalLevelIncome.toStringAsFixed(4),style: textStyleHeading(fontSize: 16.0,color: Colors.white),),
              Text(" USDT",style: textStyleLabel(fontSize: 10,color: Colors.white),),

            ],
          ),
        ),
      ],),
    ),

    Padding(
      padding: const EdgeInsets.only(left:15.0,right: 15,top: 8,bottom: 8),
      child: Row(children: [
        Text("Date",style: textStyleTitle(fontSize: 15.0),),Spacer(),
        Text("Amount",style: textStyleTitle(fontSize: 15.0),)
      ],),
    ),

//http://192.168.1.62/DarshanAppBot.git
    Expanded(
      flex: 60,
      child: ListView.builder(itemBuilder: (context, index){
        Datum1 model = levelIncomeModel!.data.elementAt(index);
        return ListTile(title: Text(model.createdDate,style: TextStyle(color: Colors.white),),
          trailing: Text('${model.roiAmount} USDT',style: TextStyle(color: Colors.white)),
          tileColor: index%2==0?ColorConstants.grey.withOpacity(0.07):null,);
      }, itemCount: levelIncomeModel!.data.length??0,),
    )





  ],);

Widget minSpacing(double height) {
  return SizedBox(height: height,);
}

  void getLevelIncomeList(String id, String token, String referalCode) {
  getLevelIncomeListAPI(id, token, referalCode).then((value){
    if(value.status){
      setState(() {
        levelIncomeModel=value.data;
      });
    }else{
      toastMessage(value.message.toString());
    }
  });
  }


}
