


import 'package:appgallery/Model/mostappsearchedmodel.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:get/get.dart';

class AllAppsDetailsController extends GetxController{
  List <MostAppSearchedModel>alsoPopularList=[];


  @override
  void onInit() {

    alsoPopularList=[
      MostAppSearchedModel(img: assetsNinjafruitImg, name: 'Ninja Fruit'),
      MostAppSearchedModel(img: assetsStgameImg, name: "Shooter Assassin"),
      MostAppSearchedModel(img: assetsLodoImg, name: 'Ludo Super'),
      MostAppSearchedModel(img: assetsNinjafruitImg, name: 'Ninja Fruit'),
      MostAppSearchedModel(img: assetsStgameImg, name: "Shooter Assassin"),
      MostAppSearchedModel(img: assetsLodoImg, name: 'Ludo Super')
    ];

    super.onInit();
  }

}