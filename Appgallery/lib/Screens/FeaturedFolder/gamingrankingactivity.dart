



import 'dart:convert';

import 'package:appgallery/Controllers/gamerankingcontroller.dart';
import 'package:appgallery/Model/featurednewappsmodel.dart';
import 'package:appgallery/Model/gamerankingheadingmodel.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import '../../Utils/loadingoverlay.dart';
import '../../apis/apirepositary.dart';
import '../PlayStore/appsdetailsactivity.dart';
import '../PlayStore/appssearchactivity.dart';


class GamingRankingActivity extends StatefulWidget {
  const GamingRankingActivity({Key? key}) : super(key: key);

  @override
  _GamingRankingActivityState createState() => _GamingRankingActivityState();
}

class _GamingRankingActivityState extends State<GamingRankingActivity> {

  GameRankingController controller=Get.put(GameRankingController());
  int rowIndex=0;
  List? data,data1;

  @override
  void initState() {
    loadAllApps();
    super.initState();
  }


  Future<String> loadAllApps() async {
    final jsonResponse=await http.post(Uri.parse(allPlayStoreAppsUrl));
    setState(() {
      data=json.decode(jsonResponse.body);
      print(data);
    });
    return 'Success';
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff1f3f5),
      body: SafeArea(
          bottom: true,
          child: Column(
            children: [
              Expanded(
                flex: 8,
                child: GestureDetector(
                  onTap: () {
                    navPush(context, const AppsSearchActivity());
                  },
                  child: Container(
                    margin: const EdgeInsets.fromLTRB(15, 10, 15, 10),
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(20), color: Colors.white),
                    child: Row(
                      children: [
                        Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: const EdgeInsets.fromLTRB(15, 12, 0, 12),
                            child: Image.asset(
                              assetsSearchImg,
                              height: 15,
                              width: 15,
                            ),
                          ),
                        ),
                        const Padding(
                          padding: EdgeInsets.only(left: 18.0),
                          child: Text(
                            'PUBG',
                            style: TextStyle(fontSize: 13.5, color: Colors.black87),
                          ),
                        )
                      ],
                    ),
                  ),
                ),
              ),
              Expanded(
                flex: 6,
                child: Padding(
                  padding: const EdgeInsets.only(bottom: 5.0),
                  child: ListView.builder(
                    scrollDirection: Axis.horizontal,
                    itemCount: controller.gameRankingHeadingList.length??0,
                    itemBuilder:(ctx,index){
                      GameRankingHeadingModel gameModel=controller.
                      gameRankingHeadingList.elementAt(index);
                      return Padding(
                        padding: const EdgeInsets.only(left:8),
                        child: Container(
                          height: double.infinity,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(18),
                              color: gameModel.color),
                          child:  Center(
                              child: Padding(
                                padding: const EdgeInsets.only(left:10.0,right: 10),
                                child: Text(
                                  gameModel.name, style: const TextStyle(color: Colors.black),
                                ),
                              )),
                        ),
                      );
                    }),
                  ),
                ),
              Expanded(
                  flex: 84,
                  child: data==null?LoadingOverlay():ListView.builder(
                    scrollDirection: Axis.vertical,
                    itemCount: data!.length??0,
                    itemBuilder: (ctx,index){
                      data1=data![index]['Category'];
                      return moreAppsListUI(data1);
                    },)),
            ],
          )),
    );
  }
  moreAppsListUI(List? data1) {
    return  Column(children: data1!.map((e) => Padding(
      padding: padding8,
      child: GestureDetector(
        onTap: (){
          navPush(context, AppsDetailsActivity
            (appId: e['id'],appIcon: e['img'],
            appName: e['name'],));
        },
        child: Container(
          color: const Color(0xfff1f3f5),
          height: 99,
          child: Row(children: [
            Container(
              height: 70,
              color: const Color(0xfff1f3f5),
              width: MediaQuery.of(context).size.width*.17,
              child: ClipRRect(
                  borderRadius: BorderRadius.circular(14),
                  child: Image.network(e['img'],fit: BoxFit.fitWidth,)),
            ),
            Padding(
              padding: const EdgeInsets.only(left:8.0),
              child: SizedBox(
                width: MediaQuery.of(context).size.width*.75,
                child: Padding(
                  padding: const EdgeInsets.only(top:8.0),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          SizedBox(
                            width: MediaQuery.of(context).size
                                .width*.40,
                            child: Text(e['name'],
                                overflow:TextOverflow.ellipsis,
                                style: const TextStyle(color: Colors.black)),
                          ),
                          const Spacer(),
                          Padding(
                            padding: const EdgeInsets.only(right:8.0),
                            child: Container(
                              width: 80,
                              height: 35,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(15),
                                  color: const Color(0xffe8e8e8)
                              ),
                              child: Center(
                                child: Text('Install'.toUpperCase(),style: const TextStyle(color:
                                Color(0xff0a58f5)),),
                              ),
                            ),
                          )
                        ],
                      ),
                      Align(
                          alignment: Alignment.topLeft,
                          child: Text(e['category'])),
                      Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: const EdgeInsets.only(top:3.0),
                            child: Text(
                                e['description'],
                                overflow:TextOverflow.ellipsis,style:
                            const TextStyle(color: Colors.black)),
                          )),
                      Padding(
                        padding: const EdgeInsets.only(top:20.0,right:10 ),
                        child: Container(
                          width: double.infinity,
                          height: 1,
                          color: Colors.grey[300],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],),
        ),
      ),
    )).toList());
  }
}
