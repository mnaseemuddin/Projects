import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/response/member_response.dart';
import 'package:royal_q/app/models/response/members_response.dart';

class MemberCenterController extends GetxController {

  final memberData = Rxn<MemberResponse>();
  final isAccepted = false.obs;

  final membersResponse = RxList<MembersResponse>();
  final listElement = RxList<ListElement>();

  @override
  void onInit(){
    super.onInit();
    getMembersData();
    loadLocalFile();
  }

  void getMembersData() async{
    ApiResponse apiResponse = await memberCenterAPI();
    if(apiResponse.status){
      memberData.value = apiResponse.data;
    }else{
      // EasyLoading.showToast(apiResponse.data['message'], duration: Duration(seconds: 2));
    }
  }

  void showActivationDialog(context, child){
    showDialog(context: context, builder: (context) => child);
  }

  void renewUser() async{
    EasyLoading.show();
    ApiResponse apiResponse = await activationIDAPI();
    EasyLoading.dismiss();
    EasyLoading.showToast(apiResponse.data['message']);
    Get.back();
    if(apiResponse.status){
      ApiResponse response = await getProfileAPI(userInfo!.id);
      userInfo = response.data;
      userInfo!.ispaid = 1;
      // await GetStorage().write('user_info', userInfo);
    }
  }

  void loadLocalFile() async{
    String data = await rootBundle.loadString('assets/json_files/members_rule.json');
    membersResponse.value = membersResponseFromJson(data);
    listElement.value = membersResponse.first.list;
  }
}
