



import 'dart:convert';
import 'package:appgallery/Model/featurednewappsmodel.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';

class FeaturedController extends GetxController{

  List<FeaturedNewAppsModel>featuredNewAppList=[];
  List? data,data1,data2;


  @override
  void onInit() {
    super.onInit();
    loadJsonData();
    featuredNewAppList=[
      FeaturedNewAppsModel(name: 'MX Taka Tak Short Video App |Made in India for you',
      category: 'Photo & video', description: 'Indian Short Videos Community:Funny Videos App,Create & Share Free Short Videos', image:
      assetsMXTakaTakImg),

      FeaturedNewAppsModel(name: 'Hungama Music',
          category: 'Entertainment', description: 'Free Music App, Listen & Download Your Favourite Songs', image:
          assetshungamaImg),

      FeaturedNewAppsModel(name: 'D2H Infinity',
          category: 'Entertainment', description: 'With d2h inifinity app, Recharge d2h account, Add channels & get great offers.', image:
          assetsD2HImg),

      // FeaturedNewAppsModel(name: 'MX Taka Tak Short Video App |Made in India for you',
      //     category: 'Photo & video', description: 'Indian Short Videos Community:Funny Videos App,Create & Share Free Short Videos', image:
      //     assetsMXTakaTakImg),
      //
      // FeaturedNewAppsModel(name: 'D2H Infinity',
      //     category: 'Entertainment', description: 'With d2h inifinity app, Recharge d2h account, Add channels & get great offers.', image:
      //     assetsD2HImg),

    ];
  }

  Future<String> loadJsonData() async {
    var jsonResponse=await rootBundle.loadString('assets/content.json');
      data=json.decode(jsonResponse);
    return 'Success';
  }
}
