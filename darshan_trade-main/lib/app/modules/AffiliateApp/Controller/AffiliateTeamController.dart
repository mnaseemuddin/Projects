



import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';

import '../../../../api/apis.dart';
import '../../../models/response/direct_team_response.dart';
import '../../../models/response/level_team_response.dart';
import '../../../models/response/levelwise_response.dart';
import '../../../models/response/rank_team_response.dart';
import '../../../models/response/rankwise_response.dart';

class AffiliateTeamController extends GetxController{
  final PageController pageController = PageController();
  final RxList<DirectTeamResponse> directTeam = <DirectTeamResponse>[].obs;
  final RxList<LevelTeamResponse> levelTeam = <LevelTeamResponse>[].obs;
  final RxList<RankTeamResponse> rankTeam = <RankTeamResponse>[].obs;
  final RxList<LevelWiseResponse> levelWiseTeam = <LevelWiseResponse>[].obs;

  final rankWiseResponse = RxList<RankWiseResponse>();
  final rankWiseListReceived = false.obs;

  @override
  void onInit() {
    super.onInit();
    getDirectTeamList();
    getLevelTeamList();
    getRankTeamList();
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {}

  void getDirectTeamList() async{
    EasyLoading.show();
    ApiResponse response = await directTeamAPI();
    if(response.status){
      directTeam.value = response.data;
    }
    EasyLoading.dismiss();
  }

  void getLevelTeamList() async{
    EasyLoading.show();
    ApiResponse response = await levelTeamAPI();
    if(response.status){
      levelTeam.value = response.data;
      levelTeam.value.sort((a, b){
        return a.level.compareTo(b.level);
      });
    }
    EasyLoading.dismiss();
  }

  void getRankTeamList() async{
    EasyLoading.show();
    ApiResponse response = await rankTeamAPI();
    if(response.status){
      rankTeam.value = response.data;
      rankTeam.value.sort((a, b){
        return a.ranks.compareTo(b.ranks);
      });
    }
    EasyLoading.dismiss();
  }


  void getLevelWise(level) async{
    EasyLoading.show();
    ApiResponse response = await getLevelWiseAPI(level);
    if(response.status){
      levelWiseTeam.value = response.data;
      levelWiseTeam.value.sort((a, b){
        return a.rank.compareTo(b.rank);
      });
    }
    EasyLoading.dismiss();
  }

  void rankwiselist(rank)async{
    ApiResponse response = await rankwiselistAPI(rank);
    rankWiseResponse.value = response.data;
    rankWiseListReceived.value = true;
  }
}