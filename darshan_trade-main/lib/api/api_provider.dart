import 'package:get/get.dart';
import 'package:royal_q/api/base_provider.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/request/register_request.dart';
import 'package:royal_q/app/models/request/update_request.dart';


class ApiProvider extends BaseProvider {
  Future<Response> login(String path, LoginRequest data) {
    return post(path, data.toJson());
  }
  //
  Future<Response> register(String path, RegisterRequest data) {
    return post(path, data.toJson());
  }

  Future<Response> updateProfile(String path, UpdateRequest data) {
    return post(path, data.toJson());
  }

  Future<Response> updateDp(String path, UpdateDpRequest data) {
    return post(path, data.toJson());
  }

  Future<Response> getData(String path, val) {
    isToast = val;
    return get(path);
  }

  //
  // Future<Response> getUsers(String path) {
  //   return get(path);
  // }
  //
  // Future<Response> commonPost(String path, ArtistRequest data) {
  //   return post(path, data.toJson());
  // }
}
