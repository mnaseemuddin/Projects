import 'dart:async';

import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get_connect/http/src/request/request.dart';
import 'package:royal_q/app/data/user_data.dart';

FutureOr<Request> requestInterceptor(request) async {
  // final token = StorageService.box.pull(StorageItems.accessToken);

  // request.headers['X-Requested-With'] = 'XMLHttpRequest';
  // request.headers['Authorization'] = 'Bearer $token';
  if(isToast)EasyLoading.showToast('loading');
    else EasyLoading.show(status: 'loading...');
  // isToast?:EasyLoading.show(status: 'loading...');
  return request;
}
