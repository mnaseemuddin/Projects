


import 'package:appgallery/Model/mostappsearchedmodel.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:get/get.dart';

class AllAppsSearchController extends GetxController{

  List<String>topSearchList=[];
  List<MostAppSearchedModel>mostAppsSearchedList=[];

  @override
  void onInit() {
    super.onInit();
    topSearchList=['PUBG','Mobile Legends-Bang Bang','Temple Run2',
      'Car Racing','Dr Driving'];

    mostAppsSearchedList=[
      MostAppSearchedModel(img: assetsNinjafruitImg, name: 'Ninja Fruit'),
      MostAppSearchedModel(img: assetsStgameImg, name: "Shooter Assassin"),
      MostAppSearchedModel(img: assetsLodoImg, name: 'Ludo Super'),
      MostAppSearchedModel(img: assetsNinjafruitImg, name: 'Ninja Fruit'),
      MostAppSearchedModel(img: assetsStgameImg, name: "Shooter Assassin"),
      MostAppSearchedModel(img: assetsLodoImg, name: 'Ludo Super')
    ];

  }

}