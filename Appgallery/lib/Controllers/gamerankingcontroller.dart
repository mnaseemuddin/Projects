


import 'package:appgallery/Model/featurednewappsmodel.dart';
import 'package:appgallery/Model/gamerankingheadingmodel.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class GameRankingController extends GetxController{

  List<GameRankingHeadingModel>gameRankingHeadingList=[];
  List<FeaturedNewAppsModel>topGrossingList=[];

  @override
  void onInit() {
    super.onInit();
   getGameRankingHeadingData();
  }

  void getGameRankingHeadingData()async {
    gameRankingHeadingList=[
      GameRankingHeadingModel(name: 'Top Grossing', color: Color(0xffd2d9e5)),
      GameRankingHeadingModel(name: 'Trending', color: Color(0xffe8e8e8)),
      GameRankingHeadingModel(name: 'Most Searched', color: Color(0xffe8e8e8)),
      GameRankingHeadingModel(name: 'Offline', color: Color(0xffe8e8e8)),
      GameRankingHeadingModel(name: 'Recent Searched', color: Color(0xffe8e8e8))
    ];
    topGrossingList=[
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
//'Top Grossing','Trending','Most Searched',
//       'Offline','Recent Searched'
}