import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/response/billing_total_response.dart';

class RewardDetailReportController extends GetxController {

  final PageController pageController = PageController();
  final fullDetailList          = RxList<TotalprofitResponse>();
  final directQualificationList = RxList<TotalprofitResponse>();
  final teamQualificationList   = RxList<TotalprofitResponse>();

  final fullDetailTotal = 0.0.obs;
  final fullDetailToday = 0.0.obs;

  final directQualificationTotal = 0.0.obs;
  final directQualificationToday = 0.0.obs;

  final teamQualificationTotal = 0.0.obs;
  final teamQualificationToday = 0.0.obs;


  final isFullListEmpty             = false.obs;
  final isDirectQualificationEmpty  = false.obs;
  final isTeamQualificationEmpty    = false.obs;

  @override
  void onInit(){
    super.onInit();
    fullDetail();
    directQualification();
    teamQualification();
  }

  void fullDetail() async{
    ApiResponse response = await totalprofitAPI('fullincome');
    fullDetailList.value = response.data;

    double totalP = 0.0;
    double todayP = 0.0;
    for (var element in fullDetailList) {
      totalP += element.profit;
      if(isToday(element.transdate)){
        todayP += element.profit;
      }
    }
    fullDetailTotal.value = totalP;
    fullDetailToday.value = todayP;
    isFullListEmpty.value = true;
  }


  void directQualification() async{
    ApiResponse response = await totalprofitAPI('directqualification');
    directQualificationList.value = response.data;

    double totalP = 0.0;
    double todayP = 0.0;
    for (var element in directQualificationList) {
      totalP += element.profit;
      if(isToday(element.transdate)){
        todayP += element.profit;
      }
    }
    directQualificationTotal.value = totalP;
    directQualificationToday.value = todayP;
    isDirectQualificationEmpty.value = true;
  }


  void teamQualification() async{
    ApiResponse response = await totalprofitAPI('teamqualification');
    teamQualificationList.value = response.data;

    double totalP = 0.0;
    double todayP = 0.0;
    for (var element in teamQualificationList) {
      totalP += element.profit;
      if(isToday(element.transdate)){
        todayP += element.profit;
      }
    }
    teamQualificationTotal.value = totalP;
    teamQualificationToday.value = todayP;
    isTeamQualificationEmpty.value = true;
  }

  bool isToday(time){
    final now = DateTime.now();
    DateFormat dateFormat = DateFormat("yyyy-MM-dd");
    DateTime dateToCheck = dateFormat.parse(time);
    final today = DateTime(now.year, now.month, now.day);
    final aDate = DateTime(dateToCheck.year, dateToCheck.month, dateToCheck.day);
    return aDate == today;
  }
}
