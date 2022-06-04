import 'dart:async';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/request/register_request.dart';
import 'package:royal_q/app/models/request/update_request.dart';
import 'package:royal_q/app/models/request/updatedp_request.dart';
import 'package:royal_q/app/models/response/register_response.dart';


import 'api.dart';

class ApiRepository {
  ApiRepository({required this.apiProvider});

  final ApiProvider apiProvider;
  Future<LoginResponse?> login(LoginRequest data) async {
    // EasyLoading.show(status: 'loading');
    // ApiResponse apiResponse = await loginAPI(data.toJson());
    // EasyLoading.dismiss();
    // if(apiResponse.status){
    //   return apiResponse.data;
    // }else{
    //   return null;
    // }
    final res = await apiProvider.login('/api/account/Login', data);
    print(res);
    if (res.statusCode == 200) {
      // LoginResponse lResp = LoginResponse.fromJson(res.body);
      return LoginResponse.fromJson(res.body);
    }else{
      return null;
    }
  }

  Future<RegisterResponse?> register(RegisterRequest data) async {
    final res = await apiProvider.register('/api/account/Register', data);
    if (res.statusCode == 200) {
      return RegisterResponse.fromJson(res.body);
    }else{

      return RegisterResponse(status: 'failure', message: 'Something went wrong please try again');
    }
  }

  Future<RegisterResponse?> updateProfile(UpdateRequest data) async {
    final res = await apiProvider.updateProfile('/api/account/updateprofile', data);
      return RegisterResponse.fromJson(res.body);
  }

  Future<RegisterResponse?> updateDp(UpdateDpRequest data) async {
    final res = await apiProvider.updateDp('/api/account/UploadDp', data);
    if (res.statusCode == 200) {
      return RegisterResponse.fromJson(res.body);
    }else{
      EasyLoading.dismiss();
      return null;
    }

  }

  Future<UserInfo?> getUserInfo(int id) async {
    final res = await apiProvider.getData('/api/account/GetProfile/$id', false);
      return UserInfo.fromJson(res.body);
  }

  // Future<List<CurrencyModel>?> getCurrency() async {
  //   ApiResponse response = await getCurChngListAPI(1);
  //   return response.data??currencyModelFromJson('[]');
  // }


}
