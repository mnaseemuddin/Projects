import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';

class CommunityController extends GetxController {

  final communityResponse     = Rxn<CommunityResponse>();
  final communityListResponse = RxList<CommunityListResponse>();
  final communityListResponseSearch = RxList<CommunityListResponse>();

  final topPosition = 0.0.obs;
  final ScrollController scrollController = ScrollController();

  @override
  void onInit(){
   super.onInit();
   getcommunity();
   communityList();

   scrollController.addListener(() {
     topPosition.value = -scrollController.offset/2;
   });

  }

  void getcommunity() async{
    ApiResponse response = await getcommunityAPI();
    if(response.status){
      communityResponse.value = response.data;
    }else{
      EasyLoading.showToast('unable to load data');
    }
  }

  void communityList() async{
    ApiResponse response = await communityListAPI();
    if(response.status){
      communityListResponse.value = response.data;
      communityListResponse.sort((a,b) => b.doj.compareTo(a.doj));
      communityListResponseSearch.value = List.from(communityListResponse);
    }else{
      EasyLoading.showToast('unable to load data');
    }
  }
}
