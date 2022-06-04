
import 'dart:convert';
import 'package:http/http.dart'as http;
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/Controllers/FeaturedController.dart';
import 'package:appgallery/Model/allplaystoreapps.dart';
import 'package:appgallery/Model/featurednewappsmodel.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import '../../apis/apirepositary.dart';
import '../PlayStore/appsdetailsactivity.dart';
import '../PlayStore/appssearchactivity.dart';
import '../PlayStore/categoryactivity.dart';
import '../PlayStore/moreappsactivity.dart';




class FeaturedActivity extends StatefulWidget {
  const FeaturedActivity({Key? key}) : super(key: key);

  @override
  _FeaturedActivityState createState() => _FeaturedActivityState();
}

class _FeaturedActivityState extends State<FeaturedActivity> {

  FeaturedController controller=Get.put(FeaturedController());
// late Map data;
// NewAppsModel? newAppsModel;
   List? data,data1,data2;
  AllPlayStoreApps? apps;
  int pageNumber=0;

  @override
  void initState() {
    super.initState();
    getAllPlayStoreApps();
  }


  Future<String> getAllPlayStoreApps() async {
    final jsonResponse=await http.post(Uri.parse(allPlayStoreAppsUrl));
    setState(() {
      data=json.decode(jsonResponse.body);
    });
    return 'Success';
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff1f3f5),
      body:Column(
        children: [
          appsSearchBarUI(),
          featuredUI()
        ],
      ),
    );
  }

  Widget appsSearchBarUI(){
    return Expanded(
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
               Padding(
                padding: const EdgeInsets.only(left: 18.0),
                child: HeadingText1(title: 'PUBG',
                 fontSize: 13.5, color: Colors.black87),
                ),
            ],
          ),
        ),
      ),
    );
  }

  Widget featuredUI() {
    return Expanded(
      flex: 88,
      child: ListView(
        children: [
         festivalSeasonUI(),
          optionChoosingUI(),
          newAppsWeLoveHeadingUI(),
         newAppsWeLoveUI(),
         WinterShoppingBannerUI(),
          weeklyPopularAppsHeadingUI(),
          weeklyPopularAppsUI(),
        ],
      ),
    );
  }


  Widget newAppsWeLoveHeadingUI() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(
        children: [
          const Text('New apps we love',
              style: TextStyle(fontSize: 15, color: Colors.black)),
          const Spacer(),
          GestureDetector(
            onTap: (){
              navPush(context, const MoreAppsActivity(appBarName: "New apps we love",));
            },
            child: SizedBox(
              height: 40,width: 60,
              child: Row(children: const [Text(
              'More',
              style: TextStyle(fontSize: 13),
            ),
              SizedBox(width: 5,),
              Icon(
                Icons.arrow_forward_ios_outlined,
                size: 13,
              )],),),
          )
        ],
      ),
    );
  }




  Widget newAppsWeLoveUI() {
    return Padding(
      padding: const EdgeInsets.only(left:18.0),
      child: SizedBox(
        width: double.infinity,
        height: 316,
        child: ListView.builder(
            scrollDirection: Axis.horizontal,
            itemCount: data==null?0: data!.length,
            itemBuilder: (ctx,index){
              data1=data![index]['Category'];
              return Padding(
                padding: const EdgeInsets.only(top:20,bottom: 20,right: 5,left: 3),
                child: Container(
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(10),
                      color: Colors.white
                  ),
                  height: 110,
                  width: MediaQuery.of(context).size.width*.90,
                  child: newAppWeLoveListUI(),
                ),
              );
            }),
      ),
    );
  }

  Widget newAppWeLoveListUI() {
    return Column(
        mainAxisSize: MainAxisSize.min,
        children: data1!.map((item) => InkWell(
      onTap: (){
        navPush(context, AppsDetailsActivity
          (appId: item['id'],appIcon: item['img'],
          appName: item['name'],));
      },
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(0.0),
            child: Row(
              children: [
                Container(
                  margin: const EdgeInsets.all(8),
                  height: 70,
                  width: MediaQuery.of(context).size.width *.17,
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(14),
                      color: Colors.white,
                      border: Border.all(
                          width: 1, color: const Color(0xffe3e2e2))),
                  child: ClipRRect(
                      borderRadius: BorderRadius.circular(14),
                      child: Image.network(item['img'],
                        fit: BoxFit.fitHeight,)),
                ),
                SizedBox(
                  width: MediaQuery.of(context).size.width * .68,
                  child: Padding(
                    padding: const EdgeInsets.only(top: 14.0),
                    child: Column(
                      children: [
                        Row(
                          children: [
                            SizedBox(
                              width: MediaQuery.of(context)
                                  .size
                                  .width *
                                  .40,
                              child: Column(
                                children: [
                                  Align(
                                    alignment: Alignment.centerLeft,
                                    child: RichText(
                                        overflow: TextOverflow.ellipsis,
                                        text: TextSpan(
                                            text:
                                            item['name'],
                                            style: const TextStyle(
                                                color: Colors.black))),
                                  ),
                                  Padding(
                                    padding: const EdgeInsets.only(top:4.0),
                                    child: Align(
                                        alignment: Alignment.centerLeft,
                                        child: Text(item['category'],textAlign: TextAlign.left,)),
                                  ),
                                ],
                              ),
                            ),
                            const Spacer(),
                            Padding(
                              padding: const EdgeInsets.only(
                                  right: 8.0),
                              child: Container(
                                width: 80,
                                height: 34,
                                decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.circular(15),
                                    color: Color(0xfff1f1f1)),
                                child: Center(
                                  child: Text(
                                    'Install'.toUpperCase(),
                                    style: const TextStyle(
                                        color: Color(0xff0a58f5)),
                                  ),
                                ),
                              ),
                            )
                          ],
                        ),

                        Align(
                            alignment: Alignment.centerLeft,
                            child: Padding(
                              padding:
                              const EdgeInsets.only(top: 5.0),
                              child: RichText(
                                  overflow: TextOverflow.ellipsis,
                                  text: TextSpan(
                                      text:
                                     item['description'],
                                      style: const TextStyle(
                                          color: Colors.black))),
                            )),
                        Padding(
                          padding: const EdgeInsets.only(
                              top: 20.0, right: 10),
                          child: Container(
                            width: double.infinity,
                            height: 1,
                            color: Colors.grey[200],
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    )).toList());
  }

 Widget weeklyPopularAppsHeadingUI() {
     return Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(
        children: [
          const Text('Weekly Popular Apps',
              style: TextStyle(fontSize: 15, color: Colors.black)),
          const Spacer(),
          GestureDetector(
            onTap: (){
              navPush(context, const MoreAppsActivity(appBarName: 'Weekly Popular Apps',));
            },
            child: SizedBox(
              height: 40,width: 60,
              child: Row(children: const [Text(
              'More',
              style: TextStyle(fontSize: 13),
            ),
              SizedBox(width: 5,),
              Icon(
                Icons.arrow_forward_ios_outlined,
                size: 13,
              )],),),
          )
        ],
      ),
    );
 }

  Widget weeklyPopularAppsUI() {
    return Padding(
      padding: const EdgeInsets.only(left:18.0),
      child: SizedBox(
        width: double.infinity,
        height: 316,
        child: ListView.builder(
            scrollDirection: Axis.horizontal,
            itemCount: data==null?0: data!.length,
            itemBuilder: (ctx,index){
              data1=data![index]['Category'];
              return Padding(
                padding: const EdgeInsets.only(top:20,bottom: 20,right: 5,left: 3),
                child: Container(
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(10),
                      color: Colors.white
                  ),
                  height: 110,
                  width: MediaQuery.of(context).size.width*.90,
                  child: newAppWeLoveListUI(),
                ),
              );
            }),
      ),
    );
  }

 Widget festivalSeasonUI() {
    return  Container(
      margin: const EdgeInsets.fromLTRB(20, 10, 20, 0),
      width: double.infinity,
      height: 390,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(13),
      ),
      child: Column(
        children: [
          SizedBox(
              width: double.infinity,
              height: 320,
              child: ClipRRect(
                borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(13),
                    topRight: Radius.circular(13)),
                child: Image.asset(
                  assetsfestivalImg,
                  fit: BoxFit.fitWidth,
                ),
              )),
          Padding(
            padding: const EdgeInsets.only(top: 8.0, left: 8),
            child: Align(
                alignment: Alignment.centerLeft,
                child: HeadingText1(
                  title: 'Make plans for upcoming holidays ',
                  color: Colors.grey[700],
                )),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 8.0, left: 8),
            child: Align(
                alignment: Alignment.centerLeft,
                child: HeadingText1(
                  title:'Festival Season is here!',
                  fontSize: 20, color: Colors.black,
                )),
          ),
        ],
      ),
    );
 }

 Widget optionChoosingUI() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(
        children: [
          Expanded(
            child: Column(
              children: [
                Padding(
                  padding: const EdgeInsets.only(top: 4.0),
                  child: Image.asset(
                    assetsGamepadImg,
                    height: 50,
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 8.0),
                  child: HeadingText1(
                    title:'Games', color: Colors.black,
                  ),
                )
              ],
            ),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(left: 30.0),
              child: Column(
                children: [
                  Image.asset(
                    assetsGiftImg,
                    height: 50,
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 12.0),
                    child: HeadingText1(
                      title:'Gifts',
                      color: Colors.black,
                    ),
                  )
                ],
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () {
                navPush(context, const CategoryActivity());
              },
              child: Padding(
                padding: const EdgeInsets.only(left: 30.0),
                child: Column(
                  children: [
                    Image.asset(
                      assetsFileImg,
                      height: 50,
                    ),
                    Padding(
                      padding: const EdgeInsets.only(top: 12.0),
                      child: HeadingText1(
                        title:'Category',
                        color: Colors.black,
                      ),
                    )
                  ],
                ),
              ),
            ),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(left: 30.0),
              child: Column(
                children: [
                  Image.asset(
                    assetsWishlistImg,
                    height: 50,
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 12.0),
                    child: HeadingText1(
                      title:'Wishlist',color: Colors.black,
                    ),
                  )
                ],
              ),
            ),
          )
        ],
      ),
    );
 }

 Widget WinterShoppingBannerUI() {
    return  Container(
      margin: const EdgeInsets.fromLTRB(20, 10, 20, 0),
      width: double.infinity,
      height: 410,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(15),
      ),
      child: Column(
        children: [
          SizedBox(
              width: double.infinity,
              height: 340,
              child: ClipRRect(
                  borderRadius: BorderRadius.only(
                      topLeft: Radius.circular(13),
                      topRight: Radius.circular(13)),
                  child: Image.asset(
                    "assets/shirts.jpg",
                    fit: BoxFit.fitWidth,
                  ))),
          Padding(
            padding: const EdgeInsets.only(top: 8.0, left: 8),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Redesign your home and closet',
                  style: TextStyle(color: Colors.grey[700]),
                )),
          ),
          const Padding(
            padding: EdgeInsets.only(top: 8.0, left: 8),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Winter Shopping',
                  style: TextStyle(fontSize: 20, color: Colors.black),
                )),
          ),
        ],
      ),
    );
 }
}
